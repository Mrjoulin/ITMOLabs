package menu.profile

import authorization.Authorization
import authorization.LoginController
import client.ClientSession
import entities.validators.exceptions.IncorrectFieldDataException
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.paint.Color
import javafx.stage.Stage
import network.Request
import utils.*
import utils.exceptions.UnsuccessfulRequestException
import java.net.URL
import java.util.*
import kotlin.collections.HashSet

class ProfileController(private val session: ClientSession) : Initializable {
    @FXML
    private lateinit var changePassword: PasswordField
    @FXML
    private lateinit var languageBox: ChoiceBox<String>
    @FXML
    private lateinit var messageLabel: Label

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        languageBox.items.addAll(APPLICATION_LANGUAGES)
        languageBox.value = DEFAULT_APPLICATION_LANGUAGE
    }

    @FXML
    fun changePassword(event: ActionEvent) {
        val password = changePassword.text
        val auth = Authorization(session)

        try {
            auth.checkPassword(password)
        } catch (e: IncorrectFieldDataException) {
            return printErrorMessage(e.message)
        }

        try {
            val response = session.socketWorker.makeRequest(
                Request(
                    token = session.userToken,
                    command = "change_password",
                    commandArgs = arrayListOf(auth.hashStringToSHA1(password))
                )
            )

            if (response.success) {
                // Update token
                session.userToken = response.message

                messageLabel.textFill = Color.GREEN
                messageLabel.text = SUCCESSFUL_PASSWORD_CHANGED_MESSAGE
            } else {
                printErrorMessage(response.message)
            }
        } catch (e: UnsuccessfulRequestException) {
            printErrorMessage(e.message)
        }
    }

    @FXML
    fun saveLanguage(event: ActionEvent) {}

    @FXML
    fun logOut(event: ActionEvent) {
        session.userToken = ""
        session.username = ""
        session.collectionInitialized = false
        session.entitiesCollection = HashSet()

        val currentStage = messageLabel.scene.window as Stage
        currentStage.close()
    }

    private fun printErrorMessage(message: String?) {
        messageLabel.textFill = Color.RED
        messageLabel.text = message
    }
}