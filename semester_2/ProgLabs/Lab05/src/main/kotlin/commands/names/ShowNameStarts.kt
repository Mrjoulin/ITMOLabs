package commands.names

import commands.interfaces.*
import entities.Route
import receiver.ReceiverInterface

import kotlin.collections.ArrayList

/**
 * Class to show info about entities in collection witch name starts with given substring
 * Extends Command
 *
 * @param receiver Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(
    name = "filter_starts_with_name", args = "name",
    description = "Show info about entities in collection witch name starts with given substring"
)
class ShowNameStarts(private val receiver: ReceiverInterface<Route>) : Command {
    override fun execute(args: ArrayList<String>) : Boolean {
        if (args.isEmpty()) {
            println("Please input name - substring with which entities names start")
            return false
        }

        val filteredEntities = receiver.getEntitiesSet().sortedBy { it.id }.filter { it.name.startsWith(args[0]) }

        println(
            if (filteredEntities.isNotEmpty())
                filteredEntities.joinToString(
                    separator="\n\n",
                    prefix="Info about all entities in collection witch name starts with \"${args[0]}\":\n"
                )
            else "There is no entities find in the collection with name starts with ${args[0]} "
        )

        return true
    }
}
