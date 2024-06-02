package su.arlet.business1.core

import jakarta.persistence.*
import su.arlet.business1.core.enums.AdPostStatus

@Entity
@Table(name = "ad_posts")
class AdPost(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var title: String,
    var body: String,
    var targetLink: String,

    @ManyToOne
    var salesEditor: User?,

    @ManyToOne
    var image: Image?,

    @OneToOne
    val adRequest: AdRequest,

    @Enumerated(EnumType.STRING)
    var status: AdPostStatus,

    @OneToOne
    var metrics: AdMetrics? = null,
)