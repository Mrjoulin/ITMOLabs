package func

import interfaces.BasicFunction
import kotlin.math.pow

class Function(
    private val sin: BasicFunction,
    private val ln: BasicFunction,
    private val log2: BasicFunction,
    private val log3: BasicFunction,
    private val log10: BasicFunction
) : BasicFunction {
    override fun calc(x: Number, precision: Double): Double {
        return if (x.toInt() <= 0) sin.calc(x, precision)
            else (
                (log3.calc(x, precision).pow(3) / log3.calc(x, precision) / log3.calc(x, precision)) * // Genius
                (log10.calc(x, precision) / log3.calc(x, precision)).pow(2) -
                (log2.calc(x, precision) + ln.calc(x, precision).pow(2) + log3.calc(x, precision).pow(2))
            )
    }

    override fun calc(x: Number): Double {
        return if (x.toDouble() <= 0) sin.calc(x)
            else (
                (log3.calc(x).pow(3) / log3.calc(x) / log3.calc(x)) * // Genius
                ((log10.calc(x) / log3.calc(x)).pow(2)) -
                (log2.calc(x) + (ln.calc(x).pow(2)) + (log3.calc(x).pow(2)))
            )
    }

    override fun toString(): String = "func"
}