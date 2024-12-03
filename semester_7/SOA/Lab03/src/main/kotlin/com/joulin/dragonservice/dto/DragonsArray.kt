package com.joulin.dragonservice.dto

import com.joulin.dragonservice.core.Dragon
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor

@Getter
@NoArgsConstructor
@AllArgsConstructor
data class DragonsArray(
    val results: List<Dragon>,
    val page: Int,
    val pageSize: Int,
    val numPages: Int
)
