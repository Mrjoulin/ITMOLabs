package commands.names

import commands.annotations.ConsoleCommand
import commands.interfaces.Command
import manager.interfaces.CollectionManagerInterface
import entities.Route
import network.Request
import network.Response


/**
 * Class to show info about entity with max "to location"
 * Extends Command
 *
 * @param collectionManager Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "max_by_to", description = "Show info about entity with max \"to location\"")
class ShowMaxByTo(private val collectionManager: CollectionManagerInterface<Route>) : Command(collectionManager) {
    override fun execute(request: Request): Response {
        val maxByTo = collectionManager.getEntitiesSet().maxByOrNull { it.to }

        return Response(
            success = true,
            message = maxByTo?.toString() ?: "There is no entities added to the collection"
        )
    }
}
