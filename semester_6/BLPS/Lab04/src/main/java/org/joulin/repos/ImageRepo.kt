package org.joulin.repos

import org.springframework.data.jpa.repository.JpaRepository
import org.joulin.core.Image

interface ImageRepo : JpaRepository<Image, Long> {
    fun findAllByAlias(alias: String): List<Image>
    fun findAllByLink(link: String): List<Image>
    fun findAllByLinkAndAlias(alias: String, link: String): List<Image>
}