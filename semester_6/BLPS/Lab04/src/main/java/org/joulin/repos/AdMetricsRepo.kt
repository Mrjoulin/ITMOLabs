package org.joulin.repos

import org.springframework.data.repository.CrudRepository
import org.joulin.core.AdMetrics

interface AdMetricsRepo : CrudRepository<AdMetrics, Long>