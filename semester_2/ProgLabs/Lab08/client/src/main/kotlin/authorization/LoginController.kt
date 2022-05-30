package authorization

import client.ClientSession
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Hyperlink
import javafx.scene.control.Label
import utils.APPLICATION_NAME
import utils.APPLICATION_SIGNUP_WINDOW
import java.net.URL
import java.nio.BufferUnderflowException
import java.util.*

class LoginController(private val session: ClientSession) : Authorization(session), Initializable {
    @FXML
    private lateinit var startRegisteringButton: Hyperlink

    private lateinit var bundle: ResourceBundle

    @FXML
    private lateinit var authorizationLabel: Label


    @FXML
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        bundle = session.currentLanguage
        authorizationLabel.text = bundle.getString("authorizationLabelMessage")
        startRegisteringButton.text = bundle.getString("startRegisteringButtonMessage")
        loginButton.text = bundle.getString("loginButtonMessage")
        loginTextField.promptText = bundle.getString("loginTextFieldMessage")
        passwordTextField.promptText = bundle.getString("passwordTextFieldMessage")
        startRegisteringButton.setOnMouseClicked {
            moveToWindow(
                windowTitle = "Registration on $APPLICATION_NAME",
                windowFXMLFilename = APPLICATION_SIGNUP_WINDOW,
                controller = RegistrationController(session = session)
            )
        }
        loginButton.setOnAction { processAuth(authType = "login") }
    }
}
