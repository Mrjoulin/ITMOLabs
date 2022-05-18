import client.Authorization
import client.Client
import client.ClientSession
import javafx.application.Application
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.TextField
import javafx.scene.shape.Circle
import javafx.stage.Stage
import utils.logger

import java.lang.Exception
import kotlin.system.exitProcess


/**
 * Start application. Entry point
 *
 * @author Matthew I.
 */
class Main {
    private val session = ClientSession()

    fun start() {
        logger.debug("Start console application")

        authorize()

        startProcess()
    }

    private fun authorize() {
        logger.debug("Start authorization")

        val success = Authorization(session).processAuth()

        if (!success) {
            logger.debug("Unsuccessful authorization, close client")
            exitProcess(2)
        }
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


class JFXMLApplication : Application() {
    override fun start(primaryStage: Stage) {
        val root: Parent = FXMLLoader.load(javaClass.classLoader.getResource("login.fxml"))
        val scene = Scene(root, 500.0, 600.0)
        scene.stylesheets.add(javaClass.classLoader.getResource("loginstyle.css")!!.toExternalForm())
        primaryStage.scene = scene

        primaryStage.show()
    }

    fun go() {
        launch()
    }

}

fun main() {
    JFXMLApplication().go()
}