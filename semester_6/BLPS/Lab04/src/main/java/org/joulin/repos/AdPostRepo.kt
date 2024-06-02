package org.joulin.repos

import org.springframework.data.jpa.repository.JpaRepository
import org.joulin.core.AdPost
import org.joulin.core.enums.AdPostStatus

interface AdPostRepo : JpaRepository<AdPost, Long> {
    fun findAllByStatus(status: AdPostStatus): List<AdPost>
}