package menu.dialogs

import client.ClientSession
import entities.Route
import entities.validators.exceptions.IncorrectFieldDataException
import input.CreateEntityMap
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.stage.Stage
import network.Request
import utils.exceptions.UnsuccessfulRequestException
import java.io.InputStreamReader
import java.net.URL
import java.util.*

class DialogueWindowController(private val session: ClientSession, private val route: Route) : Initializable {

    @FXML
    lateinit var titleLabel: Label
    @FXML
    lateinit var deleteButton: Button
    @FXML
    lateinit var editButton: Button
    @FXML
    lateinit var authorField: TextField
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

    private var informationState = true

    private lateinit var bundle: ResourceBundle

    @FXML
    fun rightButtonAction(event: ActionEvent) {
        if (informationState) {
            editButton.text = bundle.getString("dialogueWindow.saveButtonMessage")
            deleteButton.text = bundle.getString("dialogueWindow.cancelButtonMessage")
            switchEditableMode()

            informationState = false
            return
        }

        // Update route

        val data = getNewFields()
        val inp = InputStreamReader(data.byteInputStream())

        try {
            val updatedEntityMap = CreateEntityMap(inp).getObjectMapFromInput(Route::class.java)

            val response = session.socketWorker.makeRequest(
                Request(
                    token = session.userToken,
                    command = "update",
                    commandArgs = arrayListOf(route.id.toString()),
                    entityObjectMap = updatedEntityMap
                )
            )
            if (response.success) {
                //TODO: если ок все что делается
                val stage: Stage = deleteButton.scene.window as Stage
                stage.close()
            } else {
                showErrorMessage(response.message)
            }

        } catch (e: IncorrectFieldDataException) {
            fillFields()
            showErrorMessage(e.message)
        } catch (e: UnsuccessfulRequestException) {
            showErrorMessage(e.message)
        }
    }

    @FXML
    fun leftButtonAction(event: ActionEvent) {
        if (!informationState) {
            editButton.text = bundle.getString("dialogueWindow.editButtonMessage")
            deleteButton.text = bundle.getString("dialogueWindow.deleteButtonMessage")
            fillFields()
            switchEditableMode()
            informationState = true
            return
        }

        // Remove route

        try {
            val response = session.socketWorker.makeRequest(
                Request(
                    token = session.userToken,
                    command = "remove_by_id",
                    commandArgs = arrayListOf(route.id.toString())
                )
            )

            if (response.success) {
                //TODO: NOT SURE ABOUT UPDATING
                session.entitiesCollection.remove(route)

                val stage: Stage = deleteButton.scene.window as Stage
                stage.close()
            } else {
                showErrorMessage(response.message)
            }
        } catch (e: UnsuccessfulRequestException) {
            showErrorMessage(e.message)
        }
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        bundle = session.currentLanguage
        titleLabel.text = bundle.getString("dialogueWindow.HeaderMessage")
        authorLabel.text = bundle.getString("dialogueWindow.authorLabelMessage") + ":"
        nameLabel.text = bundle.getString("dialogueWindow.nameLabelMessage") + ":"
        coordinatesLabel.text = bundle.getString("dialogueWindow.coordinatesLabelMessage") + ":"
        fromLabel.text = bundle.getString("dialogueWindow.fromLabelMessage") + ":"
        toLabel.text = bundle.getString("dialogueWindow.toLabelMessage") + ":"
        distanceLabel.text = bundle.getString("dialogueWindow.distanceLabelMessage") + ":"
        deleteButton.text = bundle.getString("dialogueWindow.deleteButtonMessage")
        editButton.text = bundle.getString("dialogueWindow.editButtonMessage")
        fillFields()

        if (session.username != route.author) disableButtons()
    }

    private fun switchEditableMode() {
        val textFields = arrayListOf(
            nameField, coordinateXField, coordinateYField,
            fromNameField, fromXField, fromYField,
            toNameField, toXField, toYField,
            distanceField
        )

        for (field in textFields) {
            field.isEditable = informationState
        }
    }

    private fun disableButtons() {
        editButton.style = "-fx-background-color: gray;"
        deleteButton.style = "-fx-background-color: grey;"
        deleteButton.isDisable = true
        editButton.isDisable = true
    }

    private fun fillFields() {
        authorField.text = route.author
        nameField.text = route.name
        coordinateXField.text = route.coordinates.x.toString()
        coordinateYField.text = route.coordinates.y.toString()
        fromNameField.text = route.from.name
        fromXField.text = route.from.x.toString()
        fromYField.text = route.from.y.toString()
        toNameField.text = route.to.name
        toXField.text = route.to.x.toString()
        toYField.text = route.to.y.toString()
        distanceField.text = route.distance.toString()
    }

    private fun getNewFields() : String {
        return "${nameField.text}\n" +
                "${coordinateXField.text}\n" +
                "${coordinateYField.text}\n" +
                "${fromNameField.text}\n" +
                "${fromXField.text}\n" +
                "${fromYField.text}\n" +
                "${toNameField.text}\n" +
                "${toXField.text}\n" +
                "${toYField.text}\n" +
                distanceField.text
    }

    private fun showErrorMessage(message: String?) {
        errorLabel.text = message
    }
}