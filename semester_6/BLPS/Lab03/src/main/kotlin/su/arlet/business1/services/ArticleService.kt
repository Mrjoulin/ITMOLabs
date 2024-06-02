package su.arlet.business1.services

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import su.arlet.business1.core.*
import su.arlet.business1.core.enums.AdPostStatus
import su.arlet.business1.core.enums.AdRequestStatus
import su.arlet.business1.core.enums.ArticleStatus
import su.arlet.business1.core.enums.UserRole
import su.arlet.business1.core.letters.ArticleChangeStatusLetter
import su.arlet.business1.exceptions.*
import su.arlet.business1.gateways.email.EmailGateway
import su.arlet.business1.repos.*
import su.arlet.business1.security.services.AuthUserService
import java.time.LocalDateTime
import kotlin.jvm.optionals.getOrElse
import kotlin.jvm.optionals.getOrNull

@Service
class ArticleService(
    private val adPostService: AdPostService,
    private val articleRepo: ArticleRepo,
    private val adPostRepo: AdPostRepo,
    private val adMetricsRepo: AdMetricsRepo,
    private val imageRepo: ImageRepo,
    private val userRepo: UserRepo,
    private val authUserService: AuthUserService,
    private val articleMetricsRepo: ArticleMetricsRepo,
    private val emailGateway: EmailGateway,
) {
    @Throws(UserNotFoundException::class, ValidationException::class)
    fun addArticle(createArticleRequest: CreateArticleRequest): Long {
        val userId = authUserService.getUserId()

        val author = userRepo.findById(userId).getOrNull() ?: throw UserNotFoundException()

        val articleId = articleRepo.save(
            Article(
                title = createArticleRequest.title ?: throw ValidationException("title must be provided"),
                text = createArticleRequest.text ?: throw ValidationException("text must be provided"),
                images = getImagesById(createArticleRequest.imageIds ?: listOf()),
                status = ArticleStatus.ON_REVIEW,
                author = author,
            )
        ).id

        return articleId
    }

    private fun getImagesById(ids: List<Long>): List<Image> {
        val images = mutableListOf<Image>()

        for (id in ids) {
            val image = imageRepo.findById(id).getOrNull()
            if (image == null) {
                println("warning: image with id=${id} is not found. It will be ignored on creating article")
                continue
            }

            images.add(image)
        }

        return images
    }

    @Throws(EntityNotFoundException::class)
    fun updateArticle(id: Long, updateArticleRequest: UpdateArticleRequest) {
        val article = articleRepo.findById(id).getOrNull() ?: throw EntityNotFoundException()

        if (!authUserService.hasRole(UserRole.EDITOR) && article.author.id != authUserService.getUserId())
            throw PermissionDeniedException("article")

        updateArticleFields(article, updateArticleRequest)
    }

    @Throws(EntityNotFoundException::class)
    fun deleteArticle(id: Long) {
        val article = articleRepo.findById(id).getOrElse { throw EntityNotFoundException() }

        if (!authUserService.hasRole(UserRole.EDITOR) && article.author.id != authUserService.getUserId())
            throw PermissionDeniedException("article")

        articleRepo.deleteById(id)
    }

    @Throws(EntityNotFoundException::class)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun getArticle(id: Long): Article {
        val article = articleRepo.findById(id).getOrNull() ?: throw EntityNotFoundException()

        if (!authUserService.hasRole(UserRole.EDITOR) && article.author.id != authUserService.getUserId())
            if (article.status != ArticleStatus.PUBLISHED)
                throw PermissionDeniedException("article")

        if (article.adPosts.any { it.status == AdPostStatus.EXPIRED }) {
            article.adPosts = article.adPosts.filter { it.status != AdPostStatus.EXPIRED }
            articleRepo.save(article)
        }

        incReadMetrics(article)

        return article
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun getArticles(status: ArticleStatus?, offset: Int, limit: Int): List<ShortArticle> {
        val page = PageRequest.of(maxOf(offset, 0), minOf(maxOf(limit, 10), 100))

        val authorId = authUserService.getUserId()

        val articles = if (authUserService.hasRole(UserRole.EDITOR)) {
            if (status == null)
                articleRepo.findAll(page)
            else
                articleRepo.findAllByStatus(status, page)
        } else if (authUserService.hasRole(UserRole.JOURNALIST)) {
            if (status == null || status == ArticleStatus.PUBLISHED)
                articleRepo.findAllByStatusOrAuthorId(ArticleStatus.PUBLISHED, authorId, page)
            else
                articleRepo.findAllByStatusAndAuthorId(status, authorId, page)
        } else {
            articleRepo.findAllByStatus(ArticleStatus.PUBLISHED, page)
        }

        articles.forEach {
            incViewMetrics(it)
        }

        return articles.map {
            ShortArticle(
                id = it.id,
                title = it.title,
                status = it.status,
                clarificationText = it.clarificationText,
                authorId = it.author,
                editor = it.editor,
            )
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun incViewMetrics(article: Article) {
        if (article.status != ArticleStatus.PUBLISHED)
            return

        val metrics = article.metrics ?: ArticleMetrics()
        metrics.viewCounter++

        article.metrics = articleMetricsRepo.save(metrics)
        articleRepo.save(article)
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun incReadMetrics(article: Article) {
        if (article.status != ArticleStatus.PUBLISHED)
            return

        val metrics = article.metrics ?: ArticleMetrics()
        metrics.readCounter++

        article.metrics = articleMetricsRepo.save(metrics)
        articleRepo.save(article)

        for (adPost: AdPost in article.adPosts)
            incAdViewMetrics(adPost)
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun incAdViewMetrics(adPost: AdPost) {
        if (adPost.status != AdPostStatus.PUBLISHED)
            return

        val metrics = adPost.metrics ?: AdMetrics()
        metrics.viewCounter++

        adPost.metrics = adMetricsRepo.save(metrics)
        adPostRepo.save(adPost)
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Throws(EntityNotFoundException::class, UnsupportedStatusChangeException::class, UserNotFoundException::class)
    fun updateArticleStatus(id: Long, newStatus: ArticleStatus) {
        val article = articleRepo.findById(id).getOrNull() ?: throw EntityNotFoundException()

        when (newStatus) {
            ArticleStatus.ON_REVIEW -> {
                if (article.status != ArticleStatus.NEED_FIXES)
                    throw UnsupportedStatusChangeException()
            }

            ArticleStatus.NEED_FIXES -> {
                if (article.status != ArticleStatus.ON_REVIEW)
                    throw UnsupportedStatusChangeException()
            }

            ArticleStatus.PUBLISHED -> {
                if (article.status != ArticleStatus.APPROVED)
                    throw UnsupportedStatusChangeException()
                changeStatusesToPublished(article)
                return
            }

            ArticleStatus.APPROVED -> {
                if (article.status != ArticleStatus.ON_REVIEW)
                    throw UnsupportedStatusChangeException()

                val initiatorId: Long = authUserService.getUserId()
                val editor = userRepo.findById(initiatorId).getOrNull() ?: throw UserNotFoundException()

                article.editor = editor
            }
        }

        val oldStatus = article.status
        article.status = newStatus

        articleRepo.save(article)

        if (article.author.email != null)
            emailGateway.sendEmail(article.author.email!!, ArticleChangeStatusLetter(article, oldStatus))
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun changeStatusesToPublished(article: Article) {
        article.status = ArticleStatus.PUBLISHED
        article.adPosts.forEach {
            adPostService.updateAdPostStatus(it.id, AdPostStatus.PUBLISHED)
        }
        articleRepo.save(article)
    }

    @Throws(EntityNotFoundException::class)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun updateArticleAds(id: Long, newAdPostIds: List<Long>) {
        val article = articleRepo.findById(id).getOrNull() ?: throw EntityNotFoundException()

        val newAds = mutableListOf<AdPost>()

        for (adPostId in newAdPostIds) {
            val adPost = adPostRepo.findById(adPostId).getOrNull()
            if (adPost == null) {
                println("warning: ad post with id=${adPostId} is not found. It will be ignored on updating article")
                continue
            } else if (adPost.status == AdPostStatus.SAVED) {
                println(
                    "warning: ad post with id=${adPostId} can't be added to article " +
                            "because it not ready to publish. It will be ignored on updating article"
                )
                continue
            } else if (adPost.status == AdPostStatus.EXPIRED) {
                println(
                    "warning: ad post with id=${adPostId} can't be added to article " +
                            "because it already expired. It will be ignored on updating article"
                )
                continue
            }

            newAds.add(adPost)
        }

        article.adPosts = newAds

        articleRepo.save(article)

        if (article.status == ArticleStatus.PUBLISHED)
            changeStatusesToPublished(article)
    }

    data class CreateArticleRequest(
        val title: String?,
        val text: String?,
        val imageIds: List<Long>?,
    ) {
        @Throws(ValidationException::class)
        fun validate() {
            if (title == null)
                throw ValidationException("title must be provided")
            if (text == null)
                throw ValidationException("text must be provided")
            if (title == "")
                throw ValidationException("title must be not empty")
            if (text == "")
                throw ValidationException("text must be not empty")
        }
    }

    data class ShortArticle(
        val id: Long,
        val title: String,
        val status: ArticleStatus,
        val clarificationText: String?,
        val authorId: User,
        val editor: User?,
    )

    data class UpdateArticleRequest(
        val title: String?,
        val text: String?,
        val imageIds: List<Long>?,
        val clarificationText: String?,
    ) {
        @Throws(ValidationException::class)
        fun validate() {
            if (title == "")
                throw ValidationException("title must be not empty")
            if (text == "")
                throw ValidationException("text must be not empty")
        }
    }

    private fun updateArticleFields(article: Article, updateArticleRequest: UpdateArticleRequest) {
        updateArticleRequest.text?.let {
            article.text = it
            if (article.status == ArticleStatus.NEED_FIXES)
                article.status = ArticleStatus.ON_REVIEW
        }
        updateArticleRequest.title?.let {
            article.title = it
            if (article.status == ArticleStatus.NEED_FIXES)
                article.status = ArticleStatus.ON_REVIEW
        }
        updateArticleRequest.imageIds?.let {
            val newImages = mutableListOf<Image>()

            for (imageId in updateArticleRequest.imageIds) {
                val image = imageRepo.findById(imageId).getOrNull()
                if (image == null) {
                    println("warning: image with id=${imageId} is not found. It will be ignored on updating article")
                    continue
                }

                newImages.add(image)
            }

            article.images = newImages
        }
        updateArticleRequest.clarificationText?.let { article.clarificationText = it }
    }
}