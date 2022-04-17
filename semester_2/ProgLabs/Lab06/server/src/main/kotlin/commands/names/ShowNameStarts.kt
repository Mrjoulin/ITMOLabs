package commands.names

import commands.annotations.ConsoleCommand
import commands.interfaces.Command
import manager.interfaces.CollectionManagerInterface
import entities.Route
import network.Request
import network.Response


/**
 * Class to show info about entities in collection witch name starts with given substring
 * Extends Command
 *
 * @param collectionManager Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(
    name = "filter_starts_with_name", args = "name",
    description = "Show info about entities in collection witch name starts with given substring"
)
class ShowNameStarts(private val collectionManager: CollectionManagerInterface<Route>) : Command(collectionManager) {
    override fun execute(request: Request): Response {
        if (request.commandArgs.isEmpty())
            return Response(
                success = false,
                message = "Please input name - substring with which entities names start"
            )

        val namePrefix = request.commandArgs[0]

        val sortedEntities =  collectionManager.getEntitiesSet().sortedBy { it.name }
        val filteredEntities = sortedEntities.filter { it.name.startsWith(namePrefix) }

        val message =
            if (filteredEntities.isNotEmpty())
                filteredEntities.joinToString(
                    separator="\n\n",
                    prefix="Info about all entities in collection witch name starts with \"$namePrefix\":\n"
                )
            else "There is no entities find in the collection with name starts with $namePrefix "

        return Response(success = true, message = message)
    }
}
