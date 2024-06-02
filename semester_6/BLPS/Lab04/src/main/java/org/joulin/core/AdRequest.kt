package org.joulin.core

import jakarta.persistence.*
import org.joulin.core.enums.AdRequestStatus
import java.time.LocalDate

@Entity
@Table(name = "ad_requests")
class AdRequest(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Embedded
    var auditory: Auditory?,

    var requestText: String,
    var clarificationText: String? = null,
    var publishDeadline: LocalDate?,
    var lifeHours: Int?,

    @Enumerated(EnumType.STRING)
    var status: AdRequestStatus,
)