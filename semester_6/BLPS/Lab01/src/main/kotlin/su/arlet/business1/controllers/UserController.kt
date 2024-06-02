package su.arlet.business1.controllers


import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import su.arlet.business1.core.User
import su.arlet.business1.core.enums.UserRole
import su.arlet.business1.exceptions.EntityNotFoundException
import su.arlet.business1.exceptions.ValidationException
import su.arlet.business1.services.UserService


@RestController
@RequestMapping("\${api.path}/users")
@Tag(name = "Users API")
class UserController(
    val userService: UserService,
) {
    data class UserEntity(
        val id: Long,
        val name: String?,
        val login: String,
        val role: UserRole,
    )

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    @ApiResponse(
        responseCode = "200", description = "Success - found user", content = [
            Content(schema = Schema(implementation = User::class))
        ]
    )
    @ApiResponse(responseCode = "404", description = "Not found - user not found", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun getUserById(@PathVariable id: Long): ResponseEntity<*> {
        return try {
            val user = userService.getUser(userId = id)

            ResponseEntity(
                UserEntity(
                    id = user.id,
                    name = user.name,
                    login = user.login,
                    role = user.role,
                ),
                HttpStatus.OK,
            )
        } catch (_: EntityNotFoundException) {
            ResponseEntity("Not found", HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            println("Error in get user by id: ${e.message}")
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping("/")
    @Operation(summary = "Create a new user")
    @ApiResponse(
        responseCode = "201", description = "Created user id", content = [
            Content(schema = Schema(implementation = Long::class))
        ]
    )
    @ApiResponse(
        responseCode = "400", description = "Bad body", content = [
            Content(schema = Schema(implementation = String::class)),
        ]
    )
    @ApiResponse(responseCode = "404", description = "Not found - user not found", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun createUser(
        @RequestBody createUserRequest: UserService.CreateUserRequest,
    ): ResponseEntity<*> {

        try {
            createUserRequest.validate()
        } catch (e: ValidationException) {
            return ResponseEntity("Bad body: ${e.message}", HttpStatus.BAD_REQUEST)
        }

        return try {
            val adRequestId = userService.createUser(createUserRequest)
            ResponseEntity(adRequestId, HttpStatus.CREATED)
        } catch (_: EntityNotFoundException) {
            ResponseEntity("Not found", HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            println("Error in add user: ${e.message}")
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PatchMapping("/{id}")
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
        @PathVariable id: Long,
        @RequestBody updateUserRequest: UserService.UpdateUserRequest,
    ): ResponseEntity<*> {

        return try {
            updateUserRequest.validate()
            userService.updateUser(userId = id, updateUserRequest = updateUserRequest)
            ResponseEntity.ok(null)
        } catch (_: EntityNotFoundException) {
            ResponseEntity("Not found", HttpStatus.NOT_FOUND)
        } catch (e: ValidationException) {
            return ResponseEntity("Bad body: ${e.message}", HttpStatus.BAD_REQUEST)
        } catch (e: Exception) {
            println("Error in update user: ${e.message}")
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    @ApiResponse(responseCode = "200", description = "Success - deleted user", content = [Content()])
    @ApiResponse(responseCode = "204", description = "No content", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun deleteUser(@PathVariable id: Long): ResponseEntity<*> {
        return try {
            userService.deleteUser(userId = id)
            ResponseEntity.ok(null)
        } catch (_: EntityNotFoundException) {
            ResponseEntity(null, HttpStatus.NO_CONTENT)
        } catch (e: Exception) {
            println("Error in delete user: ${e.message}")
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}