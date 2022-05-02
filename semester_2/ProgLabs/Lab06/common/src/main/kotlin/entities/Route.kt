package entities

import entities.validators.annotations.*

import java.util.*
import java.text.SimpleDateFormat
import kotlin.IllegalArgumentException
import kotlin.collections.HashMap
import kotlin.math.abs

/**
 * Main comparable entity, witch receiver is managing.
 * All fields have annotations-validators to be able to create this entity in CreateEntity class.
 * Also can be created from Map with fields names and fields values
 *
 * @param id Entity ID, Must be Unique (annotated by @UniqueID)
 * @param creationDate Entity creation date (annotated by @CurrentDate)
 * @param name Entity name, enter from the user (annotated by @InputField)
 * @param coordinates Entity coordinates object, enter from the user (annotated by @InputField)
 * @param from Entity from location object, enter from the user (annotated by @InputField)
 * @param to Entity to location object, enter from the user (annotated by @InputField)
 * @param distance Entity distance, must be greater then 1.0, enter from the user (annotated by @InputField, @ValueDoubleSize)
 *
 * @author Matthew I.
 */
class Route(
    @UniqueId
    val id: Int,

    @CurrentDate
    val creationDate: Date,

    @InputField(description = "name")
    val name: String,

    @InputField(description = "coordinates", isPrimitiveType = false)
    val coordinates: Coordinates,

    @InputField(description = "from location", isPrimitiveType = false)
    val from: Location,

    @InputField(description = "to location", isPrimitiveType = false)
    val to: Location,

    @InputField(description = "distance")
    @ValueDoubleSize(greater_than = 1.0)
    val distance: Double

) : Comparable<Route> {

    constructor(mapData: Map<String, Any?>): this(
        mapData["id"] as? Int? ?: throw IllegalArgumentException("id"),
        mapData["creationDate"] as? Date? ?: throw IllegalArgumentException("creationDate"),
        mapData["name"] as? String? ?: throw IllegalArgumentException("name"),
        mapData["coordinates"] as? Coordinates? ?: throw IllegalArgumentException("coordinates"),
        mapData["from"] as? Location? ?: throw IllegalArgumentException("from location"),
        mapData["to"] as? Location? ?: throw IllegalArgumentException("to location"),
        mapData["distance"] as? Double? ?: throw IllegalArgumentException("distance"),
    )

    // Compare routes by distance if their difference is greater then 1.0 else by creation date
    override fun compareTo(other: Route): Int {
        if (abs(distance - other.distance) >= 1.0)
            return (distance - other.distance).toInt()

        return creationDate.compareTo(other.creationDate)
    }

    override fun toString(): String {
        return "Route ID #$id:\n" +
                "- Creation Date: ${SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(creationDate)}\n" +
                "- Name: $name\n" +
                "- Coordinates:\n$coordinates\n" +
                "- From location:\n$from\n" +
                "- To location:\n$to\n" +
                "- Distance: $distance"
    }

    override fun hashCode(): Int {
        return Objects.hash(id, creationDate, name, coordinates, from, to, distance)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Route) return false

        return name == other.name && coordinates == other.coordinates &&
                from == other.from && to == other.to && distance == other.distance
    }
}