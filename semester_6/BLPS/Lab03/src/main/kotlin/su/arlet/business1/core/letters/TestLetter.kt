package su.arlet.business1.core.letters

import letters.Letter

class TestLetter(
    testStr: String,
) : Letter("test-letter", "Test letter") {

    init {
        html = """
            <html>
                <body>
                    <p>${testStr}</p>
                    <b>Test bold text</b>
                </body>
            </html>
        """.trimIndent()
    }
}