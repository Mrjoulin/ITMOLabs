package su.arlet.business1.services

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import su.arlet.business1.core.AdPost
import su.arlet.business1.core.Article
import su.arlet.business1.core.Image
import su.arlet.business1.core.User
import su.arlet.business1.core.enums.ArticleStatus
import su.arlet.business1.exceptions.EntityNotFoundException
import su.arlet.business1.exceptions.UserNotFoundException
import su.arlet.business1.exceptions.ValidationException
import su.arlet.business1.repos.AdPostRepo
import su.arlet.business1.repos.ArticleRepo
import su.arlet.business1.repos.ImageRepo
import su.arlet.business1.repos.UserRepo
import kotlin.jvm.optionals.getOrNull

@Service
class ArticleService(
    private val articleRepo: ArticleRepo,
    private val adPostRepo: AdPostRepo,
    private val imageRepo: ImageRepo,
    private val userRepo: UserRepo,
) {
    @Throws(UserNotFoundException::class, ValidationException::class)
    fun addArticle(createArticleRequest: CreateArticleRequest): Long {
        val author = userRepo.findById(createArticleRequest.authorId).getOrNull() ?: throw UserNotFoundException()

        val articleId = articleRepo.save(
            Article(
                title = createArticleRequest.title ?: throw ValidationException("title must be provided"),
                text = createArticleRequest.text ?: throw ValidationException("text must be provided"),
                images = getImagesById(createArticleRequest.imageIds),
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

        updateArticleFields(article, updateArticleRequest)
    }

    @Throws(EntityNotFoundException::class)
    fun deleteArticle(id: Long) {
        if (articleRepo.findById(id).isPresent)
            articleRepo.deleteById(id)
        else
            throw EntityNotFoundException()
    }

    @Throws(EntityNotFoundException::class)
    fun getArticle(id: Long): Article {
        return articleRepo.findById(id).getOrNull() ?: throw EntityNotFoundException()
    }

    fun getArticles(status: ArticleStatus?, offset: Int, limit: Int): List<ShortArticle> {
        val page = PageRequest.of(maxOf(offset, 0), minOf(maxOf(limit, 10), 100))

        val articles = if (status != null) {
            articleRepo.findAllByStatus(status, page)
        } else {
            articleRepo.findAll(page)
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

    @Throws(EntityNotFoundException::class, UnsupportedOperationException::class, UserNotFoundException::class)
    fun updateArticleStatus(id: Long, newStatus: ArticleStatus, initiatorId: Long) {
        val article = articleRepo.findById(id).getOrNull() ?: throw EntityNotFoundException()

        when (newStatus) {
            ArticleStatus.ON_REVIEW -> {
                if (article.status != ArticleStatus.NEED_FIXES)
                    throw UnsupportedOperationException()
            }

            ArticleStatus.NEED_FIXES -> {
                if (article.status != ArticleStatus.ON_REVIEW)
                    throw UnsupportedOperationException()
            }

            ArticleStatus.PUBLISHED -> {
                if (article.status != ArticleStatus.APPROVED)
                    throw UnsupportedOperationException()
            }

            ArticleStatus.APPROVED -> {
                if (article.status != ArticleStatus.ON_REVIEW)
                    throw UnsupportedOperationException()

                val editor = userRepo.findById(initiatorId).getOrNull() ?: throw UserNotFoundException()

                article.editor = editor
            }
        }

        article.status = newStatus

        articleRepo.save(article)
    }

    @Throws(EntityNotFoundException::class)
    fun updateArticleAds(id: Long, newAdPostIds: List<Long>) {
        val article = articleRepo.findById(id).getOrNull() ?: throw EntityNotFoundException()

        val newAds = mutableListOf<AdPost>()

        for (adPostId in newAdPostIds) {
            val adPost = adPostRepo.findById(adPostId).getOrNull()
            if (adPost == null) {
                println("warning: ad post with id=${adPostId} is not found. It will be ignored on updating article")
                continue
            }

            newAds.add(adPost)
        }

        article.adPosts = newAds

        articleRepo.save(article)
    }

    data class CreateArticleRequest(
        val title: String?,
        val text: String?,
        val imageIds: List<Long>,
        val authorId: Long,
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
        updateArticleRequest.text?.let { article.text = it }
        updateArticleRequest.title?.let { article.title = it }
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