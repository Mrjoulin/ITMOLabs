package su.arlet.business1.security.services

import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

import su.arlet.business1.exceptions.UserNotFoundException
import su.arlet.business1.repos.UserRepo

@Service
class AuthUsersDetailsService(
    private val userRepo: UserRepo,
) : UserDetailsService {
    @Throws(UserNotFoundException::class)
    override fun loadUserByUsername(username: String): AuthUsersDetails {
        val user = userRepo.findByUsername(username)

        if (user.isEmpty) throw UsernameNotFoundException("User Not Found with username: $username")

        return AuthUsersDetails.build(user.get())
    }
}