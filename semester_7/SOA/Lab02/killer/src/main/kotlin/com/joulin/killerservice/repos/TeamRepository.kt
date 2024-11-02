package com.joulin.killerservice.repos

import com.joulin.killerservice.core.Team
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TeamRepository : JpaRepository<Team, Long>{
}