package su.arlet.business1.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import su.arlet.business1.core.AdRequest
import su.arlet.business1.core.Auditory
import su.arlet.business1.core.enums.AdRequestStatus
import su.arlet.business1.core.enums.UserRole
import su.arlet.business1.exceptions.EntityNotFoundException
import su.arlet.business1.exceptions.PermissionDeniedException
import su.arlet.business1.exceptions.UnsupportedStatusChangeException
import su.arlet.business1.exceptions.ValidationException
import su.arlet.business1.repos.AdRequestRepo
import su.arlet.business1.repos.UserRepo
import su.arlet.business1.security.services.AuthUserService
import java.time.LocalDate
import java.time.format.DateTimeParseException
import java.util.*
import kotlin.jvm.optionals.getOrElse

@Service
class AdRequestService @Autowired constructor(
    private val adRequestRepo: AdRequestRepo,
    private val userRepo: UserRepo,
    private val authUserService: AuthUserService,
) {
    @Throws(EntityNotFoundException::class, ValidationException::class, DateTimeParseException::class)
    fun createAdRequest(createAdRequest: CreateAdRequest): Long {
        val ownerId = authUserService.getUserId()
        val owner = userRepo.findById(ownerId).getOrElse { throw EntityNotFoundException() }

        if (createAdRequest.lifeHours == null) {
            throw ValidationException("life hours must be provided")
        }

        val adRequest = AdRequest(
            owner = owner,
            auditory = Auditory(
                createAdRequest.ageSegments,
                createAdRequest.incomeSegments,
                createAdRequest.locations,
                createAdRequest.interests
            ),
            requestText = createAdRequest.requestText ?: throw ValidationException("owner id must be provided"),
            publishDeadline = LocalDate.parse(createAdRequest.publishDeadline),
            lifeHours = createAdRequest.lifeHours.toIntOrNull()
                ?: throw ValidationException("life hours must be integer"),
            status = AdRequestStatus.SAVED
        )

        val adRequestId = adRequestRepo.save(adRequest).id

        return adRequestId
    }

    @Throws(EntityNotFoundException::class, DateTimeParseException::class)
    fun updateAdRequest(adRequestId: Long, updateAdRequest: UpdateAdRequest) {
        val adRequest = adRequestRepo.findById(adRequestId).getOrElse {
            throw EntityNotFoundException()
        }

        val userId = authUserService.getUserId()
        if (!authUserService.hasRole(UserRole.SALES) && adRequest.owner.id != userId)
            throw PermissionDeniedException("ad request")

        updateAdRequestFields(adRequest, updateAdRequest)

        adRequestRepo.save(adRequest)
    }

    private fun updateAdRequestFields(adRequest: AdRequest, updateAdRequest: UpdateAdRequest) {
        updateAdRequest.requestText?.let { adRequest.requestText = it }
        updateAdRequest.ageSegments?.let {
            if (adRequest.auditory != null) adRequest.auditory!!.ageSegments = it
            else adRequest.auditory = Auditory(ageSegments = it)
        }
        updateAdRequest.incomeSegments?.let {
            if (adRequest.auditory != null) adRequest.auditory!!.incomeSegments = it
            else adRequest.auditory = Auditory(incomeSegments = it)
        }
        updateAdRequest.locations?.let {
            if (adRequest.auditory != null) adRequest.auditory!!.locations = it
            else adRequest.auditory = Auditory(locations = it)
        }
        updateAdRequest.interests?.let {
            if (adRequest.auditory != null) adRequest.auditory!!.interests = it
            else adRequest.auditory = Auditory(interests = it)
        }
        updateAdRequest.publishDeadline?.let { adRequest.publishDeadline = LocalDate.parse(it) }
        updateAdRequest.lifeHours?.let {
            adRequest.lifeHours = it.toIntOrNull() ?: throw ValidationException("life hours must be integer")
        }
        updateAdRequest.clarificationText?.let { adRequest.clarificationText = it }
    }

    @Throws(EntityNotFoundException::class, ValidationException::class, UnsupportedStatusChangeException::class)
    fun updateAdRequestStatus(adRequestId: Long, updateStatus: UpdateAdRequestStatus) {
        val adRequest = adRequestRepo.findById(adRequestId).getOrElse {
            throw EntityNotFoundException()
        }
        val newStatus = try {
            AdRequestStatus.valueOf(updateStatus.status)
        } catch (e: IllegalArgumentException) {
            throw ValidationException("unknown status")
        }

        if (adRequest.status == newStatus) return

        val userId = authUserService.getUserId()
        val isSales = authUserService.hasRole(UserRole.SALES)

        if (!isSales && adRequest.owner.id != userId)
            throw PermissionDeniedException("ad request")

        when (newStatus) {
            AdRequestStatus.SAVED ->
                if (adRequest.status != AdRequestStatus.NEEDS_CLARIFICATION)
                    throw UnsupportedStatusChangeException()

            AdRequestStatus.READY_TO_CHECK ->
                if (adRequest.status != AdRequestStatus.SAVED)
                    throw UnsupportedStatusChangeException()

            AdRequestStatus.NEEDS_CLARIFICATION -> {
                if (adRequest.status != AdRequestStatus.READY_TO_CHECK)
                    throw UnsupportedStatusChangeException()
                if (!isSales) throw PermissionDeniedException("ad request status")
            }

            AdRequestStatus.MODERATION -> {
                if (adRequest.status != AdRequestStatus.READY_TO_CHECK)
                    throw UnsupportedStatusChangeException()
                if (!isSales) throw PermissionDeniedException("ad request status")
            }

            AdRequestStatus.READY_TO_PUBLISH -> {
                if (adRequest.status != AdRequestStatus.MODERATION)
                    throw UnsupportedStatusChangeException()
                if (!isSales) throw PermissionDeniedException("ad request status")
            }

            AdRequestStatus.PUBLISHED -> {
                if (adRequest.status != AdRequestStatus.READY_TO_PUBLISH)
                    throw UnsupportedStatusChangeException()
                if (!isSales) throw PermissionDeniedException("ad request status")
            }

            AdRequestStatus.CANCELED ->
                if (adRequest.status == AdRequestStatus.PUBLISHED)
                    throw UnsupportedStatusChangeException()
        }

        adRequest.status = newStatus

        adRequestRepo.save(adRequest)
    }

    @Throws(EntityNotFoundException::class)
    fun deleteAdRequest(adRequestId: Long) {
        if (adRequestRepo.findById(adRequestId).isPresent)
            adRequestRepo.deleteById(adRequestId)
        else
            throw EntityNotFoundException()
    }

    @Throws(EntityNotFoundException::class)
    fun getAdRequest(adRequestId: Long): AdRequest {
        val adRequest = adRequestRepo.findById(adRequestId).getOrElse {
            throw EntityNotFoundException()
        }

        val userId = authUserService.getUserId()
        if (!authUserService.hasRole(UserRole.SALES) && adRequest.owner.id != userId)
            throw PermissionDeniedException("ad request")

        return adRequest
    }

    fun getAdRequests(ownerId: Long?, status: String?): List<AdRequest> {
        val isSales = authUserService.hasRole(UserRole.SALES)
        val searchOwnerId = if (!isSales) authUserService.getUserId() else ownerId

        val adRequestStatus = try {
            status?.let {
                AdRequestStatus.valueOf(it.uppercase(Locale.getDefault()))
            }
        } catch (_: IllegalArgumentException) {
            null
        }

        if (adRequestStatus != null && searchOwnerId != null)
            return adRequestRepo.findAllByOwnerIdAndStatus(searchOwnerId, adRequestStatus)
        if (adRequestStatus != null)
            return adRequestRepo.findAllByStatus(adRequestStatus)
        if (searchOwnerId != null)
            return adRequestRepo.findAllByOwnerId(searchOwnerId)

        return adRequestRepo.findAll()
    }

    data class CreateAdRequest(
        var requestText: String?,
        val ageSegments: String?,
        val incomeSegments: String?,
        val locations: String?,
        val interests: String?,
        var publishDeadline: String?,
        val lifeHours: String?,
    ) {
        @Throws(ValidationException::class)
        fun validate() {
            if (requestText == null)
                throw ValidationException("request text must be provided")
            if (requestText == "")
                throw ValidationException("request text must be not empty")
            if (lifeHours != null) {
                val value = lifeHours.toIntOrNull() ?: throw ValidationException("life hour must be integer")
                if (value < 1)
                    throw ValidationException("life hours must be equal or greater 1")
            }
            if (publishDeadline != null) {
                val date = try {
                    LocalDate.parse(publishDeadline)
                } catch (e: DateTimeParseException) {
                    throw ValidationException("bad date: ${e.message}")
                }

                if (date.isBefore(LocalDate.now()))
                    throw ValidationException("publish dead line must be today or later")
            }
        }
    }

    data class UpdateAdRequest(
        val requestText: String?,
        val ageSegments: String?,
        val incomeSegments: String?,
        val locations: String?,
        val interests: String?,
        val publishDeadline: String?,
        val lifeHours: String?,
        val clarificationText: String?,
    ) {
        @Throws(ValidationException::class)
        fun validate() {
            if (requestText != null && requestText == "")
                throw ValidationException("request text must be not empty")
            if (lifeHours != null) {
                val value = lifeHours.toIntOrNull() ?: throw ValidationException("life hour must be integer")
                if (value < 1)
                    throw ValidationException("life hours must be equal or greater 1")
            }

            if (publishDeadline != null) {
                val date = try {
                    LocalDate.parse(publishDeadline)
                } catch (e: DateTimeParseException) {
                    throw ValidationException("bad date: ${e.message}")
                }

                if (date.isBefore(LocalDate.now()))
                    throw ValidationException("publish dead line must be today or later")
            }
        }
    }

    data class UpdateAdRequestStatus(
        val status: String,
    ) {
        @Throws(ValidationException::class)
        fun validate() {
            if (status == "")
                throw ValidationException("status must be provided")
        }
    }
}