package com.joulin.dragonservice.dto

import com.joulin.dragonservice.core.Dragon

data class DragonsArray(
    val results: List<Dragon>,
    val page: Int,
    val pageSize: Int,
    val numPages: Int
)
