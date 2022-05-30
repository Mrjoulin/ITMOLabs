package menu.commands

import client.Client
import client.ClientSession
import input.processScriptFile
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
import javafx.scene.control.TextField
import javafx.stage.FileChooser
import javafx.stage.Modality
import javafx.stage.Stage
import menu.dialogs.AddWindowController
import network.Request
import utils.APPLICATION_ADD_WINDOW
import utils.APPLICATION_NAME
import utils.exceptions.UnsuccessfulRequestException
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.io.PrintStream
import java.net.URL
import java.util.*


class CommandsController(private val session: ClientSession) : Initializable{
    @FXML lateinit var textArea: TextArea
    @FXML lateinit var consoleTextField: TextField

    private var findByNameProcessing = false
    private val client = Client(session)
    private val getInput = { s: String -> InputStreamReader(s.byteInputStream()) }
    private val curByteArrayOutputStream = ByteArrayOutputStream()

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        session.currentOutput = PrintStream(curByteArrayOutputStream)

        consoleTextField.setOnAction {
            val command = consoleTextField.text

            printToConsole(command, !findByNameProcessing)

            consoleTextField.clear()

            if (findByNameProcessing)
                processFindByName(command)
            else {
                session.currentInput = getInput(command)
                curByteArrayOutputStream.reset()

                client.processCommands()

                printToConsole(curByteArrayOutputStream.toString(), command = false, newLine = false)
            }
        }
    }

    @FXML
    fun add(event: ActionEvent) {
        findByNameProcessing = false
        printToConsole("add", true)

        val loader = FXMLLoader(javaClass.classLoader.getResource(APPLICATION_ADD_WINDOW))
        loader.setControllerFactory { AddWindowController(session) }
        val root: Parent = loader.load()
        val scene = Scene(root)
        val dialog = Stage()
        dialog.title = APPLICATION_NAME
        dialog.scene = scene

        // TODO print to console if object added

        dialog.initOwner((event.target as Node).scene.window)
        dialog.initModality(Modality.WINDOW_MODAL)
        dialog.show()
    }

    @FXML
    fun info() {
        findByNameProcessing = false
        printToConsole("info", true)

        try {
            val response = session.socketWorker.makeRequest(
                Request(
                    token = session.userToken,
                    command = "info")
            )

            printToConsole(response.message, false)
        } catch (e: UnsuccessfulRequestException) {
            printToConsole(e.message, false)
        }
    }

    @FXML
    fun help() {
        findByNameProcessing = false
        printToConsole("help", true)

        try {
            val response = session.socketWorker.makeRequest(
                Request(
                    token = session.userToken,
                    command = "help")
            )

            printToConsole(response.message, false)
        } catch (e: UnsuccessfulRequestException) {
            printToConsole(e.message, false)
        }
    }

    @FXML
    fun clearConsole() {
        findByNameProcessing = false
        textArea.clear()
    }

    @FXML
    fun show() {
        findByNameProcessing = false

        printToConsole("show", true)

        try {
            val response = session.socketWorker.makeRequest(
                Request(
                    token = session.userToken,
                    command = "show")
            )

            printToConsole(response.message, false)
        } catch (e: UnsuccessfulRequestException) {
            printToConsole(e.message, false)
        }
    }

    @FXML
    fun delete() {
        findByNameProcessing = false

        val yes = ButtonType("Yes")
        val no = ButtonType("No")

        val alert = Alert(Alert.AlertType.NONE,"Вы действительно хотите удалить все объекты?", yes, no)
        alert.showAndWait().ifPresent {
            if (it == yes) {
                printToConsole("clear", true)

                try {
                    val response = session.socketWorker.makeRequest(
                        Request(
                            token = session.userToken,
                            command = "clear")
                    )

                    printToConsole(response.message, false)
                } catch (e: UnsuccessfulRequestException) {
                    printToConsole(e.message, false)
                }
            }
        }
    }

    @FXML
    fun findByName() {
        printToConsole("filter_starts_with_name", true)

        textArea.appendText("Введите строку, с которой должны начинаться названия маршрутов:\n")

        findByNameProcessing = true
    }

    @FXML
    fun executeScript() {
        findByNameProcessing = false

        printToConsole("execute_script", true)

        val fileChooser = FileChooser()
        fileChooser.title = "Выберите файл с скриптом"
        fileChooser.extensionFilters.add(
            FileChooser.ExtensionFilter("Text files", "*.txt")
        )

        val file = fileChooser.showOpenDialog(textArea.scene.window)

        if (file != null) {
            curByteArrayOutputStream.reset()

            processScriptFile(session = session, filename = file.absolutePath)

            printToConsole(curByteArrayOutputStream.toString(), command = false, newLine = false)
        }
    }

    private fun processFindByName(name: String) {
        try {
            val response = session.socketWorker.makeRequest(
                Request(
                    token = session.userToken,
                    command = "filter_starts_with_name",
                    commandArgs = arrayListOf(name)
                )
            )

            printToConsole(response.message, false)

            findByNameProcessing = false
        } catch (e: UnsuccessfulRequestException) {
            printToConsole(e.message, false)
        }
    }

    private fun printToConsole(message: String?, command: Boolean, newLine: Boolean = true) {
        if (command) textArea.appendText("$ ")

        textArea.appendText(message)

        if (newLine) textArea.appendText("\n")
    }
}