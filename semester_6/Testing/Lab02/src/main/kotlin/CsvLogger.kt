import interfaces.BasicFunction
import java.io.File

class CsvLogger(
    private val func: BasicFunction
) {
    private val maxNumIterations = 1000
    private val maxStepSize = 1e3
    private val minStepSize = 1e-3
    private val maxPrecision = 1
    private val minPrecision = 1e-12

    fun writeRange(start: Double, step: Double, numIterations: Int, precision: Double) {
        if (numIterations < 0 || numIterations > maxNumIterations)
            throw IllegalArgumentException("Number of iterations must be from 0 to $maxNumIterations")
        if (step < minStepSize || step > maxStepSize)
            throw IllegalArgumentException("Step must be from $minStepSize to $maxStepSize")
        if (precision < minPrecision || precision > maxPrecision)
            throw IllegalArgumentException("Precision must be from $minStepSize to $maxStepSize")

        var cur = start
        val end = start + numIterations * step

        val funName = func::class.simpleName
        val file = File("csv/logs_${funName}.csv")

        file.writeText("X, Result")

        while (cur < end) {
            file.appendText("$cur, ${func.calc(cur, precision)}")
            cur += step
        }
    }

    fun writeRange(start: Double, step: Double, numIterations: Int) {
        if (numIterations < 0 || numIterations > maxNumIterations)
            throw IllegalArgumentException("Number of iterations must be from 0 to $maxNumIterations")
        if (step < minStepSize || step > maxStepSize)
            throw IllegalArgumentException("Step must be from $minStepSize to $maxStepSize")

        var cur = start
        val end = start + numIterations * step

        val funName = func.toString()
        val file = File("csv/logs_${funName}.csv")

        file.writeText("X,Result\n")

        while (cur < end) {
            println("$funName ${func.calc(cur)}")
            file.appendText("$cur,${func.calc(cur)}\n")
            cur += step
        }
    }
}