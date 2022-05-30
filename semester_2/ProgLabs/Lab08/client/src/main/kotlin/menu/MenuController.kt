package menu

import authorization.LoginController
import client.ClientSession
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Hyperlink
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import menu.commands.CommandsController
import menu.profile.ProfileController
import menu.table.TableViewController
import menu.visualization.VisualizationController
import utils.*
import java.io.IOException
import java.net.URL
import java.util.*

class MenuController(private val session: ClientSession) : Initializable {
    @FXML
    lateinit var mainpane: AnchorPane

    @FXML
    lateinit var profileButton: Hyperlink

    @FXML
    lateinit var tablebutton: Button

    @FXML
    lateinit var visualizationbutton: Button

    @FXML
    lateinit var commandsbutton: Button

    private var currentUI = APPLICATION_TABLE_SECTION

    private lateinit var bundle: ResourceBundle

    @FXML
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        bundle = session.currentLanguage
        tablebutton.text = bundle.getString("tablebuttonMessage")
        visualizationbutton.text = bundle.getString("visualizationbuttonMessage")
        commandsbutton.text = bundle.getString("commandsbuttonMessage")
        profileButton.text = "@" + session.username
        // Load table by default
        table()
    }

    @FXML
    fun table() {
        currentUI = APPLICATION_TABLE_SECTION
        loadUI()
    }

    @FXML
    fun visualization() {
        currentUI = APPLICATION_VISUALIZATION_SECTION
        loadUI()
    }

    @FXML
    fun commands() {
        currentUI = APPLICATION_COMMANDS_SECTION
        loadUI()
    }

    private fun loadUI() {
        try {
            val loader = FXMLLoader(javaClass.getResource(currentUI))

            val controller = getController()
            if (controller != null) loader.setControllerFactory { controller }

            val root: Parent = loader.load()

            if (mainpane.children.isNotEmpty())
                mainpane.children[0] = root
            else
                mainpane.children.add(root)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getController() : Any? {
        return when(currentUI) {
            APPLICATION_TABLE_SECTION -> TableViewController(session)
            APPLICATION_VISUALIZATION_SECTION -> VisualizationController(session)
            APPLICATION_COMMANDS_SECTION -> CommandsController(session)
            else -> null
        }
    }

    @FXML
    fun showProfile() {
        val loader = FXMLLoader(javaClass.getResource(APPLICATION_PROFILE_WINDOW))
        loader.setControllerFactory { ProfileController(session) }
        val profileScene = Scene(loader.load(), PROFILE_WINDOW_WIDTH, PROFILE_WINDOW_HEIGHT)

        val profileStage = Stage()

        profileStage.setOnHidden {
            if (session.userToken.isEmpty()) goToLogin()
            else if (session.languageChanged) changeLanguage()
        }

        profileStage.scene = profileScene
        profileStage.show()
    }

    private fun goToLogin() {
        logger.debug("Move to login window")

        val loader = FXMLLoader(javaClass.classLoader.getResource(APPLICATION_LOGIN_WINDOW))
        loader.setControllerFactory { LoginController(session) }

        val scene = Scene(loader.load(), AUTHORIZATION_WINDOW_WIDTH, AUTHORIZATION_WINDOW_HEIGHT)

        val currentStage: Stage = mainpane.scene.window as Stage

        currentStage.title = "Login to $APPLICATION_NAME"
        currentStage.scene = scene

        currentStage.sizeToScene()
        currentStage.centerOnScreen()
    }

    private fun changeLanguage() {
        // TODO change buttons text
        loadUI()
    }
}