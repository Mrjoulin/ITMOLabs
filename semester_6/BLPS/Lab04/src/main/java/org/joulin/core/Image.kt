package org.joulin.core

import jakarta.persistence.*


@Entity
@Table(name = "images")
class Image(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var alias: String,

    var link: String,
)