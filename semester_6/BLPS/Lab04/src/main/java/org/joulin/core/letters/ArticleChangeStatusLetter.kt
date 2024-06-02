package org.joulin.core.letters

import org.joulin.core.AdPost
import org.joulin.core.Article
import org.joulin.core.enums.ArticleStatus

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