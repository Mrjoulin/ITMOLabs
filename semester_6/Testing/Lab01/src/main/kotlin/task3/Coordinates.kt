package task3

import kotlin.math.pow
import kotlin.math.sqrt

class Coordinates (
    val x: Number,
    val y: Number
) {
    fun dist(c: Coordinates): Double {
        return sqrt((x.toDouble() - c.x.toDouble()).pow(2) + (y.toDouble() - c.y.toDouble()).pow(2))
    }
}
