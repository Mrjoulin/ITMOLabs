package su.arlet.business1.repos

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import su.arlet.business1.core.Article
import su.arlet.business1.core.enums.ArticleStatus

interface ArticleRepo : JpaRepository<Article, Long> {
    fun findAllByStatus(status: ArticleStatus, pageable: Pageable): List<Article>
}