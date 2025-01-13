package com.joulin.dragonservice.services

import com.joulin.dragonservice.core.*
import com.joulin.dragonservice.core.enums.DragonType
import com.joulin.dragonservice.dto.DragonRequest
import com.joulin.dragonservice.dto.DragonsArray
import com.joulin.dragonservice.dto.FieldType
import com.joulin.dragonservice.dto.SortAndFilterOptions
import com.joulin.dragonservice.exceptions.EntityNotFoundException
import com.joulin.dragonservice.repos.DragonRepository
import com.joulin.dragonservice.repos.spec.DragonSpecification
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import kotlin.math.min


@Service
class DragonService(
    private val dragonRepository: DragonRepository,
    private val caveService: CaveService
) {

    fun findDragons(options: SortAndFilterOptions): DragonsArray {
        val pageable = PageRequest.of(
            maxOf(options.page ?: 0, 0),
            minOf(maxOf(options.pageSize ?: 10, 10), 100),
            getSort(options.sort)
        )

        val specification = DragonSpecification(options)
        val page = dragonRepository.findAll(specification, pageable)

        return DragonsArray(
            results = page.content,
            page = page.number,
            pageSize = page.size,
            numPages = page.totalPages
        )
    }

    private fun getSort(sortFields: List<String>?): Sort {
        return sortFields?.filter { it.isNotEmpty() }?.map { field ->
            val isDescending = field.startsWith("-")
            val fieldName = if (isDescending) field.substring(1) else field
            Sort.Order(if (isDescending) Sort.Direction.DESC else Sort.Direction.ASC, fieldName)
        }?.let { Sort.by(it) } ?: Sort.unsorted()
    }

    fun addDragon(dragonRequest: DragonRequest): Dragon {
        if (dragonRequest.caveId != null) {
            val cave = caveService.findCaveById(dragonRequest.caveId)
            dragonRequest.setCave(cave)
        }
        val dragon = dragonRequest.validateDragon()
        return dragonRepository.save(dragon)
    }

    fun getYoungerThan(age: Int): List<Dragon> {
        return dragonRepository.findByAgeLessThan(age)
    }

    fun findDragonById(dragonId: Long): Dragon {
        return dragonRepository.findByIdOrNull(dragonId)
            ?: throw EntityNotFoundException("Dragon with given ID not found")
    }

    fun updateDragon(dragonId: Long, dragonRequest: DragonRequest): Dragon {
        val dragon = findDragonById(dragonId)

        dragonRequest.validateName()?.let { dragon.name = it }
        dragonRequest.validateAge()?.let { dragon.age = it }
        dragonRequest.getColorEnum()?.let { dragon.color = it }
        dragonRequest.getTypeEnum()?.let { dragon.type = it }
        dragonRequest.getCharacterEnum()?.let { dragon.character = it }
        dragonRequest.coordinates?.let {coord ->
            coord.validateX()?.let { dragon.coordinates.x = it }
            coord.validateY()?.let { dragon.coordinates.y = it }
        }
        dragonRequest.killer?.let {killer ->
            if (dragon.killer == null) {
                dragon.killer = killer.validatePerson()
            } else {
                killer.validateName()?.let { dragon.killer!!.name = it }
                killer.validateWeight()?.let { dragon.killer!!.weight = it }
                killer.getEyeColorEnum()?.let { dragon.killer!!.eyeColor = it }
                killer.getNationalityEnum()?.let { dragon.killer!!.nationality = it }
            }
        }
        dragonRequest.caveId?.let {
            dragon.cave = caveService.findCaveById(it)
        }

        return dragonRepository.save(dragon)
    }

    fun deleteDragonById(dragonId: Long) : Dragon {
        val dragon = dragonRepository.findById(dragonId)
        if (dragon.isPresent)
            dragonRepository.deleteById(dragonId)
        else
            throw EntityNotFoundException("Dragon with given ID not found")
        return dragon.get()
    }

    fun deleteAllByType(type: DragonType) : List<Dragon> {
        val dragons = dragonRepository.findByType(type)
        dragonRepository.deleteAll(dragons)
        return dragons
    }

    fun deleteAnyByKiller(killer: Person) : Dragon? {
        val dragons = dragonRepository.findByKiller(killer)

        val dragon = dragons.firstOrNull() ?: return null

        dragonRepository.delete(dragon)

        return dragon
    }
}