package task3

class ComputerBank (name: String, private val weatherState: Weather) : LocationObject(name) {
    var state: ComputerBankState = ComputerBankState(name)
    var pieces: ArrayList<ComputerBankPiece> = arrayListOf(
        ComputerBankPiece("Лицевая сторона", true),
        ComputerBankPiece("Другая сторона", false)
    )

    override fun checkState() {
        if (dangerLevel >= Level.HIGH || weatherState.temperatureLevel >= Level.HIGH)
            state.destroy()
        if (weatherState.temperatureLevel >= Level.UNIMAGINABLE)
            pieces.forEach { it.melt() }
    }

    fun isSomethingMetalMelting() : Boolean {
        return pieces.any { it.isMelted }
    }
}
