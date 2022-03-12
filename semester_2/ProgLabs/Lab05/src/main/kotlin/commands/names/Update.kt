package commands.names

import commands.interfaces.*
import entities.Route
import receiver.ReceiverInterface
import utils.*

import kotlin.collections.ArrayList

/**
 * Class to update entity by given id
 * Extends Command
 *
 * @param receiver Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "update", args = "id", description = "Update entity by given id")
class Update(private val receiver: ReceiverInterface<Route>) : Command {
    override fun execute(args: ArrayList<String>): Boolean {
        val result = getEntityByIdFromInputArgs(receiver.getEntitiesSet(), args)

        if (result.first == null) {
            println(result.second)
            return false
        }

        val route = result.first!!

        val updatedRoute = CreateEntity(receiver).getObjectInstanceFromInput(Route::class.java, objectMap(route))

        if (updatedRoute != null) {
            var success = receiver.removeEntity(route)
            success = success && receiver.addNewEntity(updatedRoute)

            if (success)
                println("Entity ID ${route.id} successfully updated")
            else
                println("Entity wasn't updated! Unexpected error")

            return true
        }

        return false
    }
}
