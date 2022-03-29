package entities

import entities.validators.annotations.*

import java.util.*
import kotlin.math.sqrt


/**
 * Coordinates comparable object, witch used in Route.
 * All fields have annotations-validators to be able to create this entity in CreateEntity class.
 * Also can be created from Map with fields names and fields values
 *
 * @param x Coordinate X, enter from the user (annotated by @InputField)
 * @param y Coordinate Y, max value is 973, enter from the user (annotated by @InputField, @ValueIntSize)
 *
 * @author Matthew I.
 */
class Coordinates (
    @InputField(description = "X coordinate")
    val x: Long,

    @InputField(description = "Y coordinate")
    @ValueIntSize(lower_than = 974)
    val y: Int

) : Comparable<Coordinates> {

    constructor(mapData: HashMap<String, Any?>): this(
        mapData["x"] as Long? ?: throw IllegalArgumentException("X coordinate"),
        mapData["y"] as Int? ?: throw IllegalArgumentException("Y coordinate")
    )

    // Compare coordinates by sqrt(x^2 + y^2) - scalar value
    override fun compareTo(other: Coordinates): Int {
        fun formula(x: Double, y: Double): Double = sqrt(x*x + y*y)

        val myFormula = formula(x.toDouble(), y.toDouble())
        val otherFormula = formula(other.x.toDouble(), other.y.toDouble())

        return (myFormula - otherFormula).toInt()
    }

    override fun toString(): String {
        return "- - X coordinate: $x\n" +
                "- - Y coordinate: $y"
    }

    override fun hashCode(): Int {
        return Objects.hash(x, y)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Coordinates) return false

        return x == other.x && y == other.y
    }
}
