package menu.dialogs

import client.ClientSession
import entities.Route
import entities.validators.exceptions.IncorrectFieldDataException
import input.CreateEntityMap
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.stage.Stage
import network.Request
import utils.exceptions.UnsuccessfulRequestException
import java.io.InputStreamReader
import java.net.URL
import java.util.*

class AddWindowController(private val session: ClientSession) : Initializable {

    @FXML lateinit var authorField: TextField

    @FXML
    lateinit var titleLabel: Label
    @FXML
    lateinit var addButton: Button
    @FXML
    lateinit var nameField: TextField
    @FXML
    lateinit var coordinateXField: TextField
    @FXML
    lateinit var coordinateYField: TextField
    @FXML
    lateinit var fromNameField: TextField
    @FXML
    lateinit var fromXField: TextField
    @FXML
    lateinit var fromYField: TextField
    @FXML
    lateinit var toNameField: TextField
    @FXML
    lateinit var toXField: TextField
    @FXML
    lateinit var toYField: TextField
    @FXML
    lateinit var distanceField: TextField
    @FXML
    lateinit var errorLabel: Label
    @FXML
    lateinit var choiceBox: ChoiceBox<String>
    @FXML
    lateinit var authorLabel: Label
    @FXML
    lateinit var nameLabel: Label
    @FXML
    lateinit var coordinatesLabel: Label
    @FXML
    lateinit var fromLabel: Label
    @FXML
    lateinit var toLabel: Label
    @FXML
    lateinit var distanceLabel: Label

    private lateinit var bundle: ResourceBundle

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        bundle = session.currentLanguage

        titleLabel.text = bundle.getString("addWindow.titleLabel")
        authorLabel.text = bundle.getString("dialogueWindow.authorLabelMessage") + ":"
        nameLabel.text = bundle.getString("dialogueWindow.nameLabelMessage") + ":"
        coordinatesLabel.text = bundle.getString("dialogueWindow.coordinatesLabelMessage") + ":"
        fromLabel.text = bundle.getString("dialogueWindow.fromLabelMessage") + ":"
        toLabel.text = bundle.getString("dialogueWindow.toLabelMessage") + ":"
        distanceLabel.text = bundle.getString("dialogueWindow.distanceLabelMessage") + ":"
        addButton.text = bundle.getString("addWindow.addButtonMessage")

        authorField.text = session.username
        choiceBox.items.addAll(
            bundle.getString("addWindow.withoutComparing"),
            bundle.getString("addWindow.ifMax"),
            bundle.getString("addWindow.ifMin")
        )
        choiceBox.value = bundle.getString("addWindow.withoutComparing")
    }

    fun addRoute() {

        val data = getNewFields()

        val inp = InputStreamReader(data.byteInputStream())

        try {
            val updatedEntityMap = CreateEntityMap(inp).getObjectMapFromInput(Route::class.java)

            val commandType = when(choiceBox.value) {
                bundle.getString("addWindow.ifMax") -> "add_if_max"
                bundle.getString("addWindow.ifMin") -> "add_if_min"
                else -> "add"
            }

            val request = Request(
                token = session.userToken,
                command = commandType,
                entityObjectMap = updatedEntityMap
            )

            session.socketWorker.makeAsyncRequest(request, { showErrorMessage(it.message) }) {
                if (it.success) {
                    val stage: Stage = addButton.scene.window as Stage
                    stage.close()
                } else {
                    showErrorMessage("can't update. Server is not ok now.")
                }
            }
        } catch (e: IncorrectFieldDataException) {
            showErrorMessage(e.message)
        }
    }

    private fun getNewFields() : String {
        return "\"${nameField.text}\"\n" +
                "${coordinateXField.text}\n" +
                "${coordinateYField.text}\n" +
                "\"${fromNameField.text}\"\n" +
                "${fromXField.text}\n" +
                "${fromYField.text}\n" +
                "\"${toNameField.text}\"\n" +
                "${toXField.text}\n" +
                "${toYField.text}\n" +
                distanceField.text
    }

    private fun showErrorMessage(message: String?) {
        errorLabel.text = message
    }
}