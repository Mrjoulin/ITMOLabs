package menu.table

import client.ClientSession
import entities.Coordinates
import entities.Location
import entities.Route
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableRow
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.input.MouseButton
import javafx.scene.text.Text
import javafx.stage.Modality
import javafx.stage.Stage
import menu.dialogs.DialogueWindowController
import network.Request
import utils.*
import utils.exceptions.UnsuccessfulRequestException
import java.net.URL
import java.util.*
import kotlin.collections.HashSet

class TableViewController(private val session: ClientSession) : Initializable{
    @FXML lateinit var table: TableView<Route>
    @FXML lateinit var tableText: Text

    @FXML lateinit var idColumn: TableColumn<Route, Int>
    @FXML lateinit var authorColumn: TableColumn<Route, String>
    @FXML lateinit var dateColumn: TableColumn<Route, Date>
    @FXML lateinit var nameColumn: TableColumn<Route, String>
    @FXML lateinit var coordinatesColumn: TableColumn<Route, Coordinates>
    @FXML lateinit var fromColumn: TableColumn<Route, Location>
    @FXML lateinit var toColumn: TableColumn<Route, Location>
    @FXML lateinit var distanceColumn: TableColumn<Route, Double>

    private var bundle: ResourceBundle = session.currentLanguage

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        bundle = session.currentLanguage
        tableText.text = bundle.getString("tableViewHeaderMessage")
        idColumn.text = bundle.getString("idColumnMessage")
        authorColumn.text = bundle.getString("authorColumnMessage")
        dateColumn.text = bundle.getString("dateColumnMessage")
        nameColumn.text = bundle.getString("nameColumnMessage")
        coordinatesColumn.text = bundle.getString("coordinatesColumnMessage")
        fromColumn.text = bundle.getString("fromColumnMessage")
        toColumn.text = bundle.getString("toColumnMessage")
        distanceColumn.text = bundle.getString("distanceColumnMessage")
        table.setRowFactory {
            val row: TableRow<Route> = TableRow<Route>()

            row.setOnMouseClicked { me ->
                if (me.button == MouseButton.PRIMARY && me.clickCount == 2) { // Left double click
                    val loader = FXMLLoader(javaClass.classLoader.getResource(APPLICATION_DIALOG_WINDOW))
                    loader.setControllerFactory { DialogueWindowController(session, row.item) }

                    val scene = Scene(loader.load())
                    val dialog = Stage()
                    dialog.title = APPLICATION_NAME
                    dialog.scene = scene
                    dialog.initOwner(table.scene.window)
                    dialog.initModality(Modality.WINDOW_MODAL)
                    dialog.setOnHidden {
                        updateRoutes()
                    }

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

        val routes = getCollection()

        table.items.addAll(routes)

        if (routes.size <= TABLE_MAX_NUM_OBJECTS_WITHOUT_SCROLL)
            distanceColumn.prefWidth += TABLE_NUM_PIXELS_TO_HIDE_SCROLL
    }

    private fun getCollection() : HashSet<Route> {
        if (session.collectionInitialized)
            return session.entitiesCollection

        try {
            val routes = session.socketWorker.makeRequest(
                Request(token = session.userToken, command = "show")
            ).routesCollection

            if (routes != null && routes.isNotEmpty()) {
                session.collectionInitialized = true
                session.entitiesCollection.addAll(routes)

                return session.entitiesCollection
            } else {
                println("No objects in collection")
            }
        } catch (e: UnsuccessfulRequestException) {
            println("Error while getting objects from server: ${e.message}")
        }

        return HashSet()
    }

    private fun updateRoutes() {
        for (route in session.entitiesCollection) { }
    }
}
