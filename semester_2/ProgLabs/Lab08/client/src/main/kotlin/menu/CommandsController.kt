package menu

import client.ClientSession
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.TextArea
import javafx.stage.Modality
import javafx.stage.Stage
import network.Request
import utils.APPLICATION_NAME
import java.net.URL
import java.util.*

class CommandsController(private val session: ClientSession) : Initializable{


    @FXML lateinit var textArea: TextArea

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
    fun info() {
        textArea.appendText("\$info\n")
        val response = session.socketWorker.makeRequest(
            Request(
                token = session.userToken,
                command = "info")
        )

        if (response.success) {
            textArea.appendText(response.message + "\n")
        } else {
            //TODO
        }
    }

    @FXML
    fun help() {
        textArea.appendText("\$help\n")
        val response = session.socketWorker.makeRequest(
            Request(
                token = session.userToken,
                command = "help")
        )

        textArea.appendText(response.message + "\n")
    }

    @FXML
    fun clearConsole() {
        textArea.clear()
    }

    @FXML
    fun show() {
        textArea.appendText("\$show\n")
        val response = session.socketWorker.makeRequest(
            Request(
                token = session.userToken,
                command = "show")
        )

        textArea.appendText(response.message + "\n")
    }

    @FXML
    fun delete() {
        //TODO: ALERT MESSAGE
        val yes = ButtonType("Yes")
        val no = ButtonType("No")

        val alert = Alert(Alert.AlertType.NONE,"Вы действительно хотите удалить все объекты?", yes, no)
        alert.showAndWait().ifPresent {
            if (it == yes) {
                textArea.appendText("\$clear\n")
                val response = session.socketWorker.makeRequest(
                    Request(
                        token = session.userToken,
                        command = "clear")
                )

                textArea.appendText(response.message + "\n")
            }
        }
    }
}