package utils

import commands.annotations.ConsoleCommand
import commands.interfaces.Command
import receiver.interfaces.ReceiverInterface
import entities.Route

import java.security.CodeSource
import java.util.zip.ZipInputStream


private var commandsByNames = HashMap<String, Command>()

/**
 * Class to load commands classes from commands package using Reflection API
 *
 * @param receiver Receiver object to create commands (each command class gets receiver as a param)
 *
 * @author Matthew I.
 */
class CommandsLoader(private val receiver: ReceiverInterface<Route>) {
    private val commandsPackage = "commands.names"

    /**
     * Method to find commands in package (using method getCommandsInPackage()) and
     * create they objects using Reflection API.
     *
     * @param reload If method was already called, result HashMap stores in var commandsByNames
     *               And this flag is needed in order to load all commands again
     *
     * @return HashMap of commands name (from ConsoleCommand annotation) and created objects of Commands
     *
     * @author Matthew I.
     */
    fun getCommandsByNames(reload: Boolean = false): HashMap<String, Command> {
        if (commandsByNames.isNotEmpty() && !reload) return commandsByNames

        commandsByNames = HashMap()

        val commandsClassesPaths = getCommandsInPackage()

        if (commandsClassesPaths.isEmpty())
            logger.error("Commands not found in package \"$commandsPackage\"!")

        commandsClassesPaths.forEach { className ->
            try {
                // Load class
                val commandClass = Class.forName(className)

                // If class annotated by @ConsoleCommand
                if (commandClass.isAnnotationPresent(ConsoleCommand::class.java)) {
                    val ann = commandClass.getDeclaredAnnotation(ConsoleCommand::class.java)

                    val constructor = commandClass.getDeclaredConstructor(ReceiverInterface::class.java)
                    val command = constructor.newInstance(receiver)

                    commandsByNames[ann.name] = command as Command
                }
            } catch (e: ClassNotFoundException) {
                logger.error(e.message)
                logger.error("Command from class $className not found")
            } catch (e: IllegalAccessException) {
                logger.error(e.message)
                logger.error("Can't get access to command from class $className")
            }
        }

        return commandsByNames
    }

    /**
     * Method to find commands classes names in package
     *
     * @return ArrayList of strings: path in format <commandsPackage>.<command class name> to each founded command
     *
     * @author Matthew I.
     */
    private fun getCommandsInPackage(): ArrayList<String> {
        val commandsNames = ArrayList<String>()

        val src: CodeSource = this::class.java.protectionDomain.codeSource
        val zipStream = ZipInputStream(src.location.openStream())

        val commandsUrl = commandsPackage.replace(".", "/")

        while(true){
            val entry = zipStream.nextEntry?.name ?: return commandsNames

            if (entry.contains(commandsUrl) && !entry.contains("$") && entry.endsWith(".class")) {
                val correctName = entry.dropLast(6).replace("/", ".")

                logger.debug("Find command: $correctName")

                commandsNames.add(correctName)
            }
        }
    }
}