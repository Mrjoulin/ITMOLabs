package commands.names

import commands.annotations.ConsoleCommand
import commands.interfaces.Command
import receiver.interfaces.ReceiverInterface
import entities.Route
import utils.CreateEntity

import kotlin.collections.ArrayList

/**
 * Class to remove all entities from collection lower than given
 * Extends Command
 *
 * @param receiver Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "remove_lower", description = "Remove all entities from collection lower than given")
class RemoveLower(private val receiver: ReceiverInterface<Route>) : Command {
    override fun execute(args: ArrayList<String>) : Boolean {
        val route = CreateEntity(receiver).getObjectInstanceFromInput(Route::class.java)

        if (route != null) {
            val sortedEntities = receiver.getEntitiesSet().sorted()
            var cnt = 0

            sortedEntities.filter { it < route }.forEach {
                val success = receiver.removeEntity(it)
                cnt++

                if (!success) {
                    println("Entity ${it.id} wasn't removed because of unexpected error!")
                    return false
                }
            }

            if (sortedEntities.isNotEmpty())
                println("All entities lower than given was removed. Removed $cnt entities")
            else
                println("There is no entities in collection, so no entities removed")

            return true
        }

        return false
    }
}
