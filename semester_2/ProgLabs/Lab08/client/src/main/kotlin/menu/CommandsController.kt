package menu

import client.ClientSession
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage
import utils.APPLICATION_NAME
import java.net.URL
import java.util.*

class CommandsController(private val session: ClientSession) : Initializable{


    override fun initialize(location: URL?, resources: ResourceBundle?) {

    }

    @FXML
    fun add(event: ActionEvent) {
        val loader = FXMLLoader(javaClass.classLoader.getResource("addwindow.fxml"))
        loader.setControllerFactory { AddWindowController(session) }
        val root: Parent = loader.load()
        val scene = Scene(root)
        val dialog = Stage()
        dialog.title = APPLICATION_NAME
        dialog.scene = scene

        dialog.initOwner((event.target as Node).scene.window)
        dialog.initModality(Modality.WINDOW_MODAL)
        dialog.show()
    }

    @FXML
    fun update() {

    }
}