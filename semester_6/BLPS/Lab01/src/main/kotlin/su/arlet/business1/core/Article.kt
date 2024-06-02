package su.arlet.business1.core

import jakarta.persistence.*
import su.arlet.business1.core.enums.ArticleStatus

@Entity
@Table(name = "articles")
class Article(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var title: String,

    var text: String,

    @ManyToMany
    @JoinTable(name = "article_images")
    var images: List<Image>,

    @ManyToMany
    @JoinTable(name = "article_ad_posts")
    var adPosts: List<AdPost> = emptyList(),

    @Enumerated(EnumType.STRING)
    var status: ArticleStatus,

    var clarificationText: String? = null,

    @ManyToOne
    var author: User,
    @ManyToOne
    var editor: User? = null,
)