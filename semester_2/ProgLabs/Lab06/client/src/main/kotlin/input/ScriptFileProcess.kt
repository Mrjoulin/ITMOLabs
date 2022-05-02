package input

import Client
import utils.ExecuteScriptPath
import utils.logger

import java.io.File
import java.io.FileReader
import java.lang.Exception


fun processScriptFile(filename: String) : Boolean {
    if (!File(filename).exists()) {
        println("Input file $filename for executing script not found!")
        return false
    } else if (!ExecuteScriptPath.addToExecuteScriptPath(filename)) {
        println("Recursive execute script call! File $filename already has been called!")
        return false
    }

    // Set new input stream reader from given file
    val newInput = FileReader(filename)

    // Create new client to process commands
    val fileClient = Client(newInput)

    val success = try {
        fileClient.processCommands()
    } catch (e: Exception) {
        logger.error(e.stackTraceToString())
        println("Error while executing script $filename: ${e.message}")
        false
    }

    // Remove file reader buffer from HashMap to optimize memory
    removeBufferByReader(newInput)

    // Remove current file from execute script path
    if (!ExecuteScriptPath.removeFromExecuteScriptPath(filename))
        logger.error("Can't remove file $filename from execute script path!")

    if (!success) println("Execution of script $filename failed!")
    else println("Script $filename successfully executed!")

    return success
}