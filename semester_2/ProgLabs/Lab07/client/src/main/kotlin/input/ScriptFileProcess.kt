package input

import client.Client
import client.ClientSession
import utils.ExecuteScriptPath
import utils.logger

import java.io.File
import java.io.FileReader
import java.lang.Exception


fun processScriptFile(session: ClientSession, filename: String) : Boolean {
    if (!File(filename).exists()) {
        println("Input file $filename for executing script not found!")
        return false
    } else if (!ExecuteScriptPath.addToExecuteScriptPath(filename)) {
        println("Recursive execute script call! File $filename already has been called!")
        return false
    }

    // Set new input stream reader from given file
    val previousInput = session.currentInput

    session.currentInput = FileReader(filename)

    // Create new client to process commands
    val fileClient = Client(session)

    val success = try {
        fileClient.processCommands()
    } catch (e: Exception) {
        logger.error(e.stackTraceToString())
        println("Error while executing script $filename: ${e.message}")
        false
    }

    // Remove file reader buffer from HashMap to optimize memory
    removeBufferByReader(session.currentInput)

    // Change input back
    session.currentInput = previousInput

    // Remove current file from execute script path
    if (!ExecuteScriptPath.removeFromExecuteScriptPath(filename))
        logger.error("Can't remove file $filename from execute script path!")

    if (!success) println("Execution of script $filename failed!")
    else println("Script $filename successfully executed!")

    return success
}