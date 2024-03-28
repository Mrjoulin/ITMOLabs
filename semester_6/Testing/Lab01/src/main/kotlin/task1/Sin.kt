package task1

import kotlin.math.pow

class Sin {
    companion object {
        private const val PI = Math.PI;
        private const val MAX_POWER: Int = 17
        private val factorials: LongArray = LongArray(MAX_POWER + 1).also {
            (0..MAX_POWER).forEach { i ->
                it[i] = maxOf(it.getOrElse(i - 1) { 1L } * i, 1L)
            }
        }
    }

    private fun calc(x: Double, precision: Int): Double {
        // Move x to [0; 2 * PI] range
        var dot = x % (2 * PI) + (if (x < 0) 2 * PI else 0.0)
        // Move x to [0; PI / 2] range, also save sin sign
        val sign = if (dot / PI > 1) -1 else 1
        dot = if (dot % PI > PI / 2) PI - dot % PI  else dot % PI

        // Calc Taylor row for sin (x - x^3/3! + x^5/5! - ...)
        return sign * (1..precision step 2).sumOf {
            (-1.0).pow(it / 2) * dot.pow(it) / factorials[it]
        }
    }

    fun calc(x: Number, precision: Int): Double = calc(x.toDouble(), maxOf(minOf(precision, MAX_POWER), 3))
    fun calc(x: Number): Double = calc(x.toDouble(), MAX_POWER)
}