package su.arlet.business1.services

import jakarta.validation.constraints.NotEmpty
import org.springframework.stereotype.Service
import su.arlet.business1.core.Image
import su.arlet.business1.exceptions.EntityNotFoundException
import su.arlet.business1.exceptions.ValidationException
import su.arlet.business1.repos.ImageRepo
import kotlin.jvm.optionals.getOrNull

@Service
class ImageService(
    private val imageRepo: ImageRepo,
) {
    @Throws(ValidationException::class)
    fun addImage(createImageRequest: CreateImageRequest): Long {
        val imageId = imageRepo.save(
            Image(
                alias = createImageRequest.alias ?: throw ValidationException("alias must be provided"),
                link = createImageRequest.link ?: throw ValidationException("alias must be provided"),
            )
        ).id

        return imageId
    }

    @Throws(EntityNotFoundException::class)
    fun updateImage(id: Long, updateImageRequest: UpdateImageRequest) {
        val image = imageRepo.findById(id).getOrNull() ?: throw EntityNotFoundException()

        updateImageFields(image, updateImageRequest)
    }

    @Throws(EntityNotFoundException::class)
    fun deleteImage(id: Long) {
        if (imageRepo.findById(id).isPresent)
            imageRepo.deleteById(id)
        else
            throw EntityNotFoundException()
    }

    @Throws(EntityNotFoundException::class)
    fun getImage(id: Long): Image {
        return imageRepo.findById(id).getOrNull() ?: throw EntityNotFoundException()
    }

    fun getImages(link: String?, alias: String?): List<Image> {
        if (link != null && alias != null)
            return imageRepo.findAllByLinkAndAlias(alias, link)
        else if (link != null)
            return imageRepo.findAllByLink(link)
        else if (alias != null)
            return imageRepo.findAllByAlias(alias)

        return imageRepo.findAll()
    }

    data class CreateImageRequest(
        val alias: String?,
        val link: String?,
    ) {
        @Throws(ValidationException::class)
        fun validate() {
            if (alias == null)
                throw ValidationException("alias must be provided")
            if (link == null)
                throw ValidationException("link must be provided")
            if (alias == "")
                throw ValidationException("alias must be not empty")
            if (link == "")
                throw ValidationException("link must be not empty")
        }
    }

    data class UpdateImageRequest(
        @NotEmpty val alias: String?,
        @NotEmpty val link: String?,
    ) {
        @Throws(ValidationException::class)
        fun validate() {
            if (alias == "")
                throw ValidationException("alias must be not empty")
            if (link == "")
                throw ValidationException("link must be not empty")
        }
    }

    private fun updateImageFields(image: Image, updateImageRequest: UpdateImageRequest) {
        updateImageRequest.link?.let { image.link = it }
        updateImageRequest.alias?.let { image.alias = it }
    }

}