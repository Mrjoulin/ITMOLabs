package su.arlet.business1.core

import jakarta.persistence.*

@Entity
@Table(name = "ad_metrics")
class AdMetrics(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    var viewCounter: Int = 0,
)