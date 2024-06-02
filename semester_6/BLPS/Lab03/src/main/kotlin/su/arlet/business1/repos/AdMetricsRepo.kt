package su.arlet.business1.repos

import org.springframework.data.repository.CrudRepository
import su.arlet.business1.core.AdMetrics

interface AdMetricsRepo : CrudRepository<AdMetrics, Long>