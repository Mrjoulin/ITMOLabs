package input

import java.io.*
import kotlin.jvm.Throws


private val buffersByReaders = HashMap<InputStreamReader, BufferedReader>()


/**
 * Method to remove unnecessary readers to optimize memory
 *
 * @param input Input witch reader need to remove
 * @author Matthew I.
 */
fun removeBufferByReader(input: InputStreamReader) {
    if (buffersByReaders.containsKey(input)) {
        buffersByReaders.remove(input)
    }
}

/**
 * Method to get split input of new line from given input stream
 *
 * @param input Input stream reader, which from we reads new line
 * @return Split line witch received from method splitInput
 *
 * @author Matthew I.
 */
@Throws(IOException::class)
fun getInput(input: InputStreamReader, split: Boolean = true) : ArrayList<String>? {
    if (!buffersByReaders.containsKey(input))
        buffersByReaders[input] = BufferedReader(input)

    val bufferedReader = buffersByReaders[input]

    val line = bufferedReader?.readLine() ?: throw IOException("End of file!")

    return if (split) splitInput(line=line) else arrayListOf(line)
}

/**
 * Method to split line by separator, considering writing strings in quotes
 *
 * @param line Line to split
 * @param separator: Char The separator by which the string is split, default ' '
 * @return ArrayList of split line, if line is not null and there is any data in line else return null
 *
 * @author Matthew I.
 */
fun splitInput(line: String?, separator: Char = ' '): ArrayList<String>? {
    if (line == null) return null

    val splitLine: ArrayList<String> = ArrayList()
    val currentString = StringBuilder()
    var screeningStarted = false

    for (ch in line.toCharArray()) {
        if (ch == separator && !screeningStarted) {
            if (currentString.isNotEmpty()) splitLine.add(currentString.toString())
            currentString.setLength(0)
        }
        else if (ch == '"') screeningStarted = !screeningStarted
        else currentString.append(ch)
    }

    if (currentString.isNotEmpty()) splitLine.add(currentString.toString())

    if (splitLine.isEmpty()) return null

    return splitLine
}