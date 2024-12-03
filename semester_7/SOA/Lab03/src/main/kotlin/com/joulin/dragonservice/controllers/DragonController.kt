package com.joulin.dragonservice.controllers

import com.joulin.dragonservice.core.Dragon
import com.joulin.dragonservice.core.enums.DragonType
import com.joulin.dragonservice.core.Person
import com.joulin.dragonservice.dto.DragonRequest
import com.joulin.dragonservice.dto.DragonsArray
import com.joulin.dragonservice.dto.SortAndFilterOptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import com.joulin.dragonservice.services.DragonService
import jakarta.validation.Valid

@Controller
@RequestMapping("/api/v1/dragons")
class DragonController {

    @Autowired
    lateinit var dragonService: DragonService

    @PostMapping("/array")
    fun findDragons(@Valid @RequestBody sortAndFilterOptions: SortAndFilterOptions?): ResponseEntity<DragonsArray> {
        val dragonsArray = dragonService.findDragons(sortAndFilterOptions ?: SortAndFilterOptions())
        return ResponseEntity.ok(dragonsArray)
    }

    @PostMapping("/")
    fun addDragon(@Valid @RequestBody dragonRequest: DragonRequest): ResponseEntity<Dragon> {
        val createdDragon = dragonService.addDragon(dragonRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDragon)
    }

    @GetMapping("/younger/{age}")
    fun getYoungerThan(@Valid @PathVariable age: Int): ResponseEntity<List<Dragon>> {
        val youngerDragons = dragonService.getYoungerThan(age)
        return ResponseEntity.ok(youngerDragons)
    }

    @GetMapping("/{dragonId}")
    fun findDragonById(@Valid @PathVariable dragonId: Long): ResponseEntity<Dragon> {
        val dragon = dragonService.findDragonById(dragonId)
        return ResponseEntity.ok(dragon)
    }

    @PutMapping("/{dragonId}")
    fun updateDragon(@Valid @PathVariable dragonId: Long, @RequestBody dragonRequest: DragonRequest): ResponseEntity<Dragon> {
        val updatedDragon = dragonService.updateDragon(dragonId, dragonRequest)
        return ResponseEntity.ok(updatedDragon)
    }

    @DeleteMapping("/{dragonId}")
    fun deleteDragonById(@Valid @PathVariable dragonId: Long): ResponseEntity<Dragon> {
        val dragon = dragonService.deleteDragonById(dragonId)
        return ResponseEntity.ok(dragon)
    }

    @DeleteMapping("/all/type/{type}")
    fun deleteAllByType(@Valid @PathVariable type: DragonType): ResponseEntity<List<Dragon>> {
        val dragons = dragonService.deleteAllByType(type)
        return ResponseEntity.ok(dragons)
    }

    @DeleteMapping("/one/killer/{killerStr}")
    fun deleteAnyByKiller(@Valid @PathVariable killerStr: String): ResponseEntity<Dragon> {
        val killer = Person.getFromString(killerStr)
        val removedDragon = dragonService.deleteAnyByKiller(killer)
        return ResponseEntity.ok(removedDragon)
    }
}