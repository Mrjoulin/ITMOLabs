package task3

class Bombing (private val weatherState: Weather) {
    var isBombing: Boolean = false;

    fun startBombing() {
        if (!isBombing) {
            println("Бомбардировка началась")

            isBombing = true
            weatherState.temperatureLevel = Level.UNIMAGINABLE
            weatherState.noiseLevel = Level.UNIMAGINABLE
        }
    }

    fun endBombing() {
        if (isBombing) {
            println("Бомбардировка закончилась")

            isBombing = false
            weatherState.temperatureLevel = Level.HIGH
            weatherState.noiseLevel = Level.NORMAL
        }
    }
}