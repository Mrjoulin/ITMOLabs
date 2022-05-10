package client

import input.getInput
import network.*
import utils.logger
import java.io.FileReader
import java.io.IOException
import java.lang.NumberFormatException
import java.security.MessageDigest

class Authorization (private val session: ClientSession) {
    private val authTypes: List<String> = AUTHORIZATION_COMMANDS.map { it.replace("_", " ") }

    fun processAuth() : Boolean {
        val isTokenExistAndCorrect = checkAuthToken()

        if (isTokenExistAndCorrect) return true

        while (true) {
            try {
                // Get data from user
                val authType = getAuthType() ?: return false

                val success = authorizeByType(authType) ?: return false

                if (success) return true
            } catch (e: Exception) {
                println("Some error has occurred, try again later")
                logger.debug("Error while processing authorization: $e")

                return false
            }
        }
    }

    fun authorizeByType(authType: String) : Boolean? {
        val username = getUsername(authType) ?: return null
        val password = getPassword(authType) ?: return null

        val userCredentials = arrayListOf(username, password)

        // Make request to server
        val request = Request(
            command = authType.replace(" ", "_"),
            commandArgs = userCredentials
        )
        val response = session.socketWorker.makeRequest(request) ?: return false

        // Process response
        val success = processAuthResponse(response)

        if (success) {
            println("Successful $authType to user \"$username\"")
            session.username = username

            return true
        }

        return false
    }

    private fun checkAuthToken() : Boolean {
        if (session.userToken.isEmpty()) return false

        // If token exist (may be from environment)
        val request = Request(
            token = session.userToken,
            command = "check_token" // Default command that need to be exist
        )

        val response = session.socketWorker.makeRequest(request) ?: return false

        if (response.success)
            session.username = response.message
                .split("\n")[0]
                .replaceBefore("user ", "")
                .replaceFirst("user ", "")
                .replace(":", "")


        return response.success
    }

    private fun getAuthType() : String? {
        println("Please, choose type of authorization:")
        authTypes.indices.forEach {
            println("   ${it + 1} - to ${authTypes[it]}")
        }

        while (true) {
            val authType = try {
                getInput(session.currentInput, ": ") ?: continue
            } catch (e: IOException) {
                if (session.currentInput != FileReader::javaClass) continue
                return null
            }

            val parsedAuthType = try { Integer.parseInt(authType[0]) }
                                 catch (e: NumberFormatException) { null }

            logger.debug("User auth type choose is: $parsedAuthType, not parsed: ${authType[0]}")

            if (parsedAuthType == null || (parsedAuthType - 1) !in authTypes.indices)
                println("Authorization type is incorrect! Please, choose and type again")
            else return authTypes[parsedAuthType - 1]
        }
    }


    private fun getUsername(authType: String) : String? {
        val prefixText = "Input username to $authType: "

        while (true) {
            val login = try {
                getInput(session.currentInput, prefixText, split = false)?.get(0) ?: continue
            } catch (e: IOException) {
                if (session.currentInput != FileReader::javaClass) continue
                return null
            }

            if (login.length < MIN_USERNAME_LENGTH)
                println("Login is too short, min length $MIN_USERNAME_LENGTH symbols! Type again")
            else if (login.length > MAX_USERNAME_LENGTH)
                println("Login is too long, max length $MAX_USERNAME_LENGTH symbols! Type again")
            else if (!CORRECT_LOGIN_RE.matcher(login).matches())
                println("Login in incorrect format! Use only English letters and digits (also available _.)")
            else
                return login
        }
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

            if (password!!.length < MIN_PASSWORD_LENGTH)
                println("Password is too short, min length $MIN_PASSWORD_LENGTH symbols! Type again")
            else if (password.length > MAX_PASSWORD_LENGTH)
                println("Password is too long, max length $MAX_PASSWORD_LENGTH symbols! Type again")
            else if (!CORRECT_PASSWORD_RE.matcher(password).matches())
                println("Password in incorrect format! Try again")
            else
                return hashStringToSHA1(password)
        }
    }

    private fun processAuthResponse(response: Response) : Boolean {
        if (!response.success) {
            println(response.message.ifEmpty { "Unsuccessful request to the server!" })

            return false
        }

        session.userToken = response.message

        return true
    }

    private fun hashStringToSHA1(str: String): String {
        return MessageDigest
            .getInstance("SHA-1")
            .digest(str.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
}