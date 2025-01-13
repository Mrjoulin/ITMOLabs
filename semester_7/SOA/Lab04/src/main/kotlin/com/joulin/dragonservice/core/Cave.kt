package com.joulin.dragonservice.core

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import java.time.LocalDateTime

@Entity
@Table(name = "caves")
@Getter
@NoArgsConstructor
@AllArgsConstructor
data class Cave(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Embedded
    @AttributeOverrides(*[
        AttributeOverride( name = "x", column = Column(name = "coordinates_x")),
        AttributeOverride( name = "y", column = Column(name = "coordinates_y"))
    ])
    var coordinates: Coordinates,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    var creationDate: LocalDateTime
)