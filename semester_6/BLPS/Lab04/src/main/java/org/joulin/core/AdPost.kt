package org.joulin.core

import jakarta.persistence.*
import org.joulin.core.enums.AdPostStatus
import java.time.LocalDateTime

@Entity
@Table(name = "ad_posts")
class AdPost(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var title: String,
    @Column(columnDefinition = "TEXT")
    var body: String,
    var targetLink: String,
    var publishDate: LocalDateTime? = null,

    @ManyToOne
    var image: Image?,

    @OneToOne
    val adRequest: AdRequest,

    @Enumerated(EnumType.STRING)
    var status: AdPostStatus,

    @OneToOne
    var metrics: AdMetrics? = null,
)