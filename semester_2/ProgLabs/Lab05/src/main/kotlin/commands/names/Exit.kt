package commands.names

import commands.interfaces.*
import entities.Route
import receiver.ReceiverInterface

import kotlin.collections.ArrayList
import kotlin.system.exitProcess

/**
 * Class to exit program (without saving)
 * Extends Command
 *
 * @param receiver Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "exit", description = "Exit program (without saving)")
class Exit(private val receiver: ReceiverInterface<Route>) : Command {
    override fun execute(args: ArrayList<String>) : Boolean {
        println("Exit program, come again!")
        exitProcess(0)
    }
}
