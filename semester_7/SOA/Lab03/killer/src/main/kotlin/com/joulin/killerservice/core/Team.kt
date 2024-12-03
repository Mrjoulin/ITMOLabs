package com.joulin.killerservice.core

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "killer_teams")
data class Team(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val creationDate: LocalDateTime,
    var name: String,
    var size: Long,
    var caveId: Long
)
