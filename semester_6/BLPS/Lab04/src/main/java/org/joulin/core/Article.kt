package org.joulin.core

import jakarta.persistence.*
import org.joulin.core.enums.ArticleStatus

@Entity
@Table(name = "articles")
class Article(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var title: String,

    @Column(columnDefinition = "TEXT")
    var text: String,

    var images: List<String>,

    @ManyToMany
    @JoinTable(name = "article_ad_posts")
    var adPosts: List<AdPost> = emptyList(),

    @Enumerated(EnumType.STRING)
    var status: ArticleStatus,

    @Column(columnDefinition = "TEXT")
    var clarificationText: String? = null,

    @OneToOne
    var metrics: ArticleMetrics? = null,
)