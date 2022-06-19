package utils

import javafx.scene.paint.Color


enum class UserColor(val color: Color) {
    MAROON(Color.MAROON),
    BROWN(Color.BROWN),
    OLIVE(Color.OLIVE),
    TEAL(Color.TEAL),
    NAVY(Color.NAVY),
    BLACK(Color.BLACK),
    RED(Color.RED.brighter()),
    ORANGE(Color.ORANGE),
    YELLOW_GREEN(Color.YELLOWGREEN),
    LIME(Color.LIME),
    GREEN(Color.GREEN),
    CYAN(Color.CYAN),
    BLUE(Color.BLUE.brighter()),
    PURPLE(Color.PURPLE),
    MAGENTA(Color.MAGENTA),
    PINK(Color.PINK),
    GRAY(Color.GREY),
    MINT(Color.MEDIUMSPRINGGREEN),
    KHAKI(Color.KHAKI);

    companion object {
        private val colors: Array<UserColor> = values()
        private var randomSeq: MutableList<Int> = initSeq()

        private fun initSeq(): MutableList<Int> = colors.indices.shuffled().toMutableList()

        @Synchronized fun randomColor(): UserColor {
            if (randomSeq.isEmpty()) randomSeq = initSeq()

            val nextColorIndex = randomSeq.removeAt(0)

            return colors[nextColorIndex]
        }
    }
}