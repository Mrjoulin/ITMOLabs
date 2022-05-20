package authorization

import client.ClientSession
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Hyperlink
import utils.APPLICATION_NAME
import utils.APPLICATION_SIGNUP_WINDOW
import java.net.URL
import java.util.*

class LoginController(private val session: ClientSession) : Authorization(session), Initializable {
    @FXML
    private lateinit var startRegisteringButton: Hyperlink

    @FXML
    override fun initialize(location: URL?, resources: ResourceBundle?) {
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
