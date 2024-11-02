package com.joulin.dragonservice.repos.spec

import com.joulin.dragonservice.core.Coordinates
import com.joulin.dragonservice.core.Dragon
import com.joulin.dragonservice.dto.FieldType
import com.joulin.dragonservice.dto.Filter
import com.joulin.dragonservice.dto.FilterSign
import com.joulin.dragonservice.dto.SortAndFilterOptions
import org.springframework.data.jpa.domain.Specification
import jakarta.persistence.criteria.*
import java.time.LocalDateTime

class DragonSpecification(
    private val options: SortAndFilterOptions
) : Specification<Dragon> {

    override fun toPredicate(
        root: Root<Dragon>,
        query: CriteriaQuery<*>?,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        var predicate: Predicate? = null

        options.filter?.filter { it.isCorrect() }?.forEach { filter ->
            val filterPredicate = when (filter.fieldType) {
                FieldType.INTEGER -> {
                    val fieldValue = Dragon.getIntegerFieldPath(root, filter.field)
                    val targetValue = filter.target.toLongOrNull()
                    filter.filterSign!!.compareNumbers(criteriaBuilder, fieldValue, targetValue)
                }
                FieldType.NUMBER -> {
                    val fieldValue = Dragon.getNumberFieldPath(root, filter.field)
                    val targetValue = filter.target.toDoubleOrNull()
                    filter.filterSign!!.compareNumbers(criteriaBuilder, fieldValue, targetValue)
                }
                FieldType.DATE -> {
                    val fieldValue = Dragon.getDateFieldPath(root, filter.field)
                    val targetValue = filter.parseTargetDate()
                    filter.filterSign!!.compareDates(criteriaBuilder, fieldValue, targetValue)
                }
                else -> {
                    val fieldValue = Dragon.getStringFieldPath(root, filter.field)
                    filter.filterSign!!.compareStrings(criteriaBuilder, fieldValue, filter.target)
                }
            }

            predicate = predicate?.let { criteriaBuilder.and(it, filterPredicate) } ?: filterPredicate
        }

        return predicate
    }
}