package com.joulin.killerservice.dto

import com.joulin.killerservice.core.Team
import com.joulin.killerservice.exceptions.ValidationException
import java.time.LocalDateTime

data class TeamRequest(
    var teamName: String,
    val teamSize: Long,
    val startCaveId: Long
) {
    fun validateTeam(): Team {
        if (teamSize <= 0) throw ValidationException("Team Size must be positive integer")

        return Team(
            creationDate = LocalDateTime.now(),
            name= teamName,
            size = teamSize,
            caveId = startCaveId
        )
    }
}
