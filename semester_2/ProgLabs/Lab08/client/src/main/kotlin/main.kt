import menu.MenuController
import authorization.LoginController
import client.Client
import client.ClientSession
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import utils.*


/**
 * Start application. Entry point
 *
 * @author Matthew I.
*/
class Main : Application() {
    private val session = ClientSession()

    fun run() = launch()

    override fun start(primaryStage: Stage) {
        logger.debug("Start console application")

        val success = authorize(primaryStage)

        if (success) {
            startProcess(primaryStage)
        }
    }

    private fun authorize(primaryStage: Stage) : Boolean {
        logger.debug("Start authorization")

        val controller = LoginController(session)

        // If token given from evn and correct
        if (controller.checkAuthToken()) return true

        val loader = FXMLLoader(javaClass.classLoader.getResource(APPLICATION_LOGIN_WINDOW))
        loader.setControllerFactory { controller }

        val scene = Scene(loader.load(), AUTHORIZATION_WINDOW_WIDTH, AUTHORIZATION_WINDOW_HEIGHT)

        primaryStage.title = "Login to $APPLICATION_NAME"
        primaryStage.scene = scene
        primaryStage.show()

        return false
    }

    private fun startProcess(primaryStage: Stage) {
        logger.debug("Start processing menu")

        val loader = FXMLLoader(javaClass.classLoader.getResource(APPLICATION_MENU_WINDOW))
        loader.setControllerFactory { MenuController(session) }

        val scene = Scene(loader.load(), MENU_WINDOW_WIDTH, MENU_WINDOW_HEIGHT)

        primaryStage.title = APPLICATION_NAME
        primaryStage.scene = scene
        primaryStage.show()

    }
}

fun main() {
    Main().run()
}