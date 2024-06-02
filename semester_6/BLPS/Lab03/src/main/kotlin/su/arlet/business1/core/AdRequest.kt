package su.arlet.business1.core

import jakarta.persistence.*
import su.arlet.business1.core.enums.AdRequestStatus
import java.time.LocalDate

@Entity
@Table(name = "ad_requests")
class AdRequest(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "ownerId")
    val owner: User,
    @Embedded
    var auditory: Auditory?,

    var requestText: String,
    var clarificationText: String? = null,
    var publishDeadline: LocalDate?,
    var lifeHours: Int?,

    @Enumerated(EnumType.STRING)
    var status: AdRequestStatus,
)