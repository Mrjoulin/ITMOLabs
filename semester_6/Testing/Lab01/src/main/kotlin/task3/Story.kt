package task3

fun main() {
    val weather = Weather()
    val place = Place(weather)
    val bombing = Bombing(weather)

    bombing.startBombing()

    for (tick in 1..10)
        place.tick()
}