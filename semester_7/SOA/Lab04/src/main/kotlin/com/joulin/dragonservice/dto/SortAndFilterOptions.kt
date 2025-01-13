package com.joulin.dragonservice.dto

import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor

@Getter
@NoArgsConstructor
@AllArgsConstructor
data class SortAndFilterOptions(
    val sort: List<String>? = null,
    val filter: List<Filter>? = null,
    val page: Int? = 0,
    val pageSize: Int? = 10
)