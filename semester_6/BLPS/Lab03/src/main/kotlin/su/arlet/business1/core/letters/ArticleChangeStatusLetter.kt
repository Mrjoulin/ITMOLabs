package su.arlet.business1.core.letters

import letters.Letter
import su.arlet.business1.core.AdPost
import su.arlet.business1.core.Article
import su.arlet.business1.core.enums.ArticleStatus

class ArticleChangeStatusLetter(
    article: Article, oldStatus: ArticleStatus
) : Letter("article-change-status-letter", "Your article has changed status!") {
    init {
        var text = "<h1>\"${article.title}\" changed status from $oldStatus to ${article.status}</h1></br>"

        if (article.status == ArticleStatus.NEED_FIXES) {
            text += "<h2>Clarification text from editor:</h2></br>"
            text += "<p>${
                if (!article.clarificationText.isNullOrBlank()) article.clarificationText 
                else "No clarification text was given"
            }</p>"
        }

        html = """
            <html>
                <body>
                    $text
                </body>
            </html>
        """.trimIndent()
    }
}