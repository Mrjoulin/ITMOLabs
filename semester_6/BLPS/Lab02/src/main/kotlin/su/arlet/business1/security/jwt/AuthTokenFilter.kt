package su.arlet.business1.security.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import su.arlet.business1.security.services.AuthUsersDetails
import su.arlet.business1.security.services.AuthUsersDetailsService
import java.io.IOException
import java.util.*

class AuthTokenFilter : OncePerRequestFilter() {
    @Autowired
    private lateinit var jwtUtils: JwtUtils

    @Autowired
    private lateinit var userDetailsService: AuthUsersDetailsService

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            val userDetails = getUserDetails(request)
            if (userDetails != null) {
                val authentication = UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.authorities
                )
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: Exception) {
            logger.error("Cannot set user authentication: ${e.message}")
        }
        filterChain.doFilter(request, response)
    }

    private fun getUserDetails(request: HttpServletRequest): AuthUsersDetails? {
        val jwt = parseJwt(request)

        if (jwt == null || !jwtUtils.verifyToken(jwt)) return null

        val username = jwtUtils.getUserNameFromJwtToken(jwt) ?: return null

        return userDetailsService.loadUserByUsername(username)
    }

    private fun parseJwt(request: HttpServletRequest): String? {
        val headerAuth = request.getHeader("Authorization")
        val bearerPrefix = "bearer "
        return if (StringUtils.hasText(headerAuth) && headerAuth.lowercase().startsWith(bearerPrefix))
            headerAuth.substring(bearerPrefix.length)
        else null
    }
}
