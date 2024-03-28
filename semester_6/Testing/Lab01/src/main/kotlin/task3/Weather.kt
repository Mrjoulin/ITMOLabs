package task3

class Weather {
    var temperatureLevel: Level = Level.NORMAL
        set(value) {
            println("Уровень температуры установлен на ${value.lvl}")
            field = value
        }
    var noiseLevel: Level = Level.NORMAL
        set(value) {
            println("Уровень шума установлен на ${value.lvl}")
            field = value
        }
}