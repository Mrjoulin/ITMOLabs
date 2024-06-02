package org.joulin.repos

import org.springframework.data.jpa.repository.JpaRepository
import org.joulin.core.User
import org.joulin.core.enums.UserRole
import java.util.*

interface UserRepo : JpaRepository<User, Long> {
    fun findByUsername(username: String): Optional<User>
    fun findAllByRole(role: UserRole): List<User>
}