package invoker

import commands.interfaces.Command
import invoker.interfaces.InvokerInterface
import utils.logger


/**
 * Class Invoker which stores a map of commands by name and calls the desired command.
 * Implements InvokerInterface
 *
 * @author Matthew I.
 */
class Invoker : InvokerInterface {
    private val commandMap: HashMap<String, Command> = HashMap()

    override fun register(commandName: String, command: Command) {
        logger.debug("Register new command: $commandName")

        commandMap[commandName] = command
    }

    override fun execute(commandName: String, args: ArrayList<String>): Boolean {
        val command = getCommand(commandName)

        logger.debug("Execute command: $commandName")

        return command.execute(args)
    }

    private fun getCommand(commandName: String): Command {
        logger.debug("Get command by name: $commandName")

        return commandMap[commandName] ?:
            throw IllegalStateException("Command $commandName not found!")
    }
}