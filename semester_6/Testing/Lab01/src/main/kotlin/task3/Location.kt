package task3

class Location (
    val name: String,
    val locationObject: LocationObject,
    val coordinates: Coordinates
) {
    fun nearestLocation(locations: ArrayList<Location>): Location? {
        val otherLocations = locations.filter { it !== this }
        if (otherLocations.isEmpty()) return null
        return otherLocations.minBy { it.coordinates.dist(coordinates) }
    }
}
