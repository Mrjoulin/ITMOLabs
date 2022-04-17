package commands.names

import commands.annotations.ConsoleCommand
import commands.interfaces.Command
import manager.interfaces.CollectionManagerInterface
import entities.Route
import file.CollectionFileProcess
import network.Request
import network.Response


/**
 * Class to save collection to the file (from environment)
 * Extends Command
 *
 * @param collectionManager Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "save", description = "Save collection to the file (from environment)", canBeSentFromUser = false)
class Save(private val collectionManager: CollectionManagerInterface<Route>) : Command(collectionManager) {
    override fun execute(request: Request): Response {
        val success = CollectionFileProcess(collectionManager).save()

        return Response(success, if (success) "Collection saved" else "Collection not saved!")
    }
}
