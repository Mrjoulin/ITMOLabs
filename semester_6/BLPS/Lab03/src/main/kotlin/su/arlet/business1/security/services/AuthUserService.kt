package su.arlet.business1.security.services

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import su.arlet.business1.core.enums.UserRole
import su.arlet.business1.security.jwt.AuthTokenFilter

@Service
class AuthUserService(
    private val authTokenFilter: AuthTokenFilter,
) {
    fun hasRole(role: UserRole): Boolean {
        return getUserDetails().authorities.any {
            it?.authority == AuthUsersDetails.ROLE_PREFIX + role.name ||
                    it?.authority == AuthUsersDetails.ROLE_PREFIX + UserRole.ADMIN
        }
    }

    fun getUserId(): Long {
        return getUserDetails().id
    }

    fun getUserDetails(): AuthUsersDetails {
        return SecurityContextHolder
            .getContext()
            .authentication
            .principal as AuthUsersDetails
    }
}