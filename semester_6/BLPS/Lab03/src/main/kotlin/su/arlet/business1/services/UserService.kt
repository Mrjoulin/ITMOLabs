package su.arlet.business1.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import su.arlet.business1.core.User
import su.arlet.business1.core.enums.UserRole
import su.arlet.business1.exceptions.UserAlreadyExistsException
import su.arlet.business1.exceptions.UserNotFoundException
import su.arlet.business1.exceptions.ValidationException
import su.arlet.business1.repos.UserRepo
import su.arlet.business1.security.jwt.JwtUtils
import su.arlet.business1.security.services.AuthUserService
import java.util.*
import kotlin.jvm.optionals.getOrElse


@Service
class UserService @Autowired constructor(
    private val userRepo: UserRepo,
    private val authUserService: AuthUserService,
    private val encoder: PasswordEncoder,
    private val jwtUtils: JwtUtils,
    private val authenticationManager: AuthenticationManager,
) {
    private val minUsernameLength = 4
    private val minPasswordLength = 4

    data class AuthorizedUserCredentials(
        val username: String,
        val token: String,
    )

    @Throws(ValidationException::class)
    fun login(authUserRequest: AuthUserRequest): AuthorizedUserCredentials {
        val authentication =
            try {
                authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(authUserRequest.username, authUserRequest.password)
                )
            } catch (e: AuthenticationException) {
                e.printStackTrace()
                throw UserNotFoundException()
            }
        SecurityContextHolder.getContext().authentication = authentication
        val jwtToken = jwtUtils.generateJwtToken(authentication.principal as String)

        return AuthorizedUserCredentials(authUserRequest.username!!, jwtToken)
    }

    @Throws(UserAlreadyExistsException::class, ValidationException::class)
    fun register(authUserRequest: AuthUserRequest): AuthorizedUserCredentials {
        if (authUserRequest.username!!.length < minUsernameLength)
            throw ValidationException("username is too short")
        if (authUserRequest.password!!.length < minPasswordLength)
            throw ValidationException("password is too short")
        if (userRepo.findByUsername(authUserRequest.username).isPresent)
            throw UserAlreadyExistsException()

        val user = User(
            name = authUserRequest.name,
            username = authUserRequest.username,
            passwordHash = hashPassword(authUserRequest.password),
            role = UserRole.DEFAULT
        )

        userRepo.save(user)

        return login(authUserRequest)
    }

    @Throws(UserNotFoundException::class)
    fun updateUser(updateUserRequest: UpdateUserRequest) {
        val userId = authUserService.getUserId()

        val user = userRepo.findById(userId).getOrElse {
            throw UserNotFoundException()
        }

        updateUserFields(user, updateUserRequest)

        userRepo.save(user)
    }

    private fun updateUserFields(user: User, updateUserRequest: UpdateUserRequest) {
        updateUserRequest.name?.let { user.name = it }
        updateUserRequest.email?.let { user.email = it }
        updateUserRequest.password?.let { user.passwordHash = hashPassword(it) }
    }

    @Throws(UserNotFoundException::class)
    fun updateUserRole(updateUserRoleRequest: UpdateUserRoleRequest) {
        var userSearchResult =
            if (updateUserRoleRequest.userId != null)
                userRepo.findById(updateUserRoleRequest.userId)
            else Optional.empty()

        if (userSearchResult.isEmpty)
            userSearchResult = if (updateUserRoleRequest.username != null)
                userRepo.findByUsername(updateUserRoleRequest.username)
            else Optional.empty()

        val user = userSearchResult.getOrElse { throw UserNotFoundException() }

        if (user.role != UserRole.ADMIN)
            user.role = UserRole.valueOf(updateUserRoleRequest.newRole!!)
        else throw ValidationException("you can't change admins role")

        userRepo.save(user)
    }

    @Throws(UserNotFoundException::class)
    fun deleteUser() {
        val userId = authUserService.getUserId()

        if (userRepo.findById(userId).isPresent)
            userRepo.deleteById(userId)
        else
            throw UserNotFoundException()
    }

    @Throws(UserNotFoundException::class)
    fun getUser(): User {
        val userId = authUserService.getUserId()

        return userRepo.findById(userId).getOrElse {
            throw UserNotFoundException()
        }
    }

    fun getUsers(): List<User> {
        return userRepo.findAll()
    }

    fun hashPassword(password: String?): String {
        if (password == null)
            throw ValidationException("password must be provided")

        return encoder.encode(password)
    }

    data class AuthUserRequest(
        val name: String?,
        val username: String?,
        var password: String?,
    ) {
        @Throws(ValidationException::class)
        fun validate() {
            if (username == null)
                throw ValidationException("login must be provided")
            if (password == null)
                throw ValidationException("password must be provided")
            if (username == "")
                throw ValidationException("login must be not empty")
            if (password == "")
                throw ValidationException("password must be not empty")
        }
    }

    data class UpdateUserRequest(
        val name: String?,
        val email: String?,
        var password: String?,
    ) {
        @Throws(ValidationException::class)
        fun validate() {
            if (password != null && password == "")
                throw ValidationException("password must be not empty")
        }
    }

    data class UpdateUserRoleRequest(
        val userId: Long?,
        val username: String?,
        val newRole: String?,
    ) {
        @Throws(ValidationException::class)
        fun validate() {
            if (userId == null && username == null)
                throw ValidationException("userId or username must be provided")
            if (userId != null && userId < 0)
                throw ValidationException("userId  must be positive")
            if (username.isNullOrEmpty())
                throw ValidationException("username can't be empty")
            if (newRole.isNullOrEmpty())
                throw ValidationException("new role can't be empty")

            try {
                UserRole.valueOf(newRole)
            } catch (e: IllegalArgumentException) {
                throw ValidationException("unknown role type: $newRole")
            }
        }
    }
}