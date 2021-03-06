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
    @FXML
    private lateinit var profileLabel: Label
    @FXML
    private lateinit var languageLabel: Label
    @FXML
    private lateinit var changePasswordLabel: Label
    @FXML
    private lateinit var saveLanguageButton: Button
    @FXML
    private lateinit var savePasswordButton: Button
    @FXML
    private lateinit var exitButton: Button

    private lateinit var bundle: ResourceBundle

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        bundle = session.currentLanguage
        profileLabel.text = bundle.getString("profile.profileLabel")
        languageLabel.text = bundle.getString("profile.languageLabel")
        changePasswordLabel.text = bundle.getString("profile.changePasswordLabel")
        saveLanguageButton.text = bundle.getString("profile.saveLanguageButton")
        savePasswordButton.text = bundle.getString("profile.savePasswordButton")
        exitButton.text = bundle.getString("profile.exitButton")
        changePassword.promptText = bundle.getString("profile.changePassword")

        languageBox.items.addAll(APPLICATION_LANGUAGES_CODES_BY_NAMES.keys)

        val curLanguageCode = session.currentLanguage.locale.language

        logger.debug("Current language code: $curLanguageCode")

        languageBox.value = APPLICATION_LANGUAGES_NAMES_BY_CODES[curLanguageCode]
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

        val request = Request(
            token = session.userToken,
            command = "change_password",
            commandArgs = arrayListOf(auth.hashStringToSHA1(password))
        )

        session.socketWorker.makeAsyncRequest(request, { printErrorMessage(it.message) }) {
            if (it.success) {
                // Update token
                session.userToken = it.message

                messageLabel.textFill = Color.GREEN
                messageLabel.text = SUCCESSFUL_PASSWORD_CHANGED_MESSAGE
            } else {
                printErrorMessage(it.message)
            }
        }
    }

    @FXML
    fun saveLanguage(event: ActionEvent) {
        val newBundle = ResourceBundle.getBundle(
            "${LANGUAGES_BASE_NAME_PREFIX}_${APPLICATION_LANGUAGES_CODES_BY_NAMES[languageBox.value]}"
        )

        if (session.currentLanguage != newBundle) {
            session.languageChanged = true
            session.currentLanguage = newBundle

            val stage = languageBox.scene.window as Stage
            stage.close()
        }
    }

    @FXML
    fun logOut(event: ActionEvent) {
        session.userToken = ""
        session.username = ""
        session.collectionManager.clearCollection()

        val currentStage = messageLabel.scene.window as Stage
        currentStage.close()
    }

    private fun printErrorMessage(message: String?) {
        messageLabel.textFill = Color.RED
        messageLabel.text = message
    }
}