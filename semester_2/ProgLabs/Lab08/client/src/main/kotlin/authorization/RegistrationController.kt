package authorization

import client.ClientSession
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Hyperlink
import utils.APPLICATION_LOGIN_WINDOW
import utils.APPLICATION_NAME
import java.net.URL
import java.util.*

class RegistrationController(private val session: ClientSession) : Authorization(session), Initializable {
    @FXML
    private lateinit var startLoginButton: Hyperlink

    @FXML
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        startLoginButton.setOnMouseClicked {
            moveToWindow(
                windowTitle = "Login to $APPLICATION_NAME",
                windowFXMLFilename = APPLICATION_LOGIN_WINDOW,
                controller = LoginController(session = session)
            )
        }
        loginButton.setOnAction { processAuth(authType = "sign up") }
    }
}