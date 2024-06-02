package su.arlet.business1.repos

import org.springframework.data.jpa.repository.JpaRepository
import su.arlet.business1.core.User
import java.util.*

interface UserRepo : JpaRepository<User, Long> {
    fun findByUsername(username: String): Optional<User>
}