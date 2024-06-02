package su.arlet.business1.controllers


import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import su.arlet.business1.core.AdPost
import su.arlet.business1.exceptions.EntityNotFoundException
import su.arlet.business1.exceptions.ValidationException
import su.arlet.business1.services.AdPostService

@RestController
@RequestMapping("\${api.path}/ad/posts")
@Tag(name = "Ad posts API")
class AdPostController(
    val adPostService: AdPostService,
) {
    @GetMapping("/")
    @Operation(summary = "Get ad posts by filters")
    @ApiResponse(
        responseCode = "200", description = "OK", content = [
            Content(array = ArraySchema(items = Schema(implementation = AdPost::class)))
        ]
    )
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun getAdPosts(
        @RequestParam(name = "status", required = false) status: String?,
    ): ResponseEntity<*> {
        return try {
            val adRequests = adPostService.getAdPosts(status)
            ResponseEntity(adRequests, HttpStatus.OK)
        } catch (e: Exception) {
            println("Error in get ad posts: ${e.message}")
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ad post by ID")
    @ApiResponse(
        responseCode = "200", description = "Success - found ad post", content = [
            Content(schema = Schema(implementation = AdPost::class))
        ]
    )
    @ApiResponse(responseCode = "404", description = "Not found - ad post not found", content = [])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun getAdPostById(@PathVariable id: Long): ResponseEntity<*> {
        return try {
            val adRequest = adPostService.getAdPost(adPostId = id)
            ResponseEntity(adRequest, HttpStatus.OK)
        } catch (_: EntityNotFoundException) {
            ResponseEntity("Not found", HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            println("Error in get ad post by id: ${e.message}")
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping("/")
    @Operation(summary = "Create a new ad post")
    @ApiResponse(
        responseCode = "201", description = "Created ad post id", content = [
            Content(schema = Schema(implementation = Long::class))
        ]
    )
    @ApiResponse(
        responseCode = "400", description = "Bad body", content = [
            Content(schema = Schema(implementation = String::class))
        ]
    )
    @ApiResponse(responseCode = "404", description = "Not found - user not found", content = [])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun createAdPost(
        @RequestBody createAdPost: AdPostService.CreateAdPost,
    ): ResponseEntity<*> {
        return try {
            createAdPost.validate()
            val adRequestId = adPostService.createAdPost(createAdPost)
            ResponseEntity(adRequestId, HttpStatus.CREATED)
        } catch (e: EntityNotFoundException) {
            ResponseEntity("Not found ${e.message ?: ""}", HttpStatus.NOT_FOUND)
        } catch (e: ValidationException) {
            return ResponseEntity("Bad body: ${e.message}", HttpStatus.BAD_REQUEST)
        } catch (e: Exception) {
            println("Error in create ad post: ${e.message}")
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update ad post info")
    @ApiResponse(responseCode = "200", description = "Success - updated ad post", content = [Content()])
    @ApiResponse(
        responseCode = "400", description = "Bad body", content = [
            Content(schema = Schema(implementation = String::class))
        ]
    )
    @ApiResponse(responseCode = "404", description = "Not found - ad post not found", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun updateAdPost(
        @PathVariable id: Long,
        @RequestBody updateAdPost: AdPostService.UpdateAdPost,
    ): ResponseEntity<*> {
        return try {
            updateAdPost.validate()
            adPostService.updateAdPost(adPostId = id, updateAdPost = updateAdPost)
            ResponseEntity.ok(null)
        } catch (e: EntityNotFoundException) {
            ResponseEntity("Not found ${e.message ?: ""}", HttpStatus.NOT_FOUND)
        } catch (e: ValidationException) {
            return ResponseEntity("Bad body: ${e.message}", HttpStatus.BAD_REQUEST)
        } catch (e: Exception) {
            println("Error in update ad post: ${e.message}")
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update ad post status")
    @ApiResponse(responseCode = "200", description = "Updated ad post status", content = [Content()])
    @ApiResponse(responseCode = "404", description = "Not found - ad post not found", content = [Content()])
    @ApiResponse(responseCode = "409", description = "Invalid status change", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun updateAdPostStatus(
        @PathVariable id: Long,
        @RequestBody newStatus: AdPostService.UpdateAdPostStatus,
    ): ResponseEntity<*> {
        return try {
            newStatus.validate()
            adPostService.updateAdPostStatus(adPostId = id, updateStatus = newStatus)
            ResponseEntity(null, HttpStatus.OK)
        } catch (_: EntityNotFoundException) {
            ResponseEntity("Not found", HttpStatus.NOT_FOUND)
        } catch (_: UnsupportedOperationException) {
            ResponseEntity("Unsupported status change", HttpStatus.CONFLICT)
        } catch (_: IllegalArgumentException) {
            ResponseEntity("Unknown status", HttpStatus.BAD_REQUEST)
        } catch (e: ValidationException) {
            return ResponseEntity("Bad body: ${e.message}", HttpStatus.BAD_REQUEST)
        } catch (e: Exception) {
            println("Error in update ad post: ${e.message}")
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete ad post")
    @ApiResponse(responseCode = "200", description = "Success - deleted ad post", content = [Content()])
    @ApiResponse(responseCode = "204", description = "No content", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun deleteAdPost(@PathVariable id: Long): ResponseEntity<*> {
        return try {
            adPostService.deleteAdPost(adPostId = id)
            ResponseEntity.ok(null)
        } catch (_: EntityNotFoundException) {
            ResponseEntity(null, HttpStatus.NO_CONTENT)
        } catch (e: Exception) {
            println("Error in get ad posts: ${e.message}")
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}