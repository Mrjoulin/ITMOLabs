package su.arlet.business1.services

import jakarta.validation.constraints.NotBlank
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import su.arlet.business1.core.*
import su.arlet.business1.core.enums.AdPostStatus
import su.arlet.business1.core.enums.AdRequestStatus
import su.arlet.business1.core.enums.UserRole
import su.arlet.business1.exceptions.EntityNotFoundException
import su.arlet.business1.exceptions.PermissionDeniedException
import su.arlet.business1.exceptions.UnsupportedStatusChangeException
import su.arlet.business1.exceptions.ValidationException
import su.arlet.business1.repos.*
import su.arlet.business1.security.services.AuthUserService
import java.time.LocalDateTime
import java.util.*
import kotlin.jvm.optionals.getOrElse

@Service
class AdPostService @Autowired constructor(
    private val adRequestService: AdRequestService,
    private val adPostRepo: AdPostRepo,
    private val adRequestRepo: AdRequestRepo,
    private val imageRepo: ImageRepo,
    private val userRepo: UserRepo,
    private val authUserService: AuthUserService,
) {
    @Throws(EntityNotFoundException::class, ValidationException::class)
    fun createAdPost(createAdPost: CreateAdPost): Long {
        val adRequest = adRequestRepo.findById(createAdPost.adRequestId).getOrElse {
            throw EntityNotFoundException("Ad Request")
        }
        val image = createAdPost.imageId?.let {
            imageRepo.findById(it).getOrElse { throw EntityNotFoundException("Image") }
        }

        val salesEditorId = authUserService.getUserId()
        val salesEditor = userRepo.findById(salesEditorId).getOrElse { throw EntityNotFoundException("Sales Editor") }

        val adPost = AdPost(
            title = createAdPost.title ?: throw ValidationException("title must be provided"),
            body = createAdPost.body ?: throw ValidationException("body must be provided"),
            targetLink = createAdPost.targetLink ?: throw ValidationException("target link must be provided"),
            salesEditor = salesEditor,
            image = image,
            adRequest = adRequest,
            status = AdPostStatus.SAVED
        )

        val adPostId = adPostRepo.save(adPost).id

        return adPostId
    }

    @Throws(EntityNotFoundException::class)
    fun updateAdPost(adPostId: Long, updateAdPost: UpdateAdPost) {
        val adPost = adPostRepo.findById(adPostId).getOrElse {
            throw EntityNotFoundException("Ad Post")
        }

        updateAdPostFields(adPost, updateAdPost)

        adPostRepo.save(adPost)
    }

    private fun updateAdPostFields(adPost: AdPost, updateAdPost: UpdateAdPost) {
        updateAdPost.title?.let { adPost.title = it }
        updateAdPost.body?.let { adPost.body = it }
        updateAdPost.targetLink?.let { adPost.targetLink = it }
        updateAdPost.imageId?.let {
            adPost.image = imageRepo.findById(it).getOrElse {
                throw EntityNotFoundException("Image")
            }
        }
    }

    @Throws(EntityNotFoundException::class, ValidationException::class, UnsupportedStatusChangeException::class)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun updateAdPostStatus(adPostId: Long, updateStatus: UpdateAdPostStatus) {
        val newStatus = try {
            AdPostStatus.valueOf(updateStatus.status.uppercase(Locale.getDefault()))
        } catch (e: IllegalArgumentException) {
            throw ValidationException("unknown status")
        }

        updateAdPostStatus(adPostId, newStatus)
    }

    @Throws(EntityNotFoundException::class, ValidationException::class, UnsupportedStatusChangeException::class)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun updateAdPostStatus(adPostId: Long, newStatus: AdPostStatus) {
        val adPost = adPostRepo.findById(adPostId).getOrElse {
            throw EntityNotFoundException()
        }
        if (adPost.status == newStatus) return

        val isSales = authUserService.hasRole(UserRole.SALES)

        when (newStatus) {
            AdPostStatus.SAVED ->
                throw UnsupportedStatusChangeException()

            AdPostStatus.READY_TO_PUBLISH -> {
                if (adPost.status != AdPostStatus.SAVED)
                    throw UnsupportedStatusChangeException()
                if (!isSales) throw PermissionDeniedException("ad post status")
                adRequestService.updateAdRequestStatus(adPost.adRequest.id, AdRequestStatus.READY_TO_PUBLISH)
            }

            AdPostStatus.PUBLISHED -> {
                if (adPost.status != AdPostStatus.READY_TO_PUBLISH)
                    throw UnsupportedStatusChangeException()

                adPost.publishDate = LocalDateTime.now()
                adRequestService.updateAdRequestStatus(adPost.adRequest.id, AdRequestStatus.PUBLISHED)
            }

            AdPostStatus.EXPIRED -> {}
        }

        adPost.status = newStatus

        adPostRepo.save(adPost)
    }

    @Throws(EntityNotFoundException::class)
    fun deleteAdPost(adPostId: Long) {
        if (adPostRepo.findById(adPostId).isPresent)
            adPostRepo.deleteById(adPostId)
        else
            throw EntityNotFoundException()
    }

    @Throws(EntityNotFoundException::class)
    @Transactional
    fun getAdPost(adPostId: Long): AdPost {
        val adPost = adPostRepo.findById(adPostId).getOrElse {
            throw EntityNotFoundException()
        }

        if (!authUserService.hasRole(UserRole.SALES) && adPost.status == AdPostStatus.SAVED)
            throw PermissionDeniedException("ad post")

        return adPost
    }

    fun getAdPosts(status: String?): List<AdPost> {
        val isSales = authUserService.hasRole(UserRole.SALES)

        if (status == null && !isSales)
            throw ValidationException("status must be provided")
        if (status == null)
            return adPostRepo.findAll()

        return try {
            val adPostStatus = AdPostStatus.valueOf(status.uppercase(Locale.getDefault()))

            if (isSales || adPostStatus != AdPostStatus.SAVED)
                adPostRepo.findAllByStatus(adPostStatus)
            else
                throw PermissionDeniedException("saved ad posts")
        } catch (_: IllegalArgumentException) {
            if (isSales) adPostRepo.findAll()
            else throw ValidationException("status incorrect")
        }
    }

    data class CreateAdPost(
        @NotBlank var title: String?,
        @NotBlank var body: String?,
        @NotBlank var targetLink: String?,
        var imageId: Long?,
        val adRequestId: Long,
    ) {
        @Throws(ValidationException::class)
        fun validate() {
            if (title == null)
                throw ValidationException("title must be provided")
            if (body == null)
                throw ValidationException("body must be provided")
            if (targetLink == null)
                throw ValidationException("target link must be provided")
            if (title == "")
                throw ValidationException("title must be not empty")
            if (body == "")
                throw ValidationException("body must be not empty")
            if (targetLink == "")
                throw ValidationException("target link must be not empty")
        }
    }

    data class UpdateAdPost(
        var title: String?,
        var body: String?,
        var targetLink: String?,
        var imageId: Long?,
    ) {
        @Throws(ValidationException::class)
        fun validate() {
            if (title != null && title == "")
                throw ValidationException("title must be not empty")
            if (body != null && body == "")
                throw ValidationException("body must be not empty")
            if (targetLink != null && targetLink == "")
                throw ValidationException("target link must be not empty")
        }
    }

    data class UpdateAdPostStatus(
        val status: String,
    ) {
        @Throws(ValidationException::class)
        fun validate() {
            if (status.isEmpty())
                throw ValidationException("status must be not empty")
        }
    }
}