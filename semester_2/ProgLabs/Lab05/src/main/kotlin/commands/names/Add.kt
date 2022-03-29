package commands.names

import commands.annotations.ConsoleCommand
import commands.interfaces.Command
import receiver.interfaces.ReceiverInterface
import entities.Route
import utils.CreateEntity

import kotlin.collections.ArrayList

/**
 * Class to add new entity to collection
 * Extends Command
 *
 * @param receiver Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "add", description = "Add new entity to collection")
class Add(private val receiver: ReceiverInterface<Route>) : Command {
    override fun execute(args: ArrayList<String>) : Boolean {
        val newRoute = CreateEntity(receiver).getObjectInstanceFromInput(Route::class.java)

        if (newRoute != null) {
            val success = receiver.addNewEntity(newRoute)

            if (success)
                println("New entity ID #${newRoute.id} successfully added")
            else
                println("Entity wasn't added because of unexpected error!")

            return success
        }

        return false
    }
}
