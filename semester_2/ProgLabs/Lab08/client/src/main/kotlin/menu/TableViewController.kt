package menu

import client.ClientSession
import entities.Coordinates
import entities.Location
import entities.Route
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableRow
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.input.MouseButton
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.stage.Modality
import javafx.stage.Popup
import javafx.stage.Stage
import network.Request
import utils.MENU_WINDOW_HEIGHT
import utils.MENU_WINDOW_WIDTH
import utils.TABLE_MAX_NUM_OBJECTS_WITHOUT_SCROLL
import utils.TABLE_NUM_PIXELS_TO_HIDE_SCROLL
import utils.exceptions.UnsuccessfulRequestException
import java.net.URL
import java.util.*

class TableViewController(private val session: ClientSession) : Initializable{
    @FXML lateinit var table: TableView<Route>

    @FXML lateinit var idColumn: TableColumn<Route, Int>
    @FXML lateinit var authorColumn: TableColumn<Route, String>
    @FXML lateinit var dateColumn: TableColumn<Route, Date>
    @FXML lateinit var nameColumn: TableColumn<Route, String>
    @FXML lateinit var coordinatesColumn: TableColumn<Route, Coordinates>
    @FXML lateinit var fromColumn: TableColumn<Route, Location>
    @FXML lateinit var toColumn: TableColumn<Route, Location>
    @FXML lateinit var distanceColumn: TableColumn<Route, Double>

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        table.setRowFactory {
            val row: TableRow<Route> = object: TableRow<Route>() {}

            row.setOnMouseClicked { me ->
                if (me.button == MouseButton.PRIMARY && me.clickCount == 2) { // Left double click
                    val dialog = Stage()

                    dialog.initModality(Modality.APPLICATION_MODAL)
                    dialog.initOwner(table.scene.window)

                    val dialogVbox = VBox(20.0)
                    dialogVbox.children.add(Text("This is a Dialog"))

                    val dialogScene = Scene(dialogVbox, 400.0, 300.0)
                    dialog.scene = dialogScene
                    dialog.show()
                }
            }

            row
        }

        val columns = arrayListOf(
            idColumn, authorColumn, dateColumn, nameColumn, coordinatesColumn, fromColumn, toColumn, distanceColumn
        )
        val columnsNames = arrayListOf(
            "id", "author", "creationDate", "name", "coordinates", "from", "to", "distance"
        )

        for (columnNumber in columns.indices) {
            columns[columnNumber].isReorderable = false
            columns[columnNumber].cellValueFactory = PropertyValueFactory(columnsNames[columnNumber])
        }

        val items = FXCollections.observableArrayList<Route>()
        table.items = items

        try {
            val routes = session.socketWorker.makeRequest(
                Request(token = session.userToken, command = "show")
            ).routesCollection

            if (routes != null && routes.isNotEmpty()) {
                session.entitiesCollection.addAll(routes)
                table.items.addAll(routes)

                if (routes.size <= TABLE_MAX_NUM_OBJECTS_WITHOUT_SCROLL)
                    distanceColumn.prefWidth += TABLE_NUM_PIXELS_TO_HIDE_SCROLL
            } else {
                // TODO write message than No objects in collection
                println("No objects in collection")
            }
        } catch (e: UnsuccessfulRequestException) {
            // TODO write exception message
            println("No objects in collection")
        }
    }
}
