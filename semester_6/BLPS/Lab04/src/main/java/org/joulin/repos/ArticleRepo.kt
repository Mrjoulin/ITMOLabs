package org.joulin.repos

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.joulin.core.Article
import org.joulin.core.enums.ArticleStatus

interface ArticleRepo : JpaRepository<Article, Long> {
    fun findAllByStatus(status: ArticleStatus, pageable: Pageable): List<Article>
}