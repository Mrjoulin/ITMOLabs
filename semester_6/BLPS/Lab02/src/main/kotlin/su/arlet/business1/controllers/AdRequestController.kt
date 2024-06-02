package su.arlet.business1.controllers


import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import su.arlet.business1.core.AdRequest
import su.arlet.business1.security.services.AuthUserService
import su.arlet.business1.services.AdRequestService

@RestController
@RequestMapping("\${api.path}/ad/requests")
@Tag(name = "Ad requests API")
class AdRequestController(
    val adRequestService: AdRequestService,
    val authUserService: AuthUserService,
) {
    @GetMapping("/")
    @Operation(summary = "Get ad requests by filters")
    @ApiResponse(
        responseCode = "200", description = "OK", content = [
            Content(array = ArraySchema(items = Schema(implementation = AdRequest::class)))
        ]
    )
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun getAdRequests(
        @RequestParam(name = "ownerId", required = false) ownerId: Long?,
        @RequestParam(name = "status", required = false) status: String?,
        request: HttpServletRequest,
    ): ResponseEntity<*> {
        val adRequests = adRequestService.getAdRequests(ownerId, status)
        return ResponseEntity(adRequests, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ad request by ID")
    @ApiResponse(
        responseCode = "200", description = "Success - found ad request", content = [
            Content(schema = Schema(implementation = AdRequest::class))
        ]
    )
    @ApiResponse(responseCode = "404", description = "Not found - ad request not found", content = [])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun getAdRequestById(@PathVariable id: Long): ResponseEntity<*> {
        val adRequest = adRequestService.getAdRequest(adRequestId = id)
        return ResponseEntity(adRequest, HttpStatus.OK)
    }

    @PostMapping("/")
    @Operation(summary = "Create a new ad request")
    @ApiResponse(
        responseCode = "201", description = "Created ad request id", content = [
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
    fun createAdRequest(
        @RequestBody createAdRequest: AdRequestService.CreateAdRequest,
    ): ResponseEntity<*> {
        createAdRequest.validate()
        val adRequestId = adRequestService.createAdRequest(createAdRequest)
        return ResponseEntity(adRequestId, HttpStatus.CREATED)
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update ad request info")
    @ApiResponse(responseCode = "200", description = "Success - updated ad request", content = [Content()])
    @ApiResponse(
        responseCode = "400", description = "Bad body", content = [
            Content(schema = Schema(implementation = String::class))
        ]
    )
    @ApiResponse(responseCode = "404", description = "Not found - ad request not found", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun updateAdRequest(
        @PathVariable id: Long,
        @RequestBody updateAdRequest: AdRequestService.UpdateAdRequest,
    ): ResponseEntity<*> {
        updateAdRequest.validate()
        adRequestService.updateAdRequest(adRequestId = id, updateAdRequest = updateAdRequest)
        return ResponseEntity.ok(null)
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update ad request status")
    @ApiResponse(responseCode = "200", description = "Updated ad request status", content = [Content()])
    @ApiResponse(responseCode = "404", description = "Not found - user not found", content = [Content()])
    @ApiResponse(responseCode = "409", description = "Invalid status change", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun updateAdRequestStatus(
        @PathVariable id: Long,
        @RequestBody newStatus: AdRequestService.UpdateAdRequestStatus,
    ): ResponseEntity<*> {
        newStatus.validate()
        adRequestService.updateAdRequestStatus(adRequestId = id, updateStatus = newStatus)
        return ResponseEntity(null, HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SALES')")
    @Operation(summary = "Delete ad request")
    @ApiResponse(responseCode = "200", description = "Success - deleted ad request", content = [Content()])
    @ApiResponse(responseCode = "204", description = "No content", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun deleteAdRequest(@PathVariable id: Long): ResponseEntity<*> {
        adRequestService.deleteAdRequest(adRequestId = id)
        return ResponseEntity.ok(null)
    }
}