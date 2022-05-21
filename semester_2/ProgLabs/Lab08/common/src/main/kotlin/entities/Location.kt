package entities

import entities.validators.annotations.*
import java.io.Serializable

import java.util.*
import kotlin.math.sqrt

/**
 * Location comparable object, witch used in Route.
 * All fields have annotations-validators to be able to create this entity in CreateEntity class.
 * Also can be created from Map with fields names and fields values
 *
 * @param name Location name, max length 379, enter from the user (annotated by @InputField, @ValueIntSize)
 * @param x Location X, enter from the user (annotated by @InputField)
 * @param y Location Y, enter from the user (annotated by @InputField)
 *
 * @author Matthew I.
 */
class Location(// Длина строки не должна быть больше 379, Поле не может быть null
    @InputField("location name")
    @ValueIntSize(lower_than = 380)
    val name: String,

    @InputField("location X")
    val x: Double,

    @InputField("location Y")
    val y: Float

) : Comparable<Location>, Serializable {

    constructor(mapData: Map<String, Any?>): this(
        mapData["name"] as String? ?: throw IllegalArgumentException("name"),
        mapData["x"] as Double? ?: throw IllegalArgumentException("location X"),
        mapData["y"] as Float? ?: throw IllegalArgumentException("location Y")
    )

    // Compare locations by sqrt(x^2 + y^2) - scalar value
    override fun compareTo(other: Location): Int {
        fun formula(x: Double, y: Float): Double = sqrt(x*x + y*y)

        return (formula(x, y) - formula(other.x, other.y)).toInt()
    }

    override fun toString(): String {
        return "$name - ($x; $y)"
    }

    override fun hashCode(): Int {
        return Objects.hash(name, x, y)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Location) return false

        return name == other.name && x == other.x && y == other.y
    }
}