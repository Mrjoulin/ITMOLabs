package su.arlet.business1.core

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import su.arlet.business1.core.enums.UserRole

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var name: String?,
    val login: String,
    @JsonIgnore
    var passwordHash: String,

    @Enumerated(EnumType.STRING)
    val role: UserRole,
)