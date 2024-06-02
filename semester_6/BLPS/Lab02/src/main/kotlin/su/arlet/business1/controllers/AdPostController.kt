package su.arlet.business1.controllers


import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import su.arlet.business1.core.AdPost
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
        val adRequests = adPostService.getAdPosts(status)
        return ResponseEntity(adRequests, HttpStatus.OK)
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
        val adRequest = adPostService.getAdPost(adPostId = id)
        return ResponseEntity(adRequest, HttpStatus.OK)
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('SALES')")
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
        createAdPost.validate()
        val adRequestId = adPostService.createAdPost(createAdPost)
        return ResponseEntity(adRequestId, HttpStatus.CREATED)
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('SALES')")
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
        updateAdPost.validate()
        adPostService.updateAdPost(adPostId = id, updateAdPost = updateAdPost)
        return ResponseEntity.ok(null)
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
        newStatus.validate()
        adPostService.updateAdPostStatus(adPostId = id, updateStatus = newStatus)
        return ResponseEntity(null, HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SALES')")
    @Operation(summary = "Delete ad post")
    @ApiResponse(responseCode = "200", description = "Success - deleted ad post", content = [Content()])
    @ApiResponse(responseCode = "204", description = "No content", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun deleteAdPost(@PathVariable id: Long): ResponseEntity<*> {
        adPostService.deleteAdPost(adPostId = id)
        return ResponseEntity.ok(null)
    }
}