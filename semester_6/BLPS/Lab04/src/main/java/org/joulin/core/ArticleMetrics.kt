package org.joulin.core

import jakarta.persistence.*

@Entity
@Table(name = "article_metrics")
class ArticleMetrics(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    var viewCounter: Int = 0,
    var readCounter: Int = 0,
)