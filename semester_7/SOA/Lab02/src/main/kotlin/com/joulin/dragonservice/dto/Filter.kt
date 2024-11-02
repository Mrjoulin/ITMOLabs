package com.joulin.dragonservice.dto

import com.joulin.dragonservice.core.Dragon
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.DateTimeParseException

data class Filter(
    val field: String,
    val sign: String,
    val target: String
) {
    private val possibleSingsForFieldTypes = mapOf(
        FieldType.STRING to listOf(FilterSign.EQUAL, FilterSign.NOT_EQUAL_1, FilterSign.NOT_EQUAL_2),
        FieldType.ENUM to listOf(FilterSign.EQUAL, FilterSign.NOT_EQUAL_1, FilterSign.NOT_EQUAL_2),
        FieldType.INTEGER to FilterSign.values().toList(),
        FieldType.NUMBER to FilterSign.values().toList(),
        FieldType.DATE to FilterSign.values().toList()
    )
    val fieldType: FieldType? = Dragon.getFields()[field]
    val filterSign: FilterSign? = FilterSign.values().filter { it.sign == sign }.getOrNull(0)

    fun isCorrect(): Boolean {
        if (fieldType == null || filterSign == null) return false
        if (!possibleSingsForFieldTypes[fieldType]!!.contains(filterSign)) return false
        if (fieldType == FieldType.INTEGER && target.toLongOrNull() == null) return false
        if (fieldType == FieldType.NUMBER && target.toDoubleOrNull() == null) return false
        if (fieldType == FieldType.DATE && parseTargetDate() == null) return false
        if (fieldType == FieldType.ENUM && !Dragon.getEnumValues(field).contains(target)) return false
        return true
    }

    fun parseTargetDate(): LocalDateTime? {
        val possibleFormats = listOf(
            DateTimeFormatter.ISO_DATE, DateTimeFormatter.ISO_DATE_TIME,
            DateTimeFormatter.ISO_LOCAL_DATE, DateTimeFormatter.ISO_LOCAL_DATE_TIME,
            DateTimeFormatterBuilder()
                .parseCaseInsensitive().append(DateTimeFormatter.ISO_LOCAL_DATE).appendLiteral(' ')
                .append(DateTimeFormatter.ISO_LOCAL_TIME).toFormatter(),
            DateTimeFormatterBuilder()
                .parseCaseInsensitive().append(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                .appendLiteral('T').append(DateTimeFormatter.ISO_LOCAL_TIME).toFormatter(),
            DateTimeFormatterBuilder()
                .parseCaseInsensitive().append(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                .appendLiteral(' ').append(DateTimeFormatter.ISO_LOCAL_TIME).toFormatter(),
        )
        possibleFormats.forEach {format ->
            try {
                return LocalDateTime.parse(target, format).also {
                    println("Find correct datetime format: $format")
                }
            } catch (e: DateTimeParseException) {
                try {
                    return LocalDate.parse(target, format).atStartOfDay().also {
                        println("Find correct date format: $format")
                    }
                } catch (e: DateTimeParseException) {
                    println("Date format $format not fits: ${e.message}")
                }
            }
        }
        return null
    }
}