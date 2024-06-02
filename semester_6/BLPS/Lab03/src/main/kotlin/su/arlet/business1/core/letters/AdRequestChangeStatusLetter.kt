package su.arlet.business1.core.letters

import letters.Letter
import su.arlet.business1.core.AdPost
import su.arlet.business1.core.AdRequest
import su.arlet.business1.core.Article
import su.arlet.business1.core.enums.AdRequestStatus
import su.arlet.business1.core.enums.ArticleStatus

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