package commands.names

import commands.annotations.ConsoleCommand
import commands.interfaces.Command
import receiver.interfaces.ReceiverInterface
import entities.Route
import utils.CommandsLoader

import kotlin.collections.ArrayList
import kotlin.math.max

/**
 * Class to show info about available commands
 * Extends Command
 *
 * @param receiver Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "help", description = "Show info about available commands")
class Help(private val receiver: ReceiverInterface<Route>) : Command {
    override fun execute(args: ArrayList<String>) : Boolean {
        val commandMap = CommandsLoader(receiver).getCommandsByNames()

        var maxCommandWithArgsLength = 0
        val commandsInfo = ArrayList<Pair<String, String>>()

        // Get commands names, args and descriptions from annotation
        commandMap.values.forEach {
            val ann = it.javaClass.getAnnotation(ConsoleCommand::class.java)

            val commandWithArgs = ann.name + (if (ann.args.isNotEmpty()) " " else "") + ann.args
            maxCommandWithArgsLength = max(maxCommandWithArgsLength, commandWithArgs.length)

            commandsInfo.add(Pair(commandWithArgs, ann.description))
        }

        // Sort by commands names
        commandsInfo.sortBy { it.first }

        println("Available commands:")

        commandsInfo.forEach {
            println("- ${it.first.padEnd(maxCommandWithArgsLength)} : ${it.second}")
        }

        return true
    }
}
