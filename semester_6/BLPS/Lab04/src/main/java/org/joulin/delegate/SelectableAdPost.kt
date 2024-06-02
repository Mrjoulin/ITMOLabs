package org.joulin.delegate

import org.joulin.core.AdPost
import java.io.Serializable

class SelectableAdPost(
    val id: Long = 0,
    var title: String?,
    var body: String?,
    var targetLink: String?,
    var chosen: Boolean
) : Serializable {
    constructor(adPost: AdPost): this(adPost.id, adPost.title, adPost.body, adPost.targetLink, false);
}