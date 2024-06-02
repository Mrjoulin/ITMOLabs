package su.arlet.business1.security.services

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import su.arlet.business1.core.User

class AuthUsersDetails(
    val id: Long,
    private val username: String,
    @JsonIgnore
    private val password: String,
    private val authorities: Collection<GrantedAuthority?>,
) : UserDetails {
    companion object {
        const val ROLE_PREFIX = "ROLE_"

        fun build(user: User): AuthUsersDetails {
            return AuthUsersDetails(
                user.id,
                user.username,
                user.passwordHash,
                listOf(
                    SimpleGrantedAuthority(ROLE_PREFIX + user.role.name)
                )
            )
        }
    }

    override fun getAuthorities(): Collection<GrantedAuthority?> = authorities

    override fun getPassword(): String = password

    override fun getUsername(): String = username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val user = other as AuthUsersDetails
        return id == user.id && username == user.username
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + authorities.hashCode()
        return result
    }
}
