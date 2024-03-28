package task3

class ComputerBankState(private val name: String, var isBroken: Boolean = false) {
    fun destroy() {
        if (!isBroken) {
            println("$name разваливается на куски")
            isBroken = true
        }
    }

    fun repair() {
        if (isBroken) {
            println("$name восстаёт из пепла")
            isBroken = false
        }
    }
}
