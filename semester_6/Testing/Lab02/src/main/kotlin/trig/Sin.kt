package trig

import interfaces.BasicFunction
import kotlin.math.abs
import kotlin.math.pow

class Sin : BasicFunction {
    companion object {
        private const val PI = Math.PI;
        private const val MAX_POWER: Int = 17
        private val factorials: LongArray = LongArray(MAX_POWER + 1).also {
            (0..MAX_POWER).forEach { i ->
                it[i] = maxOf(it.getOrElse(i - 1) { 1L } * i, 1L)
            }
        }
    }

    private fun calc(x: Double, precision: Double): Double {
        // Move x to [0; 2 * PI] range
        var dot = x % (2 * PI) + (if (x < 0) 2 * PI else 0.0)
        // Move x to [0; PI / 2] range, also save sin sign
        val sign = if (dot / PI > 1) -1 else 1
        dot = if (dot % PI > PI / 2) PI - dot % PI else dot % PI

        // Calc Taylor row for sin (x - x^3/3! + x^5/5! - ...)
        var step = 1; var prev = 0.0; var res = dot

        while (abs(res - prev) > precision && step < MAX_POWER) {
            step += 2
            prev = res
            res += (-1.0).pow(step / 2) * dot.pow(step) / factorials[step]
        }

        return sign * res
    }

    override fun calc(x: Number, precision: Double): Double =
        calc(x.toDouble(), maxOf(minOf(precision, 1.0), 1e-12))
    override fun calc(x: Number): Double =
        calc(x.toDouble(), 1e-12)

    override fun toString(): String = "sin"
}