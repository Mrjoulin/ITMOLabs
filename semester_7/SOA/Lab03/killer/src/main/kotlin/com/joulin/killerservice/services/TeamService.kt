package com.joulin.killerservice.services

import com.joulin.killerservice.core.Team
import com.joulin.killerservice.dto.TeamRequest
import com.joulin.killerservice.exceptions.EntityNotFoundException
import com.joulin.killerservice.repos.TeamRepository
import jakarta.ws.rs.ServiceUnavailableException
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

@Service
class TeamService(
    private val teamRepo: TeamRepository,
    private val restTemplate: RestTemplate,
    private val discoveryClient: DiscoveryClient
) {
    private val dragonServiceName = "dragon"
    private val apiPrefix = "/api/v1"
    private fun ensureCaveExist(caveId: Long): Any? {
//        val uri = discoveryClient.getInstances(dragonServiceName).stream().findFirst().map { it.uri }
//        val service = uri.map { s -> s.resolve("$apiPrefix/caves/${caveId}") }.orElseThrow {
//            ServiceUnavailableException()
//        }
        val service = "https://$dragonServiceName/$apiPrefix/caves/${caveId}"
        println(service)
        try {
            val responseEntity: ResponseEntity<Any> = restTemplate.getForEntity(service, Any::class.java)
            if (responseEntity.statusCode == HttpStatus.OK) {
                return responseEntity.body
            }
            throw EntityNotFoundException("Invalid cave id")
        } catch (e: RestClientException) {
            e.printStackTrace()
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