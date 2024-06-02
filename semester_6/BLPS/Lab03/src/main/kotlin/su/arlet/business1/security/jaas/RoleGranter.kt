package su.arlet.business1.security.jaas

import org.springframework.security.authentication.jaas.AuthorityGranter
import org.springframework.security.core.userdetails.UserDetailsService
import java.security.Principal
import javax.security.auth.login.LoginException


class RoleGranter(
    private val usersDetailsService: UserDetailsService?
) : AuthorityGranter {
    override fun grant(principal: Principal?): MutableSet<String> {
        if (usersDetailsService == null) throw LoginException("No context")
        if (principal == null) return mutableSetOf()
        val username = principal.name

        val userDetails = usersDetailsService.loadUserByUsername(username)

        return userDetails.authorities.map { it.authority }.toMutableSet()
    }
}