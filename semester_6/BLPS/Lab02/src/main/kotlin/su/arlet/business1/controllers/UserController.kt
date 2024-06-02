package su.arlet.business1.controllers


import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import su.arlet.business1.core.User
import su.arlet.business1.core.enums.UserRole
import su.arlet.business1.security.services.AuthUserService
import su.arlet.business1.services.UserService


@RestController
@RequestMapping("\${api.path}/users")
@Tag(name = "Users API")
class UserController(
    val userService: UserService,
    val authUserService: AuthUserService,
) {
    data class UserEntity(
        val id: Long,
        val name: String?,
        val username: String,
        val role: UserRole,
    )

    @GetMapping("/")
    @Operation(summary = "Get current user")
    @ApiResponse(
        responseCode = "200", description = "Success - found user", content = [
            Content(schema = Schema(implementation = User::class))
        ]
    )
    @ApiResponse(responseCode = "404", description = "Not found - user not found", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun getUserById(): ResponseEntity<*> {
        val user = userService.getUser()

        return ResponseEntity(
            UserEntity(
                id = user.id,
                name = user.name,
                username = user.username,
                role = user.role,
            ),
            HttpStatus.OK,
        )
    }

    @PatchMapping("/")
    @Operation(summary = "Update user info")
    @ApiResponse(responseCode = "200", description = "Success - updated user", content = [Content()])
    @ApiResponse(
        responseCode = "400", description = "Bad body", content = [
            Content(schema = Schema(implementation = String::class)),
        ]
    )
    @ApiResponse(responseCode = "404", description = "Not found - user not found", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun updateUser(
        @RequestBody updateUserRequest: UserService.UpdateUserRequest,
        request: HttpServletRequest,
    ): ResponseEntity<*> {
        updateUserRequest.validate()

        userService.updateUser(updateUserRequest = updateUserRequest)

        return ResponseEntity.ok(null)
    }

    @PutMapping("/role")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update user role (only for admins)")
    @ApiResponse(responseCode = "200", description = "Success - updated user role", content = [Content()])
    @ApiResponse(
        responseCode = "400", description = "Bad body", content = [
            Content(schema = Schema(implementation = String::class)),
        ]
    )
    @ApiResponse(responseCode = "404", description = "Not found - user not found", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun updateUserRole(
        @RequestBody updateUserRoleRequest: UserService.UpdateUserRoleRequest,
    ): ResponseEntity<*> {
        updateUserRoleRequest.validate()

        userService.updateUserRole(updateUserRoleRequest)

        return ResponseEntity.ok(null)
    }

    @DeleteMapping("/")
    @Operation(summary = "Delete user")
    @ApiResponse(responseCode = "200", description = "Success - deleted user", content = [Content()])
    @ApiResponse(responseCode = "204", description = "No content", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun deleteUser(): ResponseEntity<*> {
        userService.deleteUser()

        return ResponseEntity.ok(null)
    }
}