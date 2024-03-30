import func.Function
import interfaces.BasicFunction
import log.Ln
import log.Log
import trig.Sin

fun main() {
    val sin = Sin()
    val ln = Ln()
    val log2 = Log(2, ln)
    val log3 = Log(3, ln)
    val log10 = Log(10, ln)
    val func = Function(sin, ln, log2, log3, log10)

    for (f: BasicFunction in listOf(sin, ln, log2, log3, log10, func)) {
        val logger = CsvLogger(f)
        logger.writeRange(0.1, 0.5, 100)
    }
}