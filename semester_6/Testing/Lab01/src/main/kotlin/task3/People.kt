package task3

class People(name: String) : LocationObject(name) {
    var scaryWaiting: Boolean = false

    override fun checkState() {
        if (!scaryWaiting && dangerLevel >= Level.HIGH) {
            println("$name сгрудились плотнее и стали ждать конца")
            scaryWaiting = true
        } else if (scaryWaiting && dangerLevel == Level.NORMAL) {
            println("$name дождались конца")
            scaryWaiting = false
        }
    }
}