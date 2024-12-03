package com.joulin.dragonservice.core

import com.joulin.dragonservice.dto.FieldType
import jakarta.persistence.Embeddable
import jakarta.persistence.criteria.Path
import jakarta.validation.Constraint
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import java.time.LocalDateTime

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
data class Coordinates(
    var x: Double,
    var y: Double
) {
    companion object {
        fun getFields(): Map<String, FieldType> {
            return mapOf("x" to FieldType.NUMBER, "y" to FieldType.NUMBER)
        }

        fun getEnumValues(fieldName: String) : List<String> {
            return listOf()
        }

        fun getIntegerFieldPath(root: Path<Coordinates>, fieldName: String): Path<Long>? {
            return null
        }

        fun getNumberFieldPath(root: Path<Coordinates>, fieldName: String): Path<Double>? {
            return when(fieldName) {
                "x" -> root.get("x")
                "y" -> root.get("y")
                else -> null
            }
        }
        fun getDateFieldPath(root: Path<Person>, fieldName: String): Path<LocalDateTime>? {
            return null
        }

        fun getStringFieldPath(root: Path<Person>, fieldName: String): Path<String>? {
            return null
        }
    }

    fun getFieldNumberValueByName(fieldName: String): Double? {
        return when(fieldName) {
            "x" -> x
            "y" -> y
            else -> null
        }
    }

    fun getFieldStringValueByName(fieldName: String): String? = null

    fun getFieldDateValueByName(fieldName: String): LocalDateTime? = null
}