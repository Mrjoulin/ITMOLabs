package su.arlet.business1.repos

import org.springframework.data.jpa.repository.JpaRepository
import su.arlet.business1.core.User

interface UserRepo : JpaRepository<User, Long>