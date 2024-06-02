package su.arlet.business1.repos

import org.springframework.data.jpa.repository.JpaRepository
import su.arlet.business1.core.AdPost
import su.arlet.business1.core.enums.AdPostStatus

interface AdPostRepo : JpaRepository<AdPost, Long> {
    fun findAllByStatus(status: AdPostStatus): List<AdPost>
}