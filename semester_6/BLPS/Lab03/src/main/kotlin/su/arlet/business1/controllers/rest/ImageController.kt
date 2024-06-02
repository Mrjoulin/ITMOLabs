package su.arlet.business1.controllers.rest

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
import su.arlet.business1.core.Image
import su.arlet.business1.services.ImageService

@RestController
@RequestMapping("\${api.path}/images")
@Tag(name = "Image API")
class ImageController(
    val imageService: ImageService,
) {

    @GetMapping("/")
    @Operation(summary = "Get images by filters")
    @ApiResponse(
        responseCode = "200", description = "OK", content = [
            Content(array = ArraySchema(items = Schema(implementation = Image::class)))
        ]
    )
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun getImages(
        @RequestParam(name = "link", required = false) link: String?,
        @RequestParam(name = "alias", required = false) alias: String?,
    ): ResponseEntity<*> {
        val images = imageService.getImages(link, alias)
        return ResponseEntity(images, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get image by ID")
    @ApiResponse(
        responseCode = "200", description = "Success - found image", content = [
            Content(schema = Schema(implementation = Image::class))
        ]
    )
    @ApiResponse(responseCode = "404", description = "Not found - image not found", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun getImageById(@PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity(imageService.getImage(id), HttpStatus.OK)
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('JOURNALIST') || hasRole('SALES')")
    @Operation(summary = "Create a new image")
    @ApiResponse(
        responseCode = "201", description = "Created image", content = [
            Content(schema = Schema(implementation = Long::class))
        ]
    )
    @ApiResponse(
        responseCode = "400", description = "Bad body", content = [
            Content(schema = Schema(implementation = String::class)),
        ]
    )
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun createImage(
        @RequestBody createImageRequest: ImageService.CreateImageRequest,
    ): ResponseEntity<*> {
        createImageRequest.validate()
        val imageId = imageService.addImage(createImageRequest)
        return ResponseEntity(imageId, HttpStatus.CREATED)
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('JOURNALIST') || hasRole('SALES')")
    @Operation(summary = "Update image")
    @ApiResponse(responseCode = "200", description = "Success - updated image", content = [Content()])
    @ApiResponse(
        responseCode = "400", description = "Bad body", content = [
            Content(schema = Schema(implementation = String::class)),
        ]
    )
    @ApiResponse(responseCode = "404", description = "Not found - article not found", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun updateImage(
        @PathVariable id: Long,
        @RequestBody updateImageRequest: ImageService.UpdateImageRequest,
    ): ResponseEntity<*> {
        updateImageRequest.validate()
        imageService.updateImage(id, updateImageRequest)
        return ResponseEntity(null, HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('JOURNALIST') || hasRole('SALES')")
    @Operation(summary = "Delete image")
    @ApiResponse(responseCode = "200", description = "Success - deleted image", content = [Content()])
    @ApiResponse(responseCode = "204", description = "No content", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun deleteImage(@PathVariable id: Long): ResponseEntity<*> {
        imageService.deleteImage(id)
        return ResponseEntity(null, HttpStatus.OK)
    }
}