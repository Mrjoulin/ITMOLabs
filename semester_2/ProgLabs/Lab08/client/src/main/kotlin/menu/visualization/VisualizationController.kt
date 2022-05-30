package menu.visualization

import client.ClientSession
import entities.Coordinates
import entities.Location
import entities.Route
import javafx.animation.AnimationTimer
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.geometry.VPos
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.*
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment
import javafx.scene.transform.Affine
import javafx.scene.transform.Transform
import javafx.stage.Modality
import javafx.stage.Stage
import menu.dialogs.DialogueWindowController
import utils.*
import java.net.URL
import java.util.*
import java.util.concurrent.Semaphore
import kotlin.math.*
import kotlin.properties.Delegates


class VisualizationController (private val session: ClientSession) : Initializable {
    @FXML
    lateinit var canvas: Canvas
    @FXML
    lateinit var routesPane: AnchorPane
    @FXML
    lateinit var visualizationLabel: Text

    private lateinit var bundle: ResourceBundle

    private lateinit var gc: GraphicsContext

    private var coordinatePlaneOffsetX by Delegates.notNull<Double>()
    private var coordinatePlaneOffsetY by Delegates.notNull<Double>()

    private val personImage = Image(PERSON_IMAGE_PATH)
    private val transparentColor = Color.rgb(0, 0, 0,0.0)
    private val semaphore = Semaphore(NUM_OBJECTS_TO_DRAW_SYNC)

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        bundle = session.currentLanguage
        visualizationLabel.text = bundle.getString("visualizationLabel")
        gc = canvas.graphicsContext2D

        coordinatePlaneOffsetX = canvas.width / 2
        coordinatePlaneOffsetY = canvas.height / 2

        logger.debug("Coordinate pane center: $coordinatePlaneOffsetX, $coordinatePlaneOffsetY")

