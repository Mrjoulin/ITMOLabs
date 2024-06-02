package su.arlet.business1.controllers.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import su.arlet.business1.core.Article
import su.arlet.business1.core.enums.ArticleStatus
import su.arlet.business1.exceptions.ValidationException
import su.arlet.business1.security.services.AuthUserService
import su.arlet.business1.services.ArticleService

@RestController
@RequestMapping("\${api.path}/articles")
@Tag(name = "Article API")
class ArticleController(
    val articleService: ArticleService,
    val authUserService: AuthUserService,
) {

    @GetMapping("/")
    @Operation(summary = "Get articles by filters")
    @ApiResponse(
        responseCode = "200", description = "OK", content = [
            Content(array = ArraySchema(items = Schema(implementation = ArticleService.ShortArticle::class)))
        ]
    )
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun getArticles(
        @RequestParam(name = "status", required = false) status: ArticleStatus?,
        @RequestParam(name = "offset", defaultValue = "0") offset: Int,
        @RequestParam(name = "limit", defaultValue = "20") limit: Int,
    ): ResponseEntity<*> {
        val articles = articleService.getArticles(status, offset, limit)
        return ResponseEntity(articles, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get article by ID")
    @ApiResponse(
        responseCode = "200", description = "Success - found article", content = [
            Content(schema = Schema(implementation = Article::class))
        ]
    )
    @ApiResponse(responseCode = "404", description = "Not found - article not found", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun getArticleById(@PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity(articleService.getArticle(id), HttpStatus.OK)
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('JOURNALIST')")
    @Operation(summary = "Create a new article")
    @ApiResponse(
        responseCode = "201", description = "Created article id", content = [
            Content(schema = Schema(implementation = Long::class))
        ]
    )
    @ApiResponse(
        responseCode = "400", description = "Bad body", content = [
            Content(schema = Schema(implementation = String::class)),
        ]
    )
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun createArticle(
        @RequestBody createArticleRequest: ArticleService.CreateArticleRequest,
        request: HttpServletRequest,
    ): ResponseEntity<*> {
        createArticleRequest.validate()

        val articleId = articleService.addArticle(createArticleRequest)

        return ResponseEntity(articleId, HttpStatus.CREATED)
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('JOURNALIST')")
    @Operation(summary = "Update article info")
    @ApiResponse(responseCode = "200", description = "Success - updated article", content = [Content()])
    @ApiResponse(
        responseCode = "400", description = "Bad body", content = [
            Content(schema = Schema(implementation = String::class)),
        ]
    )
    @ApiResponse(responseCode = "404", description = "Not found - article not found", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun updateArticle(
        @PathVariable id: Long,
        @RequestBody updatedArticle: ArticleService.UpdateArticleRequest,
    ): ResponseEntity<*> {
        updatedArticle.validate()

        articleService.updateArticle(id, updatedArticle)

        return ResponseEntity(null, HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EDITOR')")
    @Operation(summary = "Delete article")
    @ApiResponse(responseCode = "200", description = "Success - deleted article", content = [Content()])
    @ApiResponse(responseCode = "204", description = "No content", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun deleteArticle(@PathVariable id: Long): ResponseEntity<*> {
        articleService.deleteArticle(id)
        return ResponseEntity(null, HttpStatus.OK)
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('JOURNALIST')")
    @Operation(summary = "Update article status")
    @ApiResponse(responseCode = "200", description = "Updated article", content = [Content()])
    @ApiResponse(
        responseCode = "400", description = "Bad body", content = [
            Content(schema = Schema(implementation = String::class))
        ]
    )
    @ApiResponse(responseCode = "404", description = "Not found - article not found", content = [Content()])
    @ApiResponse(responseCode = "409", description = "Invalid status change", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun updateArticleStatus(
        @PathVariable id: Long,
        @RequestBody updateStatusRequest: UpdateStatusRequest,
        request: HttpServletRequest,
    ): ResponseEntity<*> {
        updateStatusRequest.validate()

        articleService.updateArticleStatus(id, ArticleStatus.valueOf(updateStatusRequest.newStatus!!))
        return ResponseEntity(null, HttpStatus.OK)
    }

    @PutMapping("/{id}/ads")
    @PreAuthorize("hasRole('EDITOR')")
    @Operation(summary = "Update article ads")
    @ApiResponse(responseCode = "200", description = "Updated article", content = [Content()])
    @ApiResponse(
        responseCode = "400", description = "Bad body", content = [
            Content(schema = Schema(implementation = String::class))
        ]
    )
    @ApiResponse(responseCode = "404", description = "Not found - article not found", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun updateArticleAds(
        @PathVariable id: Long,
        @RequestBody articleAdPostsIds: List<Long>,
    ): ResponseEntity<*> {
        articleService.updateArticleAds(id, articleAdPostsIds)
        return ResponseEntity(null, HttpStatus.OK)
    }

    data class UpdateStatusRequest(
        val newStatus: String?,
    ) {
        fun validate() {
            if (newStatus == null)
                throw ValidationException("new status must be provided")

            try {
                ArticleStatus.valueOf(newStatus)
            } catch (e: IllegalArgumentException) {
                throw ValidationException("unknown status type: $newStatus")
            }
        }
    }
}