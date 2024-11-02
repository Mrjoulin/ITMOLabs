package com.joulin.dragonservice.services

import com.joulin.dragonservice.core.Cave
import com.joulin.dragonservice.dto.CaveRequest
import com.joulin.dragonservice.exceptions.EntityNotFoundException
import com.joulin.dragonservice.repos.CaveRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CaveService(private val caveRepo: CaveRepository) {

    fun getCaves(): List<Cave> {
        return caveRepo.findAll()
    }

    fun findCaveById(caveId: Long) : Cave {
        return caveRepo.findByIdOrNull(caveId)
            ?: throw EntityNotFoundException("Cave with given ID not found")
    }

    fun addCave(caveRequest: CaveRequest) : Cave {
        val cave = caveRequest.validateCave()
        return caveRepo.save(cave)
    }

    fun updateCave(caveId: Long, caveRequest: CaveRequest) : Cave {
        val cave = findCaveById(caveId = caveId)
        cave.coordinates = caveRequest.coordinates
        return caveRepo.save(cave)
    }

    fun deleteCaveById(caveId: Long) {
        if (caveRepo.findById(caveId).isPresent)
            caveRepo.deleteById(caveId)
        else
            throw EntityNotFoundException("Cave with given ID not found")
    }
}