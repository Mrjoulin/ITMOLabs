package entities

import entities.validators.annotations.UniqueId
import entities.validators.annotations.ValueIntSize
import network.*
import java.io.Serializable
import java.sql.Date

data class User (
    @UniqueId
    @ValueIntSize(greater_than = MIN_USERNAME_LENGTH - 1, lower_than = MAX_USERNAME_LENGTH + 1)
    val username: String,

    val password: String,

    @ValueIntSize(lower_than = 256)
    val token: String,

    val tokenExpires: Date = Date(java.util.Date().time + TOKEN_EXPIRES_MILS)
) : Serializable {
    constructor(mapData: Map<String, Any?>): this(
        mapData["username"] as? String? ?: throw IllegalArgumentException("username"),
        mapData["password"] as? String? ?: throw IllegalArgumentException("password"),
        mapData["token"] as? String? ?: throw IllegalArgumentException("token"),
        mapData["tokenExpires"] as? Date? ?: throw IllegalArgumentException("tokenExpires"),
    )

    override fun toString(): String {
        return "User{username=$username, password=$password, token=${token.take(20)}..., tokenExpires=$tokenExpires}"
    }
}
