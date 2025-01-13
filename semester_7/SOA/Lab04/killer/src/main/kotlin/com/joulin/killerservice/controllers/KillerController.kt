package com.joulin.killerservice.controllers

import com.joulin.killerservice.config.WebServiceConfig.Companion.NAMESPACE_URI
import com.joulin.killerservice.dto.MoveTeamRequest
import com.joulin.killerservice.dto.MoveTeamResponse
import com.joulin.killerservice.dto.TeamRequest
import com.joulin.killerservice.dto.TeamResponse
import com.joulin.killerservice.services.TeamService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ws.server.endpoint.annotation.Endpoint
import org.springframework.ws.server.endpoint.annotation.PayloadRoot
import org.springframework.ws.server.endpoint.annotation.RequestPayload
import org.springframework.ws.server.endpoint.annotation.ResponsePayload


// @Controller
// @RequestMapping("/api/v1/killers")
@Endpoint
class KillerController {
    @Autowired
    private lateinit var teamService: TeamService
    private val logger = LoggerFactory.getLogger(KillerController::class.java)
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "TeamRequest")
    @ResponsePayload
    fun createTeam(@RequestPayload teamRequest: TeamRequest) : TeamResponse {
        logger.info("Create Team request: ${teamRequest.teamName} ${teamRequest.teamSize} ${teamRequest.startCaveId}")

        val team = teamService.createTeam(teamRequest)

        logger.info("Created team: $team")

        return TeamResponse.fromTeam(team)
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "MoveTeamRequest")
    @ResponsePayload
    fun moveTeam(@RequestPayload request: MoveTeamRequest) : MoveTeamResponse {
        logger.info("Move Team request: ${request.teamId} ${request.caveId}")

        val oldTeam = teamService.getTeamById(request.teamId)
        val oldCaveId = oldTeam.caveId

        val team = teamService.updateTeamCave(oldTeam, request.caveId)

        logger.info("Moved team: $team")

        return MoveTeamResponse.fromTeam(team, oldCaveId)
    }
}