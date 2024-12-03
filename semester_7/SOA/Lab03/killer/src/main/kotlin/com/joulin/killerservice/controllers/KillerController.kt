package com.joulin.killerservice.controllers

import com.joulin.killerservice.core.Team
import com.joulin.killerservice.dto.TeamRequest
import com.joulin.killerservice.services.TeamService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/api/v1/killers")
class KillerController {

    @Autowired
    private lateinit var teamService: TeamService

    @PostMapping("/teams/create/{teamName}/{teamSize}/{startCaveId}")
    fun createTeam(teamRequest: TeamRequest) : ResponseEntity<Team> {
        val team = teamService.createTeam(teamRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(team)
    }

    @PutMapping("/team/{teamId}/move/{caveId}")
    fun updateTeamCave(@PathVariable teamId: Long, @PathVariable caveId: Long) : ResponseEntity<Team> {
        val team = teamService.updateTeamCave(teamId, caveId)
        return ResponseEntity.ok(team)
    }
}