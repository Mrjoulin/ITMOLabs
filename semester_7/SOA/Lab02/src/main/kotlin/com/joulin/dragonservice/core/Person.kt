package com.joulin.dragonservice.core

import com.joulin.dragonservice.core.enums.Color
import com.joulin.dragonservice.core.enums.Country
import com.joulin.dragonservice.dto.FieldType
import com.joulin.dragonservice.exceptions.ValidationException
import jakarta.persistence.*
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Root
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import java.time.LocalDateTime

@Embeddable
data class Person(
    @NotBlank
    var name: String,
    @Positive
    var weight: Int,
    @Enumerated(EnumType.STRING) var eyeColor: Color,
    @Enumerated(EnumType.STRING) var nationality: Country
) {
    companion object {
        fun getFields(): Map<String, FieldType> {
            return mapOf(
                "name" to FieldType.STRING,
                "weight" to FieldType.INTEGER,
                "eyeColor" to FieldType.ENUM,
                "nationality" to FieldType.ENUM
            )
        }

        fun getEnumValues(fieldName: String) : List<String> {
            return when(fieldName) {
                "eyeColor" -> Color.values().map { it.name }
                "nationality" -> Country.values().map { it.name }
                else -> listOf()
            }
        }

        fun getFromString(personString: String): Person {
            val personMap = mutableMapOf(
                "name" to "",
                "weight" to "",
                "eyeColor" to "",
                "nationality" to ""
            )
            var nextArg = ""
            personString.split(',').forEach {
                if (personMap.containsKey(it) && nextArg.isEmpty())
                    nextArg = it
                else if (nextArg.isNotEmpty()) {
                    personMap[nextArg] = it
                    nextArg = ""
                } else
                    throw ValidationException("Incorrect argument: $it")
            }

            val fields = getFields()
            personMap.forEach{ (k, v) ->
                if (v.isEmpty())
                    throw ValidationException("Argument not given: $k")
                if (fields[k] == FieldType.NUMBER && (v.toIntOrNull() == null || v.toInt() <= 0))
                    throw ValidationException("Validation exception: $k should be positive integer")
                if (fields[k] == FieldType.ENUM && !getEnumValues(k).contains(v))
                    throw ValidationException("Validation exception: $k should be correct enum")
            }

            return Person(
                name = personMap["name"]!!,
                weight = personMap["weight"]!!.toInt(),
                eyeColor = Color.valueOf(personMap["eyeColor"]!!),
                nationality = Country.valueOf(personMap["nationality"]!!)
            )
        }

        fun getIntegerFieldPath(root: Path<Person>, fieldName: String): Path<Long>? {
            return when(fieldName) {
                "weight" -> root.get("weight")
                else -> null
            }
        }

        fun getNumberFieldPath(root: Path<Person>, fieldName: String): Path<Double>? {
            return null
        }

        fun getDateFieldPath(root: Path<Person>, fieldName: String): Path<LocalDateTime>? {
            return null
        }

        fun getStringFieldPath(root: Path<Person>, fieldName: String): Path<String>? {
            return when(fieldName) {
                "name" ->  root.get("name")
                "eyeColor" ->  root.get("eyeColor")
                "nationality" -> root.get("nationality")
                else -> null
            }
        }
    }

    fun getFieldNumberValueByName(fieldName: String): Double? {
        return when(fieldName) {
            "weight" -> weight.toDouble()
            else -> null
        }
    }

    fun getFieldStringValueByName(fieldName: String): String? {
        return when(fieldName) {
            "name" -> name
            "eyeColor" -> eyeColor.name
            "nationality" -> nationality.name
            else -> null
        }
    }

    fun getFieldDateValueByName(fieldName: String): LocalDateTime? = null
}