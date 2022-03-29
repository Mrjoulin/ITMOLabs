package commands.interfaces

/**
 * Interface that describes executable command
 *
 * @author Matthew I.
 */
interface Command {
    /**
     * Execute command with given args
     *
     * @param args List of input args
     * @return Success of execution
     *
     * @author Matthew I.
     */
    fun execute(args: ArrayList<String>) : Boolean
}
