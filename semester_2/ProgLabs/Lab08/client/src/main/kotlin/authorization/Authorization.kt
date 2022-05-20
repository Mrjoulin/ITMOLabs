package authorization

import client.ClientSession
import utils.exceptions.UnsuccessfulRequestException
import input.getInput
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.stage.Stage
import network.*
import utils.APPLICATION_AUTH_STYLES
import utils.APPLICATION_NAME
import utils.APPLICATION_SIGNUP_WINDOW
import utils.exceptions.IncorrectFieldDataException
import utils.logger
import java.io.FileReader
import java.io.IOException
import java.lang.NumberFormatException
import java.security.MessageDigest
import kotlin.jvm.Throws

open class Authorization (private val session: ClientSession) {
    @FXML
    lateinit var loginTextField: TextField
    @FXML
    lateinit var passwordTextField: PasswordField
    @FXML
    lateinit var loginButton: Button
    @FXML
    lateinit var errorMessage: Label

    fun moveToWindow(windowTitle: String, windowFXMLFilename: String, controller: Any) {
        val loader = FXMLLoader(javaClass.classLoader.getResource(windowFXMLFilename))
        loader.setControllerFactory { controller }

        val currentStage: Stage = loginButton.scene.window as Stage

        val scene = Scene(loader.load())
        scene.stylesheets.add(javaClass.classLoader.getResource(APPLICATION_AUTH_STYLES)!!.toExternalForm())

        currentStage.title = windowTitle
        currentStage.scene = scene

        currentStage.sizeToScene()
    }

    fun processAuth(authType: String) {
        try {
            authorizeByType(
                authType = authType,
                username = loginTextField.text,
                password = passwordTextField.text
            )

            // TODO move to menu
        }
        catch (e: UnsuccessfulRequestException) { errorMessage.text = e.message }
        catch (e: IncorrectFieldDataException) { errorMessage.text = "Неверный ввод: ${e.message}" }
    }

    @Throws(UnsuccessfulRequestException::class, IOException::class)
    fun authorizeByType(authType: String, username: String? = null, password: String? = null) {
        val correctUsername: String = username ?: (getUsername(authType) ?: throw IOException())
        val correctPassword: String = password ?: (getPassword(authType) ?: throw IOException())

        checkUsername(correctUsername)
        checkPassword(correctPassword)

        val userCredentials = arrayListOf(correctUsername, hashStringToSHA1(correctPassword))

        // Make request to server
        val request = Request(
            command = authType.replace(" ", "_"),
            commandArgs = userCredentials
        )

        val response: Response = session.socketWorker.makeRequest(request)

        // Process response
        processAuthResponse(response)

        session.username = correctUsername
    }

    fun checkAuthToken() : Boolean {
        if (session.userToken.isEmpty()) return false

        // If token exist (may be from environment)
        val request = Request(
            token = session.userToken,
            command = "check_token" // Default command that need to be exist
        )

        return try {
            val response = session.socketWorker.makeRequest(request)

            if (response.success)
                session.username = response.message
                    .split("\n")[0]
                    .replaceBefore("user ", "")
                    .replaceFirst("user ", "")
                    .replace(":", "")


            response.success
        } catch (e: UnsuccessfulRequestException) {
            false
        }
    }

    private fun getUsername(authType: String) : String? {
        val prefixText = "Input username to $authType: "

        while (true) {
            val username = try {
                getInput(session.currentInput, prefixText, split = false)?.get(0) ?: continue
            } catch (e: IOException) {
                if (session.currentInput != FileReader::javaClass) continue
                return null
            }

            try {
                checkUsername(username)

                return username
            } catch (e: IncorrectFieldDataException) {
                println(e.message)
            }
        }
    }

    private fun checkUsername(username: String) {
        if (username.length < MIN_USERNAME_LENGTH)
            throw IncorrectFieldDataException("Login is too short, min length $MIN_USERNAME_LENGTH symbols! Type again")
        if (username.length > MAX_USERNAME_LENGTH)
            throw IncorrectFieldDataException("Login is too long, max length $MAX_USERNAME_LENGTH symbols! Type again")
        if (!CORRECT_LOGIN_RE.matcher(username).matches())
            throw IncorrectFieldDataException("Login in incorrect format! Use only English letters and digits (also available _.)")
    }

    private fun getPassword(authType: String) : String? {
        val prefixText = "Input password to $authType: "

        while (true) {
            var password: String? = null

            if (session.currentInput.javaClass != FileReader::class.java)
                password = System.console()?.readPassword(prefixText)?.joinToString("")

            if (password == null) {
                password = try {
                    getInput(session.currentInput, prefixText, split = false)?.get(0) ?: continue
                } catch (e: IOException) {
                    if (session.currentInput != FileReader::javaClass) continue
                    return null
                }
            }

            try {
                checkUsername(password!!)

                return password
            } catch (e: IncorrectFieldDataException) {
                println(e.message)
            }
        }
    }

    private fun checkPassword(password: String) {
        if (password.length < MIN_PASSWORD_LENGTH)
            throw IncorrectFieldDataException("Password is too short, min length $MIN_PASSWORD_LENGTH symbols! Type again")
        if (password.length > MAX_PASSWORD_LENGTH)
            throw IncorrectFieldDataException("Password is too long, max length $MAX_PASSWORD_LENGTH symbols! Type again")
        if (!CORRECT_PASSWORD_RE.matcher(password).matches())
            throw IncorrectFieldDataException("Password in incorrect format! Try again")
    }


    @Throws(UnsuccessfulRequestException::class)
    private fun processAuthResponse(response: Response) {
        if (!response.success)
            throw UnsuccessfulRequestException(
                response.message.ifEmpty { "Unsuccessful request to the server!" }
            )

        session.userToken = response.message
    }

    private fun hashStringToSHA1(str: String): String {
        return MessageDigest
            .getInstance("SHA-1")
            .digest(str.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
}