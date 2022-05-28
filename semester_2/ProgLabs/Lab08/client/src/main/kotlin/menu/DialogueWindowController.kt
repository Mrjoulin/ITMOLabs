package menu

import client.ClientSession
import entities.Route
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.util.Callback
import network.Request
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

class DialogueWindowController(private val session: ClientSession, private val route: Route) : Initializable {

    @FXML lateinit var titleLabel: Label
    @FXML lateinit var deleteButton: Button
    @FXML lateinit var editButton: Button
    @FXML lateinit var authorField: TextField
    @FXML lateinit var nameField: TextField
    @FXML lateinit var coordinateXField: TextField
    @FXML lateinit var coordinateYField: TextField
    @FXML lateinit var fromNameField: TextField
    @FXML lateinit var fromXField: TextField
    @FXML lateinit var fromYField: TextField
    @FXML lateinit var toNameField: TextField
    @FXML lateinit var toXField: TextField
    @FXML lateinit var toYField: TextField
    @FXML lateinit var distanceField: TextField

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

        }
    }

    @FXML
    fun leftButtonAction(event: ActionEvent) {
        if (informationState) {
            //TODO: DELETE COMMAND
            val response = session.socketWorker.makeRequest(
                Request(token = session.userToken,
                    command = "remove_by_id",
                commandArgs = arrayListOf(route.id.toString())))
            if (response.success) {
                session.entitiesCollection
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
        if (route != null) {
            fillFields()
        }
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

}