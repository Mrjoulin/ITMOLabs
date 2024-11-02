package com.joulin.dragonservice.repos

import com.joulin.dragonservice.core.Cave
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CaveRepository : JpaRepository<Cave, Long>{
}