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
import su.arlet.business1.core.Image
import su.arlet.business1.exceptions.EntityNotFoundException
import su.arlet.business1.exceptions.ValidationException
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
        return try {
            val images = imageService.getImages(link, alias)
            ResponseEntity(images, HttpStatus.OK)
        } catch (e: Exception) {
            println("Error in get images: ${e.message}")
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
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
        return try {
            ResponseEntity(imageService.getImage(id), HttpStatus.OK)
        } catch (e: EntityNotFoundException) {
            ResponseEntity("not found", HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            println("Error in get image by id: ${e.message}")
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping("/")
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

        return try {
            createImageRequest.validate()
            val imageId = imageService.addImage(createImageRequest)
            ResponseEntity(imageId, HttpStatus.CREATED)
        } catch (e: ValidationException) {
            return ResponseEntity("Bad body: ${e.message}", HttpStatus.BAD_REQUEST)
        } catch (e: Exception) {
            println("Error in create image: ${e.message}")
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PatchMapping("/{id}")
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
        return try {
            updateImageRequest.validate()
            imageService.updateImage(id, updateImageRequest)
            ResponseEntity(null, HttpStatus.OK)
        } catch (e: EntityNotFoundException) {
            ResponseEntity(null, HttpStatus.NOT_FOUND)
        } catch (e: ValidationException) {
            return ResponseEntity("Bad body: ${e.message}", HttpStatus.BAD_REQUEST)
        } catch (e: Exception) {
            println("Error in update image: ${e.message}")
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete image")
    @ApiResponse(responseCode = "200", description = "Success - deleted image", content = [Content()])
    @ApiResponse(responseCode = "204", description = "No content", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun deleteImage(@PathVariable id: Long): ResponseEntity<*> {
        return try {
            imageService.deleteImage(id)
            ResponseEntity(null, HttpStatus.OK)
        } catch (e: EntityNotFoundException) {
            ResponseEntity(null, HttpStatus.NO_CONTENT)
        } catch (e: Exception) {
            println("Error in delete image: ${e.message}")
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }

    }
}