        // Start drawing routes
        drawCollectionRoutes()
    }

    private fun onMouseClick(route: Route, mouseEvent: MouseEvent) {
        if (mouseEvent.button == MouseButton.PRIMARY) { // Left click
            val loader = FXMLLoader(javaClass.classLoader.getResource(APPLICATION_DIALOG_WINDOW))
            loader.setControllerFactory { DialogueWindowController(session, route) }

            val scene = Scene(loader.load())

            val dialog = Stage()
            dialog.title = APPLICATION_NAME
            dialog.scene = scene
            // dialog.setOnCloseRequest { updateRoutes() }
            dialog.initOwner(routesPane.scene.window)
            dialog.initModality(Modality.WINDOW_MODAL)
            dialog.show()
        }
    }

    private fun drawCollectionRoutes() {
        logger.debug("Collection size: ${session.entitiesCollection.size}")

        session.entitiesCollection.forEach {
            val routeThread = Thread {
                semaphore.acquire()
                drawRoute(it)
            }
            routeThread.name = "Id #${it.id}"
            routeThread.isDaemon = true
            routeThread.start()
        }
    }

    @Synchronized private fun drawRoute(route: Route) {
        val authorColor = getUserColor(route.author)

        logger.debug("Start drawing object #${route.id}: ${route.from} -> ${route.coordinates} -> ${route.to}")

        try {
            // Dots on start and end
            val dot1: Node = drawDot(authorColor, route.from)
            val dot2: Node = drawDot(authorColor, route.to)
            // Write names of locations
            drawLocationsNames(route.from, route.coordinates, route.to)
            // Person
            val person: Node = drawPerson(route.coordinates)

            // Create group
            val group = Group(dot1, dot2, person)

            // Start drawing line - route
            // After drawing release semaphore and set mouse event listeners to each children of group
            drawLine(authorColor, group, route)
        } catch (e: Exception) {
            println("Error while drawing route #${route.id}: ${e.message}")
            semaphore.release()
        }
    }

    private fun drawDot(color: Color, location: Location) : Circle {
        val dotX = getX(location)
        val dotY = getY(location)

        synchronized(gc) {
            gc.fill = color

            gc.fillOval(dotX, dotY, VISUALIZATION_DOT_SIZE, VISUALIZATION_DOT_SIZE)
        }

        return Circle(dotX, dotY, VISUALIZATION_DOT_SIZE, transparentColor)
    }

    private fun drawPerson(coordinates: Coordinates): Rectangle {
        val personX = getX(coordinates)
        val personY = getY(coordinates)

        synchronized(gc) {
            gc.drawImage(personImage, personX, personY)
        }

        return Rectangle(personX, personY, PERSON_WIDTH, PERSON_HEIGHT).apply { fill = transparentColor }
    }

    private fun drawLine(color: Color, group: Group, route: Route) {
        val fromX = getX(route.from, dotCenter = true); val fromY = getY(route.from, dotCenter = true)
        val toX = getX(route.to, dotCenter = true); val toY = getY(route.to, dotCenter = true)

        val middleX = getX(route.coordinates, person = toY < fromY) // Add shear if line from bottom to top
        val middleY = getY(route.coordinates, person = true)

        val cond1 = (middleY < toY).xor(middleY < fromY)
        val cond2 = cond1.xor(!(fromX < middleX).xor(toX < fromX))

        val controlX1 = if (cond1) middleX else fromX
        val controlY1 = if (cond1) fromY else middleY
        val controlX2 = if (cond2) middleX else toX
        val controlY2 = if (cond2) toY else middleY

        gc.lineWidth = VISUALIZATION_LINE_WIDTH
        gc.setLineDashes(VISUALIZATION_LINE_DASH)

        // Create animation timer to animate line drawing

        val timer: AnimationTimer = object : AnimationTimer() {
            private val threadName = Thread.currentThread().name
            private val distanceDivideBy = 10.0
            private val gradientScale = (distToMiddle(fromX, fromY) + distToMiddle(toX, toY)) / distanceDivideBy
            private var curMiddle = 0.0
            private val stops = arrayListOf(
                Stop(0.0, color), Stop(curMiddle, color),
                Stop(curMiddle, transparentColor), Stop(1.0, transparentColor)
            )

            fun distToMiddle(startX: Double, startY: Double): Double =
                sqrt((startX - middleX).pow(2) + (startY - middleY).pow(2))

            // Animation of line drawing
            override fun handle(now: Long) {
                if (curMiddle >= 1.0) return stopDrawing()

                curMiddle += 1 / gradientScale
                stops[1] = Stop(curMiddle, color)
                stops[2] = Stop(curMiddle + 1 / gradientScale, transparentColor)

                synchronized(gc) {
                    gc.beginPath()

                    val prevColor = gc.stroke

                    val startX = if (fromX < toX) 0.0 else 1.0
                    val startY = if (fromY < toY) 0.0 else 1.0
                    val endX = 1.0 - startX
                    val endY = 1.0 - startY

                    val gradient = LinearGradient(startX, startY, endX, endY, true, CycleMethod.NO_CYCLE, stops)
                    gc.stroke = gradient

                    gc.moveTo(fromX, fromY)
                    gc.quadraticCurveTo(controlX1, controlY1, middleX, middleY)
                    gc.quadraticCurveTo(controlX2, controlY2, toX, toY)

                    // Draw arrow on the end of path
                    if (curMiddle >= 1.0) drawArrow(color, fromX, fromY, toX, toY)

                    gc.stroke()

                    gc.stroke = prevColor
                }
            }

            fun stopDrawing() {
                logger.debug("Stop drawing object: $threadName")

                // Create transparent line
                val moveTo = MoveTo(fromX, fromY)
                val quadToMiddle = QuadCurveTo(controlX1, controlY1, middleX, middleY)
                val quadFromMiddle = QuadCurveTo(controlX2, controlY2, toX, toY)

                val path = Path(moveTo, quadToMiddle, quadFromMiddle).apply {
                    stroke = transparentColor
                    strokeWidth = VISUALIZATION_LINE_WIDTH * 2
                }

                // Add path to group and set on mouse click event to all objects
                group.children.add(path)
                group.children.forEach { node ->
                    node.setOnMouseClicked { me ->
                        onMouseClick(route, me)
                    }
                }
                routesPane.children.add(group)

                // Release semaphore to allow other threads start drawing
                semaphore.release()

                // Stop timer
                return stop()
            }
        }

        timer.start()
    }

    private fun drawLocationsNames(from: Location, middle: Coordinates, to: Location) {
        val fromX = getX(from, dotCenter = true); val fromY = getY(from, dotCenter = true)
        val middleY = getY(middle, dotCenter = true)
        val toX = getX(to, dotCenter = true); val toY = getY(to, dotCenter = true)

        val fromTextY = fromY + (if (middleY == fromY) -1.0 else (fromY - middleY).sign) * VISUALIZATION_DOT_SIZE
        val toTextY = toY + (if (middleY == toY) 1.0 else (toY - middleY).sign) * VISUALIZATION_DOT_SIZE

        synchronized(gc) {
            val prevColor = gc.fill

            gc.fill = Color.BLACK
            gc.textAlign = TextAlignment.CENTER
            gc.textBaseline = VPos.CENTER

            gc.fillText(from.name, fromX, fromTextY)
            gc.fillText(to.name, toX, toTextY)

            gc.fill = prevColor
        }
    }

    private fun drawArrow(color: Color, startX: Double, startY: Double, x2: Double, y2: Double) {
        val circleRadius = 15.0
        val coefficient = (y2 - startY) / (x2 - startX)

        fun getNearLineCoordinates(reverse: Boolean): Pair<Double, Double> {
            val arrowXPoint = { t: Double -> x2 + circleRadius * cos(t) }
            val arrowYPoint = { t: Double -> y2 + circleRadius * sin(if (reverse) -t else t) }

            var x1 = 0.0; var y1 = 0.0
            var t = 0.0

            while (t < 2 * PI) {
                if (gc.isPointInPath(arrowXPoint(t), arrowYPoint(t))) {
                    x1 = arrowXPoint(t)
                    y1 = arrowYPoint(t)
                    break
                }
                t += 2 * PI / 1000
            }

            return Pair(x1, y1)
        }

        // Get couple of coordinates (with different types of circular motion)
        val firstCoordinatesPair = getNearLineCoordinates(false)
        val secondCoordinatesPair = getNearLineCoordinates(true)
        // Get their line slope coefficients to (x2, y2)
        val firstCoefficient = abs((y2 - firstCoordinatesPair.second) / (x2 - firstCoordinatesPair.first) - coefficient)
        val secondCoefficient = abs((y2 - secondCoordinatesPair.second) / (x2 - secondCoordinatesPair.first) - coefficient)
        // Take as (x1, y1) coordinates with greater line slope coefficient
        val x1 = if (firstCoefficient > secondCoefficient) firstCoordinatesPair.first else secondCoordinatesPair.first
        val y1 = if (firstCoefficient > secondCoefficient) firstCoordinatesPair.second else secondCoordinatesPair.second

        val arrowSize = 12.0
        val dx = x2 - x1
        val dy = y2 - y1

        val angle = atan2(dy, dx)
        val len = sqrt(dx * dx + dy * dy).toInt() - VISUALIZATION_DOT_SIZE / 2

        // Draw a triangle (arrow) with needed rotation angle
        synchronized(gc) {
            val previousTransform = gc.transform

            var transform: Transform = Transform.translate(x1, y1)
            transform = transform.createConcatenation(Transform.rotate(Math.toDegrees(angle), 0.0, 0.0))
            gc.transform = Affine(transform)

            gc.fill = color

            gc.fillPolygon(
                doubleArrayOf(len, len - arrowSize, len - arrowSize, len),
                doubleArrayOf(0.0, -arrowSize / 2, arrowSize / 2, 0.0),
                4
            )

            gc.transform = previousTransform
        }
    }

    private fun getX(obj: Any, dotCenter: Boolean = false, person: Boolean = false): Double {
        var x = when (obj) {
            is Coordinates -> obj.x.toDouble()
            is Location -> obj.x
            else -> throw Exception("Unresolved object")
        }

        x += coordinatePlaneOffsetX
        if (dotCenter) x += VISUALIZATION_DOT_SIZE / 2
        if (person) x += PERSON_WIDTH

        return x
    }

    private fun getY(obj: Any, dotCenter: Boolean = false, person: Boolean = false): Double {
        var y = when (obj) {
            is Coordinates -> obj.y.toDouble()
            is Location -> obj.y.toDouble()
            else -> throw Exception("Unresolved object")
        }

        y += coordinatePlaneOffsetY
        if (dotCenter) y -= VISUALIZATION_DOT_SIZE / 2
        if (person) y -= PERSON_HEIGHT

        return canvas.height - y
    }
}