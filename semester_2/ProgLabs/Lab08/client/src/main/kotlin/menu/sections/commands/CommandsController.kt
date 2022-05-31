package menu.sections.commands

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
import javafx.scene.control.*
import javafx.stage.FileChooser
import javafx.stage.Modality
import javafx.stage.Stage
import menu.dialogs.AddWindowController
import menu.sections.interfaces.UpdatableController
import network.Request
import utils.APPLICATION_ADD_WINDOW
import utils.APPLICATION_NAME
import utils.exceptions.UnsuccessfulRequestException
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.io.PrintStream
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList


class CommandsController(private val session: ClientSession) : UpdatableController, Initializable{
    @FXML lateinit var textArea: TextArea
    @FXML lateinit var consoleTextField: TextField
    @FXML lateinit var commandsLabel: Label
    @FXML lateinit var infoButton: Button
    @FXML lateinit var addButton: Button
    @FXML lateinit var deleteButton: Button
    @FXML lateinit var showButton: Button
    @FXML lateinit var helpButton: Button
    @FXML lateinit var executeScriptButton: Button
    @FXML lateinit var findByNamesButton: Button
    @FXML lateinit var clearConsoleButton: Button
    @FXML lateinit var consoleLabel: Label
    private lateinit var bundle: ResourceBundle

    private var findByNameProcessing = false
    private val client = Client(session)
    private val getInput = { s: String -> InputStreamReader(s.byteInputStream()) }
    private val curByteArrayOutputStream = ByteArrayOutputStream()

    private var currentUserRoutesIds = getCurrentUserRoutesIds()

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        bundle = session.currentLanguage
        commandsLabel.text = bundle.getString("commands.commandsLabel")
        infoButton.text = bundle.getString("commands.infoButton")
        addButton.text = bundle.getString("commands.addButton")
        deleteButton.text = bundle.getString("commands.deleteButton")
        showButton.text = bundle.getString("commands.showButton")
        helpButton.text = bundle.getString("commands.helpButton")
        executeScriptButton.text = bundle.getString("commands.executeScriptButton")
        findByNamesButton.text = bundle.getString("commands.findByNamesButton")
        clearConsoleButton.text = bundle.getString("commands.clearConsoleButton")
        consoleTextField.promptText = bundle.getString("commands.consoleTextField")
        consoleLabel.text = bundle.getString("commands.consoleLabel")

        session.currentOutput = PrintStream(curByteArrayOutputStream)

        consoleTextField.setOnAction {
            val command = consoleTextField.text

            printToConsole(command, !findByNameProcessing)

            consoleTextField.clear()

            if (findByNameProcessing) processFindByName(command)
            else if (command.split(" ")[0] == "add") add(it)
            else if (command.split(" ")[0] == "update")
                printToConsole(bundle.getString("commands.updateMessage"), false)
            else {
                session.currentInput = getInput(command)
                curByteArrayOutputStream.reset()

                client.processCommands()

                printToConsole(curByteArrayOutputStream.toString(), command = false, newLine = false)
            }
        }
    }

    private fun getCurrentUserRoutesIds() : List<Int> {
        return session.collectionManager.getEntitiesSet()
                .filter { it.author == session.username }
                .map { it.id }
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

        val alert = Alert(Alert.AlertType.NONE,bundle.getString("commands.deleteConfirmMessage"), yes, no)
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

        textArea.appendText(bundle.getString("commands.findByNameMessage"))

        findByNameProcessing = true
    }

    @FXML
    fun executeScript() {
        findByNameProcessing = false

        printToConsole("execute_script", true)

        val fileChooser = FileChooser()
        fileChooser.title = bundle.getString("commands.fileChooserTitle")
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


    override fun receiveUpdates() {
        val newUserRoutesIds = getCurrentUserRoutesIds()

        newUserRoutesIds.forEach { id ->
            if (!currentUserRoutesIds.contains(id))
                printToConsole("New route ID #${id} successfully added", false)
        }

        currentUserRoutesIds = newUserRoutesIds
    }
}