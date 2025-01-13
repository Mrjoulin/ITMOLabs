package com.joulin.dragonservice.dto

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Expression
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Predicate
import java.time.LocalDateTime

enum class FilterSign(val sign: String) {
    EQUAL("="),
    NOT_EQUAL_1("!="),
    NOT_EQUAL_2("<>"),
    GREATER(">"),
    EQUAL_OR_GREATER(">="),
    LESS("<"),
    EQUAL_OR_LESS("<=");

    fun compareNumbers(criteriaBuilder: CriteriaBuilder, n1: Path<out Number>?, n2: Number?): Predicate {
        return when (this) {
            EQUAL -> criteriaBuilder.equal(n1, n2)
            NOT_EQUAL_1, NOT_EQUAL_2 -> criteriaBuilder.notEqual(n1, n2)
            GREATER -> criteriaBuilder.gt(n1, n2)
            EQUAL_OR_GREATER -> criteriaBuilder.ge(n1, n2)
            LESS -> criteriaBuilder.lt(n1, n2)
            EQUAL_OR_LESS -> criteriaBuilder.le(n1, n2)
        }
    }

    fun compareDates(criteriaBuilder: CriteriaBuilder, n1: Path<LocalDateTime>?, n2: LocalDateTime?): Predicate {
        return when (this) {
            EQUAL -> criteriaBuilder.equal(n1, n2)
            NOT_EQUAL_1, NOT_EQUAL_2 -> criteriaBuilder.notEqual(n1, n2)
            GREATER -> criteriaBuilder.greaterThan(n1, n2)
            EQUAL_OR_GREATER -> criteriaBuilder.greaterThanOrEqualTo(n1, n2)
            LESS -> criteriaBuilder.lessThan(n1, n2)
            EQUAL_OR_LESS -> criteriaBuilder.lessThanOrEqualTo(n1, n2)
        }
    }

    fun compareStrings(criteriaBuilder: CriteriaBuilder, n1: Path<String>?, n2: String): Predicate? {
        return when (this) {
            EQUAL -> criteriaBuilder.equal(n1, n2)
            NOT_EQUAL_1, NOT_EQUAL_2 -> criteriaBuilder.notEqual(n1, n2)
            else -> null
        }
    }
}