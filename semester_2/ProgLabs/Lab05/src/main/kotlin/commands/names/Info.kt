package commands.names

import commands.interfaces.*
import entities.Route
import receiver.ReceiverInterface

import kotlin.collections.ArrayList

/**
 * Class to show info about collection
 * Extends Command
 *
 * @param receiver Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "info", description = "Show info about collection")
class Info(private val receiver: ReceiverInterface<Route>) : Command {
    override fun execute(args: ArrayList<String>) : Boolean {
        val col = receiver.getEntitiesSet()

        println(
            "Collection of entities info:\n" +
            "- Collection type: ${col.javaClass.simpleName}\n" +
            "- Initialization date: ${receiver.getCreationDate()}\n" +
            "- Elements type: ${Route::class.java.simpleName}\n" +
            "- Elements count: ${col.size}"
        )

        return true
    }
}
