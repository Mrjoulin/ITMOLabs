package commands.names

import commands.interfaces.*
import entities.Route
import receiver.ReceiverInterface
import utils.CreateEntity

import kotlin.collections.ArrayList

/**
 * Class to add new entity to collection if it greater than others
 * Extends Command
 *
 * @param receiver Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "add_if_max", description = "Add new entity to collection if it greater than others")
class AddMax(private val receiver: ReceiverInterface<Route>) : Command {
    override fun execute(args: ArrayList<String>) : Boolean {
        val newRoute = CreateEntity(receiver).getObjectInstanceFromInput(Route::class.java)

        if (newRoute != null) {
            val maxEntity = receiver.getEntitiesSet().maxByOrNull { it }
            var success = true

            if (maxEntity == null || newRoute > maxEntity) {
                success = receiver.addNewEntity(newRoute)

                if (success)
                    println("New entity ID #${newRoute.id} successfully added")
                else
                    println("Entity wasn't added because of unexpected error!")
            } else
                println("Entity isn't max in collection, so it wasn't added")

            return success
        }

        return false
    }
}
