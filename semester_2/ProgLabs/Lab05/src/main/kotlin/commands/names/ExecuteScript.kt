package commands.names

import Client
import commands.annotations.ConsoleCommand
import commands.interfaces.Command
import receiver.interfaces.ReceiverInterface
import entities.Route
import utils.ExecuteScriptPath
import utils.logger

import java.io.File
import java.io.FileReader
import java.lang.Exception

/**
 * Class to execute script in given file
 * Extends Command
 *
 * @param receiver Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "execute_script", args = "filename", description = "Execute script in given file")
class ExecuteScript(val receiver: ReceiverInterface<Route>) : Command {
    override fun execute(args: ArrayList<String>): Boolean {
        if (args.isEmpty()) {
            println("Please input script filename to execute")
            return false
        } else if (!File(args[0]).exists()) {
            println("Input file ${args[0]} for executing script not found!")
            return false
        } else if (!ExecuteScriptPath.addToExecuteScriptPath(args[0])) {
            println("Recursive execute script call! File ${args[0]} already has been called!")
            return false
        }

        // Get previous Stream reader to set it back on end of script
        val previousStream = receiver.getInputStreamReader()
        // Set new input stream reader from given file
        receiver.setInputStreamReader(newInputStreamReader = FileReader(args[0]))

        // Create new client to process commands
        val fileClient = Client(receiver)
        fileClient.loadCommands()

        val success = try {
                fileClient.processCommands()
            } catch (e: Exception) {
                logger.error(e.stackTraceToString())
                println("Error while executing script ${args[0]}: ${e.message}")
                false
            }

        // Set back previous input stream reader
        receiver.setInputStreamReader(newInputStreamReader = previousStream)
        // Remove current file from execute script path
        if (!ExecuteScriptPath.removeFromExecuteScriptPath(args[0]))
            logger.error("Can't remove file ${args[0]} from execute script path!")

        if (!success) println("Execution of script ${args[0]} failed!")
        else println("Script ${args[0]} successfully executed!")

        return success
    }
}