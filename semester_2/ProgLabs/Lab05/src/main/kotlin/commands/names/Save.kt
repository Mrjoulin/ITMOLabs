package commands.names

import commands.interfaces.*
import entities.Route
import file.CollectionFileProcess
import receiver.ReceiverInterface

import kotlin.collections.ArrayList

/**
 * Class to save collection to the file (from environment)
 * Extends Command
 *
 * @param receiver Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "save", description = "Save collection to the file (from environment)")
class Save(private val receiver: ReceiverInterface<Route>) : Command {
    override fun execute(args: ArrayList<String>) : Boolean {
        val success = CollectionFileProcess(receiver).save()

        if (success)
            println("Collection saved")
        else
            println("Collection not saved!")

        return success
    }
}
