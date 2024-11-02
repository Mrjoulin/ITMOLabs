package com.joulin.dragonservice.repos

import org.springframework.data.jpa.repository.JpaRepository
import com.joulin.dragonservice.core.Dragon
import com.joulin.dragonservice.core.enums.DragonType
import com.joulin.dragonservice.core.Person
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface DragonRepository : JpaRepository<Dragon, Long>, JpaSpecificationExecutor<Dragon> {
    fun findByAgeLessThan(age: Int): List<Dragon>
    fun findByKiller(killer: Person): List<Dragon>
    fun findByType(type: DragonType): List<Dragon>
}