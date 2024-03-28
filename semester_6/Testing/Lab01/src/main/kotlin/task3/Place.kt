package task3

class Place (private val weatherState: Weather) {
    val computerBank: ComputerBank = ComputerBank("Компьютерный банк", weatherState)
    val locations = arrayListOf(
        Location("Компьютерный банк", computerBank, Coordinates(0, 0)),
        Location("Угол", People("Они"), Coordinates(1, 0)),
        Location("Коворкинг", People("ВТшники"), Coordinates(1, 1))
    )

    fun tick() {
        locations.forEach {
            it.locationObject.checkState()
        }

        if (computerBank.isSomethingMetalMelting()) {
            val computerBankLoc = locations.find { it.locationObject === computerBank }
            val nearLocation = computerBankLoc?.nearestLocation(locations)

            if (nearLocation != null && nearLocation.locationObject.dangerLevel < Level.HIGH) {
                println("Густые ручейки расплавленного металла начали заползать в ${nearLocation.name}, " +
                        "в котором был ${nearLocation.locationObject.name}")
                nearLocation.locationObject.dangerLevel = Level.HIGH
            }
        }
    }
}