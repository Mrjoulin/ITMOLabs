package commands.interfaces

/**
 * Annotation above the command Class, says that this command is available to use in console
 *
 * @param name Command name
 * @param args Commands inline args description, default empty string
 * @param description Command description (to show user in help message)
 *
 * @author Matthew I.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class ConsoleCommand(val name: String, val args: String = "", val description: String)
