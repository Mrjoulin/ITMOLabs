package letters

import java.io.Serializable

open class Letter(
    val name: String,
    val subject: String,
) : Serializable {

    open var html: String = ""

    constructor() : this("", "")
}