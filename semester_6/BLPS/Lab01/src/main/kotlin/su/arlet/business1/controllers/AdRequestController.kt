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
import su.arlet.business1.core.AdRequest
import su.arlet.business1.exceptions.EntityNotFoundException
import su.arlet.business1.exceptions.ValidationException
import su.arlet.business1.services.AdRequestService

@RestController
@RequestMapping("\${api.path}/ad/requests")
@Tag(name = "Ad requests API")
class AdRequestController(
    val adRequestService: AdRequestService,
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
    ): ResponseEntity<*> {
        return try {
            val adRequests = adRequestService.getAdRequests(ownerId, status)
            ResponseEntity(adRequests, HttpStatus.OK)
        } catch (e: Exception) {
            println("Error in get ad requests: ${e.message}")
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
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
        return try {
            val adRequest = adRequestService.getAdRequest(adRequestId = id)
            ResponseEntity(adRequest, HttpStatus.OK)
        } catch (_: EntityNotFoundException) {
            ResponseEntity("Not found", HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            println("Error in get ad request by id: ${e.message}")
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
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
        return try {
            createAdRequest.validate()
            val adRequestId = adRequestService.createAdRequest(createAdRequest)
            ResponseEntity(adRequestId, HttpStatus.CREATED)
        } catch (_: EntityNotFoundException) {
            ResponseEntity("Not found", HttpStatus.NOT_FOUND)
        } catch (e: ValidationException) {
            return ResponseEntity("Bad body: ${e.message}", HttpStatus.BAD_REQUEST)
        } catch (e: Exception) {
            println("Error in create ad request: ${e.message}")
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
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
        return try {
            updateAdRequest.validate()
            adRequestService.updateAdRequest(adRequestId = id, updateAdRequest = updateAdRequest)
            ResponseEntity.ok(null)
        } catch (_: EntityNotFoundException) {
            ResponseEntity("Not found", HttpStatus.NOT_FOUND)
        } catch (e: ValidationException) {
            return ResponseEntity("Bad body: ${e.message}", HttpStatus.BAD_REQUEST)
        } catch (e: Exception) {
            println("Error in update ad request: ${e.message}")
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
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
        return try {
            newStatus.validate()
            adRequestService.updateAdRequestStatus(adRequestId = id, updateStatus = newStatus)
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
            println("Error in update ad request: ${e.message}")
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete ad request")
    @ApiResponse(responseCode = "200", description = "Success - deleted ad request", content = [Content()])
    @ApiResponse(responseCode = "204", description = "No content", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun deleteAdRequest(@PathVariable id: Long): ResponseEntity<*> {
        return try {
            adRequestService.deleteAdRequest(adRequestId = id)
            ResponseEntity.ok(null)
        } catch (_: EntityNotFoundException) {
            ResponseEntity(null, HttpStatus.NO_CONTENT)
        } catch (e: Exception) {
            println("Error in get ad requests: ${e.message}")
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}