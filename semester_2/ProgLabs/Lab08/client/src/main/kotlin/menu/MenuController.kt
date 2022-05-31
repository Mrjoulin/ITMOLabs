package menu

import authorization.LoginController
import client.ClientSession
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Hyperlink
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import menu.sections.commands.CommandsController
import menu.profile.ProfileController
import menu.sections.interfaces.UpdatableController
import menu.sections.table.TableViewController
import menu.sections.visualization.VisualizationController
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

    private lateinit var currentUI: String
    private var currentController: UpdatableController? = null
    private lateinit var updatesThread: Thread

    private var bundle: ResourceBundle = session.currentLanguage

    @FXML
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        tablebutton.text = bundle.getString("tablebuttonMessage")
        visualizationbutton.text = bundle.getString("visualizationbuttonMessage")
        commandsbutton.text = bundle.getString("commandsbuttonMessage")

        profileButton.text = "@" + session.username
        // Load table by default
        table()

        session.socketWorker.startUpdatesListener()

        // Start processing of collection updates
        updatesThread = Thread(this::processCollectionUpdates)
        updatesThread.isDaemon = true
        updatesThread.start()
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

            setController()

            if (currentController != null) loader.setControllerFactory { currentController }

            val root: Parent = loader.load()

            if (mainpane.children.isNotEmpty())
                mainpane.children[0] = root
            else
                mainpane.children.add(root)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun setController(){
        currentController = when(currentUI) {
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
        // Stop updates processing thread
        session.socketWorker.stopUpdatesListenerProcess()
        updatesThread.interrupt()

        // Load login window
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
        bundle = session.currentLanguage

        tablebutton.text = bundle.getString("tablebuttonMessage")
        println(bundle.getString("tablebuttonMessage"))
        visualizationbutton.text = bundle.getString("visualizationbuttonMessage")
        commandsbutton.text = bundle.getString("commandsbuttonMessage")

        loadUI()
    }

    private fun processCollectionUpdates() {
        logger.debug("Start waiting for new collection updates")

        while (true) {
            while (!session.collectionManager.isCollectionChanged()) {
                Thread.sleep(10) // Wait for updates
            }
            // When update received
            logger.debug("Invoke receive updates methods of controller: ${currentController?.javaClass?.simpleName}")
            currentController?.receiveUpdates()

            session.collectionManager.collectionChangedProcessed()
        }
    }
}