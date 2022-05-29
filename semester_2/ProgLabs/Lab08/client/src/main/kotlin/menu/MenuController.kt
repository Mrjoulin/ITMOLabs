package menu

import client.ClientSession
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.Scene
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
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        profileButton.text = "@" + session.username
        loadUI(APPLICATION_TABLE_SECTION, TableViewController(session))
    }

    @FXML
    fun table(event: ActionEvent) {
        loadUI(APPLICATION_TABLE_SECTION, TableViewController(session))
    }

    @FXML
    fun visualization(event: ActionEvent) {
        loadUI(APPLICATION_VISUALIZATION_SECTION, VisualizationController(session))
    }

    @FXML
    fun commands(event: ActionEvent) {
        loadUI(APPLICATION_COMMANDS_SECTION, CommandsController(session))
    }

    private fun loadUI(ui: String, controller: Any) {
        try {
            val loader = FXMLLoader(javaClass.getResource(ui))
            loader.setControllerFactory { controller }

            val root: Parent = loader.load()

            if (mainpane.children.isNotEmpty())
                mainpane.children[0] = root
            else
                mainpane.children.add(root)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @FXML
    fun showProfile() {
        val loader = FXMLLoader(javaClass.getResource(APPLICATION_PROFILE_WINDOW))
        loader.setControllerFactory { ProfileController(session) }
        val profileScene = Scene(loader.load(), PROFILE_WINDOW_WIDTH, PROFILE_WINDOW_HEIGHT)
        val profileStage = Stage()
        profileStage.scene = profileScene
        profileStage.show()

    }
}