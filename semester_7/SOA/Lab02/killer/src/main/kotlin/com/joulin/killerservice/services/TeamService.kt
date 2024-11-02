package com.joulin.killerservice.services

import com.joulin.killerservice.core.Team
import com.joulin.killerservice.dto.TeamRequest
import com.joulin.killerservice.exceptions.EntityNotFoundException
import com.joulin.killerservice.repos.TeamRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

@Service
class TeamService(
    private val teamRepo: TeamRepository,
    private val restTemplate: RestTemplate
) {
    private val dragonServiceUrl = "https://dragon:8443/Lab02-1.0/api/v1/"

    private fun ensureCaveExist(caveId: Long): Any? {
        try {
            val responseEntity: ResponseEntity<Any> = restTemplate.getForEntity(
                "$dragonServiceUrl/caves/{caveId}", Any::class.java, caveId
            )
            if (responseEntity.statusCode == HttpStatus.OK) {
                return responseEntity.body
            }
            throw EntityNotFoundException("Invalid cave id")
        } catch (e: RestClientException) {
            throw EntityNotFoundException("Invalid cave id")
        }
    }

    fun createTeam(teamRequest: TeamRequest): Team {
        ensureCaveExist(teamRequest.startCaveId)
        val team = teamRequest.validateTeam()
        return teamRepo.save(team)
    }

    fun updateTeamCave(teamId: Long, caveId: Long): Team {
        val team = teamRepo.findByIdOrNull(teamId) ?: throw EntityNotFoundException("Team not found")
        ensureCaveExist(caveId)

        team.caveId = caveId
        return teamRepo.save(team)
    }
}