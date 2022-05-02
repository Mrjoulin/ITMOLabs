package utils

/**
 * Class to store and process current execute script path (to avoid recursion)
 *
 * @author Matthew I.
 */
class ExecuteScriptPath {
    companion object {
        @JvmStatic
        private val executeScriptPath = ArrayList<String>()

        @JvmStatic
        fun addToExecuteScriptPath(filename: String) : Boolean {
            if (!executeScriptPath.contains(filename))
                return executeScriptPath.add(filename)
            return false
        }

        @JvmStatic
        fun removeFromExecuteScriptPath(filename: String) : Boolean {
            return executeScriptPath.remove(filename)
        }
    }
}