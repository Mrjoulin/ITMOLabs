package org.joulin.core.letters

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