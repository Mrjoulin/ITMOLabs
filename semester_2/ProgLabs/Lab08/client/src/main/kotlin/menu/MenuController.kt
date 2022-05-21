package menu

import client.ClientSession
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.layout.AnchorPane
import utils.APPLICATION_TABLE_SECTION
import java.io.IOException
import java.net.URL
import java.util.*

class MenuController(private val session: ClientSession) : Initializable {

    @FXML
    lateinit var mainpane: AnchorPane;

    @FXML
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        loadUI(APPLICATION_TABLE_SECTION, TableViewController(session))
    }

    @FXML
    fun table(event: ActionEvent) {
        loadUI(APPLICATION_TABLE_SECTION, TableViewController(session))
    }

    @FXML
    fun visualization(event: ActionEvent) {
        loadUI(APPLICATION_TABLE_SECTION, TableViewController(session))
    }

    @FXML
    fun commands(event: ActionEvent) {
        loadUI(APPLICATION_TABLE_SECTION, TableViewController(session))
    }

    private fun loadUI(ui: String, controller: Any) {
        try {
            val loader = FXMLLoader(javaClass.getResource(ui))
            loader.setControllerFactory { controller }

            val root: Parent = loader.load()
            mainpane.children.add(root)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}