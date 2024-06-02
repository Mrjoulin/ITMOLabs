package org.joulin.core.letters

import org.joulin.core.AdPost
import org.joulin.core.AdRequest
import org.joulin.core.Article
import org.joulin.core.enums.AdRequestStatus
import org.joulin.core.enums.ArticleStatus

class AdRequestChangeStatusLetter(
    adRequest: AdRequest, oldStatus: AdRequestStatus
) : Letter("ad-request-change-status-letter", "Your ad request has changed status!") {
    init {
        var text = "<h1>Your Ad Request #${adRequest.id} changed status from $oldStatus to ${adRequest.status}</h1></br>"

        if (adRequest.status == AdRequestStatus.NEEDS_CLARIFICATION) {
            text += "<h2>Clarification text from sales:</h2></br>"
            text += "<p>${
                if (!adRequest.clarificationText.isNullOrBlank()) adRequest.clarificationText 
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