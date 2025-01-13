package com.joulin.killerservice.services

import com.joulin.killerservice.core.Team
import com.joulin.killerservice.dto.ServiceFaultDto
import com.joulin.killerservice.dto.TeamRequest
import com.joulin.killerservice.exceptions.ServiceFaultException
import com.joulin.killerservice.repos.TeamRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate


@Service
class TeamService(
    private val teamRepo: TeamRepository,
    private val restTemplate: RestTemplate,
) {
    private val dragonServiceName = "dragon"
    private val apiPrefix = "/api/v1"

    private fun ensureCaveExist(caveId: Long): Any? {
        val serviceURI = "https://$dragonServiceName/$apiPrefix/caves/${caveId}"
        println("Request to $serviceURI")
        try {
            val requestEntity = HttpEntity<String>("")
            val responseEntity: ResponseEntity<Any> = restTemplate.exchange(
                serviceURI, HttpMethod.GET, requestEntity, Any::class.java
            )

            if (responseEntity.statusCode == HttpStatus.OK) {
                return responseEntity.body
            }
            throw ServiceFaultException(ServiceFaultDto(404, "Invalid cave id"))
        } catch (e: RestClientException) {
            throw ServiceFaultException(ServiceFaultDto(404, "Invalid cave id"))
        }
    }

    fun createTeam( teamRequest: TeamRequest): Team {
        if (teamRequest.teamName == null || teamRequest.teamSize == null || teamRequest.startCaveId == null)
            throw ServiceFaultException(ServiceFaultDto(400, "All params should be filled"))

        ensureCaveExist(teamRequest.startCaveId!!)
        val team = teamRequest.validateTeam()
        return teamRepo.save(team)
    }

    fun updateTeamCave(team: Team, caveId: Long?): Team {
        if (caveId == null)
            throw ServiceFaultException(ServiceFaultDto(400, "All params should be filled"))

        ensureCaveExist(caveId)

        team.caveId = caveId
        return teamRepo.save(team)
    }

    fun getTeamById(teamId: Long): Team {
        return teamRepo.findByIdOrNull(teamId) ?:
            throw ServiceFaultException(ServiceFaultDto(404, "Team not found"))
    }
}