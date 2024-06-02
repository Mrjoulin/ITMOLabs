package su.arlet.business1.security.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import su.arlet.business1.exceptions.UnauthorizedError
import java.time.Instant
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtils {
    @Value("\${app.jwtSecret}")
    private var jwtSecret: String = ""

    @Value("\${app.jwtExpirationSec}")
    private var jwtExpirationSec = 60L

    private val logger = LoggerFactory.getLogger(JwtUtils::class.java)

    fun getKey(): SecretKey {
        return Keys.hmacShaKeyFor(jwtSecret.encodeToByteArray())
    }

    fun getUserNameFromJwtToken(token: String): String? {
        return parseToken(token).subject
    }

    fun verifyToken(token: String): Boolean {
        return try {
            parseToken(token)
            true
        } catch (e: UnauthorizedError) {
            false
        }
    }

    fun parseToken(token: String): Claims {
        try {
            return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).payload
        } catch (e: MalformedJwtException) {
            logger.error("Invalid JWT token: {}", e.message)
            throw UnauthorizedError("Invalid JWT token")
        } catch (e: ExpiredJwtException) {
            logger.error("JWT token is expired: {}", e.message)
            throw UnauthorizedError("JWT token is expired")
        } catch (e: UnsupportedJwtException) {
            logger.error("JWT token is unsupported: {}", e.message)
            throw UnauthorizedError("JWT token is unsupported")
        } catch (e: IllegalArgumentException) {
            logger.error("JWT claims string is empty: {}", e.message)
            throw UnauthorizedError("JWT claims string is empty")
        } catch (e: JwtException) {
            throw UnauthorizedError()
        }
    }

    fun generateJwtToken(subject: String): String {
        return Jwts.builder()
            .subject(subject)
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plusSeconds(jwtExpirationSec)))
            .signWith(getKey())
            .compact()
    }
}
