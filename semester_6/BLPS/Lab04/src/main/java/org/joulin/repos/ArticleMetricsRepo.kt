package org.joulin.repos

import org.springframework.data.repository.CrudRepository
import org.joulin.core.ArticleMetrics

interface ArticleMetricsRepo : CrudRepository<ArticleMetrics, Long>