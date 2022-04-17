package input

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.io.InputStreamReader
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
 * @param prefixText Message, to show for user to get need input (if input not from file)
 * @return Split line witch received from method splitInput
 *
 * @author Matthew I.
 */
@Throws(IOException::class)
fun getInput(input: InputStreamReader, prefixText: String = "") : ArrayList<String>? {
    if (!buffersByReaders.containsKey(input))
        buffersByReaders[input] = BufferedReader(input)

    val bufferedReader = buffersByReaders[input]

    if (input.javaClass != FileReader::class.java) print(prefixText)

    val line = bufferedReader?.readLine() ?: throw IOException("End of file!")

    return splitInput(line=line)
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