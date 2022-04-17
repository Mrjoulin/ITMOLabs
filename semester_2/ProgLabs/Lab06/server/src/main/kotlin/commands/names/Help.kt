package commands.names

import commands.annotations.ConsoleCommand
import commands.interfaces.Command
import manager.interfaces.CollectionManagerInterface
import entities.Route
import network.Request
import network.Response
import utils.CommandsLoader

import kotlin.collections.ArrayList
import kotlin.math.max

/**
 * Class to show info about available commands
 * Extends Command
 *
 * @param collectionManager Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "help", description = "Show info about available commands")
class Help(private val collectionManager: CollectionManagerInterface<Route>) : Command(collectionManager) {
    override fun execute(request: Request): Response {
        val commandMap = CommandsLoader(collectionManager).getUserCommandsByNames()

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

        val commandsMessages: ArrayList<String> = ArrayList()

        commandsInfo.forEach {
            commandsMessages.add("- ${it.first.padEnd(maxCommandWithArgsLength)} : ${it.second}")
        }

        val message = commandsMessages.joinToString("\n", prefix = "Available commands:\n")

        return Response(success = true, message = message)
    }
}
