package utils

import commands.annotations.ConsoleCommand
import commands.interfaces.Command
import manager.interfaces.CollectionManagerInterface
import entities.Route

import java.security.CodeSource
import java.util.zip.ZipInputStream


private var userCommandsByNames = HashMap<String, Command>()
private var specialCommandsByNames = HashMap<String, Command>()

/**
 * Class to load commands classes from commands package using Reflection API
 *
 * @param collectionManager Collection manager object to create commands (each command class gets collectionManager as a param)
 *
 * @author Matthew I.
 */
class CommandsLoader(private val collectionManager: CollectionManagerInterface<Route>) {
    private val commandsPackage = "commands.names"

    fun getUserCommandsByNames(reload: Boolean = false): HashMap<String, Command> {
        if (userCommandsByNames.isNotEmpty() && !reload) return userCommandsByNames

        return getCommandsByNames()
    }

    fun getSpecialCommandsByNames(reload: Boolean = false): HashMap<String, Command> {
        if (specialCommandsByNames.isNotEmpty() && !reload) return specialCommandsByNames

        return getCommandsByNames(userCommands = false)
    }


    /**
     * Method to find commands in package (using method getCommandsInPackage()) and
     * create they objects using Reflection API.
     *
     * @param userCommands if true, method will return user commands else special commands
     *
     * @return HashMap of commands name (from ConsoleCommand annotation) and created objects of Commands
     *
     * @author Matthew I.
     */
    private fun getCommandsByNames(userCommands: Boolean = true): HashMap<String, Command> {
        userCommandsByNames = HashMap()
        specialCommandsByNames = HashMap()

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

                    val constructor = commandClass.getDeclaredConstructor(CollectionManagerInterface::class.java)
                    val command = constructor.newInstance(collectionManager)

                    if (ann.canBeSentFromUser)
                        userCommandsByNames[ann.name] = command as Command
                    else
                        specialCommandsByNames[ann.name] = command as Command
                }
            } catch (e: ClassNotFoundException) {
                logger.error(e.message)
                logger.error("Command from class $className not found")
            } catch (e: IllegalAccessException) {
                logger.error(e.message)
                logger.error("Can't get access to command from class $className")
            }
        }

        return if (userCommands) userCommandsByNames else specialCommandsByNames
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