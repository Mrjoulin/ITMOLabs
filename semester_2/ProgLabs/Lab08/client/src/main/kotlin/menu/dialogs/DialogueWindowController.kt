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

    private var informationState = true

    @FXML
    fun rightButtonAction(event: ActionEvent) {
        if (informationState) {
            editButton.text = "Сохранить"
            deleteButton.text = "Отмена"
            switchEditableMode()
            informationState = false
        } else {
            //TODO: SEND UPDATE COMMAND
            val data = getNewFields()

            val inp = InputStreamReader(data.byteInputStream())
            try {
                val updatedEntityMap = CreateEntityMap(inp).getObjectMapFromInput(Route::class.java)

                val response = session.socketWorker.makeRequest(
                    Request(
                        token = session.userToken,
                        command = "update",
                        commandArgs = arrayListOf(route.id.toString()),
                        entityObjectMap = updatedEntityMap)
                    )
                if (response.success) {
                    //TODO: если ок все что делается
                    val stage: Stage = deleteButton.scene.window as Stage
                    stage.close()
                } else {
                    showErrorMessage("can't update. Server is not ok now.")
                }

            } catch (e: IncorrectFieldDataException) {
                fillFields()
                showErrorMessage(e.message)
            }


        }
    }

    @FXML
    fun leftButtonAction(event: ActionEvent) {
        if (informationState) {
            //TODO: DELETE COMMAND
            val response = session.socketWorker.makeRequest(
                Request(
                    token = session.userToken,
                    command = "remove_by_id",
                    commandArgs = arrayListOf(route.id.toString())
                )
            )

            if (response.success) {
                session.entitiesCollection.remove(route)
                val stage: Stage = deleteButton.scene.window as Stage
                //TODO: NOT SURE ABOUT UPDATING
                stage.close()
            } else {
                showErrorMessage("can't delete. Server is not ok now!")
            }

        } else {
            editButton.text = "Редактировать"
            deleteButton.text = "Удалить"
            fillFields()
            switchEditableMode()
            informationState = true
        }
    }


    override fun initialize(location: URL?, resources: ResourceBundle?) {
        fillFields()

        if (session.username != route.author) disableButtons()
    }

    private fun switchEditableMode() {
        val textFields = arrayListOf<TextField>(
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