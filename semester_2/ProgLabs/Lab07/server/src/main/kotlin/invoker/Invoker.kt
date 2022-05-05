package invoker

import commands.interfaces.Command
import invoker.interfaces.InvokerInterface
import network.Request
import network.Response
import utils.logger


/**
 * Class Invoker which stores a map of commands by name and calls the desired command.
 * Implements InvokerInterface
 *
 * @see InvokerInterface
 * @see Command
 * @see Request
 * @see Response
 *
 * @author Matthew I.
 */
class Invoker : InvokerInterface {
    private val commandMap: HashMap<String, Command> = HashMap()

    override fun register(commandName: String, command: Command) {
        logger.info("Register new command: $commandName")

        commandMap[commandName] = command
    }

    override fun execute(request: Request): Response {
        val commandName = request.command

        val command = getCommand(commandName)

        logger.info("Execute command: $commandName")

        return command.execute(request)
    }

    private fun getCommand(commandName: String): Command {
        logger.info("Get command by name: $commandName")

        val command = commandMap[commandName]

        return command ?: throw IllegalStateException("Command $commandName not found!")
    }
}