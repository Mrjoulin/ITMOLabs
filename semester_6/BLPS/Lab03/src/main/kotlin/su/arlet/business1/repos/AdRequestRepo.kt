package su.arlet.business1.repos

import org.springframework.data.jpa.repository.JpaRepository
import su.arlet.business1.core.AdRequest
import su.arlet.business1.core.enums.AdRequestStatus

interface AdRequestRepo : JpaRepository<AdRequest, Long> {
    fun findAllByStatus(status: AdRequestStatus): List<AdRequest>
    fun findAllByOwnerId(ownerId: Long): List<AdRequest>
    fun findAllByOwnerIdAndStatus(ownerId: Long, status: AdRequestStatus): List<AdRequest>
}