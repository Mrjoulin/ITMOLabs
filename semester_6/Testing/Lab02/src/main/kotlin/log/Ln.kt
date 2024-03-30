package log

import interfaces.BasicFunction
import kotlin.math.abs
import kotlin.math.pow

class Ln : BasicFunction {
    companion object {
        private const val MAX_POWER: Int = 1e6.toInt()
    }

    private fun calc(x: Double, precision: Double): Double {
        if (x <= 0) throw IllegalArgumentException("x must be positive")

        // ln((dot + 1) / (dot - 1)) = 2(x + x^3/3 + x^5/5 + ...)
        val dot = (x - 1) / (x + 1)
        var pDot = dot
        var step = 1; var prev = 0.0; var res = 2 * dot

        while (abs(res - prev) > precision / 2 && step < MAX_POWER) {
            step += 2
            prev = res
            pDot *= dot.pow(2)
            res += 2 * pDot / step
        }
        // println("$step $prev $res diff ${res - prev}")
        return res
    }

    override fun calc(x: Number, precision: Double): Double =
        calc(x.toDouble(), maxOf(minOf(precision, 1.0), 1e-12))
    override fun calc(x: Number): Double =
        calc(x.toDouble(), 1e-12)

    override fun toString(): String = "ln"
}