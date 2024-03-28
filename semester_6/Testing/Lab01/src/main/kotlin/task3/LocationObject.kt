package task3

abstract class LocationObject (val name: String) {
    var dangerLevel: Level = Level.NORMAL

    abstract fun checkState();
}