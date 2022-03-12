package commands.names

import commands.interfaces.*
import entities.Route
import receiver.ReceiverInterface
import utils.CreateEntity

import kotlin.collections.ArrayList

/**
 * Class to add new entity to collection if it lower than others
 * Extends Command
 *
 * @param receiver Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "add_if_min", description = "Add new entity to collection if it lower than others")
class AddMin(private val receiver: ReceiverInterface<Route>) : Command {
    override fun execute(args: ArrayList<String>) : Boolean {
        val newRoute = CreateEntity(receiver).getObjectInstanceFromInput(Route::class.java)

        if (newRoute != null) {
            val minEntity = receiver.getEntitiesSet().minByOrNull { it }
            var success = true

            if (minEntity == null || newRoute < minEntity) {
                success = receiver.addNewEntity(newRoute)

                if (success)
                    println("New entity ID #${newRoute.id} successfully added")
                else
                    println("Entity wasn't added because of unexpected error!")
            } else
                println("Entity isn't min in collection, so it wasn't added")

            return success
        }

        return false
    }
}
