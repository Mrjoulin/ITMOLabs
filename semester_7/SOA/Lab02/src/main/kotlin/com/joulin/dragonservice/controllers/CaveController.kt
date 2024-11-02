package com.joulin.dragonservice.controllers

import com.joulin.dragonservice.core.Cave
import com.joulin.dragonservice.dto.CaveRequest
import com.joulin.dragonservice.services.CaveService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/api/v1/caves")
class CaveController {
    @Autowired
    lateinit var caveService: CaveService

    @PostMapping
    fun addCave(@RequestBody caveRequest: CaveRequest): ResponseEntity<Cave> {
        val createdCave = caveService.addCave(caveRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCave)
    }

    @GetMapping
    fun getCaves(): ResponseEntity<List<Cave>> {
        val caves = caveService.getCaves()
        return ResponseEntity.ok(caves)
    }

    @GetMapping("/{caveId}")
    fun findCaveById(@PathVariable caveId: Long): ResponseEntity<Cave> {
        val cave = caveService.findCaveById(caveId)
        return ResponseEntity.ok(cave)
    }

    @PutMapping("/{caveId}")
    fun updateCave(@PathVariable caveId: Long, @RequestBody caveRequest: CaveRequest): ResponseEntity<Cave> {
        val updatedCave = caveService.updateCave(caveId, caveRequest)
        return ResponseEntity.ok(updatedCave)
    }

    @DeleteMapping("/{caveId}")
    fun deleteCaveById(@PathVariable caveId: Long): ResponseEntity<Unit> {
        caveService.deleteCaveById(caveId)
        return ResponseEntity.ok().build()
    }
}