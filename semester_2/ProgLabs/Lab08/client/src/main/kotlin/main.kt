import authorization.Authorization
import authorization.LoginController
import authorization.RegistrationController
import client.Client
import client.ClientSession
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import utils.*

import java.lang.Exception
import kotlin.system.exitProcess


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
            startProcess()
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

        scene.stylesheets.add(javaClass.classLoader.getResource(APPLICATION_AUTH_STYLES)!!.toExternalForm())

        primaryStage.title = "Login to $APPLICATION_NAME"
        primaryStage.scene = scene
        primaryStage.show()

        return false
    }

    private fun startProcess() {
        logger.debug("Start processing commands")

        val client = Client(session)

        println("Hello to the console application!")

        while (true) {
            println("To show available commands type: help")

            try {
                client.processCommands()
            } catch (e: Exception) {
                logger.debug("Error while processing client: {}", e)
            }
        }
    }
}

fun main() {
    Main().run()
}