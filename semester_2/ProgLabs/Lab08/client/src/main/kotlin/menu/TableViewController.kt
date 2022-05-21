package menu

import client.ClientSession
import entities.Coordinates
import entities.Location
import entities.Route
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import utils.TABLE_MAX_NUM_OBJECTS_WITHOUT_SCROLL
import utils.TABLE_NUM_PIXELS_TO_HIDE_SCROLL
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
        table.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY;

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

        // TODO get data from show command
        val numObjects = 16

        for (i in 1..numObjects)
            table.items.add(
                Route(
                    i, Date(), "Stepa", "Test", Coordinates(12, 13),
                    Location("SPB", 1.3, 43.2F), Location("SRV", 1344.23, 13.37F), 123.5
                )
            )

        if (numObjects <= TABLE_MAX_NUM_OBJECTS_WITHOUT_SCROLL)
            distanceColumn.prefWidth += TABLE_NUM_PIXELS_TO_HIDE_SCROLL
    }
}
