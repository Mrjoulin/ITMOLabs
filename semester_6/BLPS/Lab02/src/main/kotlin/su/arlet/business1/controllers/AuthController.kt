package su.arlet.business1.controllers


import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import su.arlet.business1.services.UserService


@RestController
@RequestMapping("\${api.path}/auth")
@Tag(name = "Auth API")
class AuthController(
    val userService: UserService,
) {
    @PostMapping("/register")
    @Operation(summary = "Create a new user")
    @ApiResponse(
        responseCode = "201", description = "Created user id", content = [
            Content(schema = Schema(implementation = UserService.AuthorizedUserCredentials::class))
        ]
    )
    @ApiResponse(
        responseCode = "400", description = "Bad body", content = [
            Content(schema = Schema(implementation = String::class)),
        ]
    )
    @ApiResponse(responseCode = "404", description = "Not found - user not found", content = [Content()])
    @ApiResponse(responseCode = "409", description = "User with given username already exists", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun createUser(
        @RequestBody authUserRequest: UserService.AuthUserRequest,
    ): ResponseEntity<*> {
        authUserRequest.validate()

        val authorizedUserCredentials = userService.register(authUserRequest)

        return ResponseEntity(authorizedUserCredentials, HttpStatus.CREATED)
    }

    @PostMapping("/login")
    @Operation(summary = "Login to existing user")
    @ApiResponse(
        responseCode = "200", description = "Logged in", content = [
            Content(schema = Schema(implementation = UserService.AuthorizedUserCredentials::class))
        ]
    )
    @ApiResponse(
        responseCode = "400", description = "Bad body", content = [
            Content(schema = Schema(implementation = String::class)),
        ]
    )
    @ApiResponse(responseCode = "404", description = "Not found - user not found", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun login(
        @RequestBody authUserRequest: UserService.AuthUserRequest,
    ): ResponseEntity<*> {
        authUserRequest.validate()

        val authorizedUserCredentials = userService.login(authUserRequest)

        return ResponseEntity(authorizedUserCredentials, HttpStatus.OK)
    }
}