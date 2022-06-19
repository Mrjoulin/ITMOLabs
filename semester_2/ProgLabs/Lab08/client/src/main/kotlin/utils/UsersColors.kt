package utils

import javafx.scene.paint.Color


private val USERS_COLORS = HashMap<String, Color>()


fun getUserColor(username: String) : Color {
    if (USERS_COLORS.contains(username))
        return USERS_COLORS[username]!!

    val userColor = UserColor.randomColor()

    USERS_COLORS[username] = userColor.color

    logger.debug("Set user $username color: ${userColor.name}")

    return userColor.color
}