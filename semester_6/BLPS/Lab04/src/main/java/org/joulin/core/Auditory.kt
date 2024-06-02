package org.joulin.core

import jakarta.persistence.Embeddable

@Embeddable
class Auditory(
    var ageSegments: String? = null,
    var incomeSegments: String? = null,
    var locations: String? = null,
    var interests: String? = null,
)