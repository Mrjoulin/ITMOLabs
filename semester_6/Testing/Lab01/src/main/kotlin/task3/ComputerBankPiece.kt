package task3

class ComputerBankPiece(var name: String, var isMetal: Boolean) {
    var isMelted: Boolean = false
    fun melt() {
        if (isMetal && !isMelted) {
            println("$name расплавилось")
            isMelted = true
        }
    }
}
