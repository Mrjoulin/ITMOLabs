package menu

import client.ClientSession
import entities.Route
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.TextField
import utils.objectMap
import java.net.URL
import java.util.*

class AddWindowController(private val session: ClientSession) : Initializable {

    @FXML lateinit var authorField: TextField

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        authorField.text = session.username
    }

    fun addRoute() {
        //TODO: implement adding new route
    }
}