package entities.validators

import entities.validators.annotations.*

import java.lang.ClassCastException
import java.lang.reflect.Field
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.jvm.kotlinProperty


/**
 * Class to check restrictions and validates field value by annotations above the field using Reflection API
 *
 * @param field Field to validate
 *
 * @author Matthew I.
 */
class FieldValidator (private val field: Field) {

    fun getFieldRestrictions(): String {
        val restrictions = ArrayList<String>()
        restrictions.add(field.type.simpleName)

        field.annotations.forEach { ann: Annotation ->
            if (ann is ValueIntSize) {
                if (ann.greater_than > Int.MIN_VALUE) {
                    if (field.type != String::class.java) restrictions.add("min ${ann.greater_than + 1}")
                    else restrictions.add("min length ${ann.greater_than + 1}")
                }
                if (ann.lower_than < Int.MAX_VALUE) {
                    if (field.type != String::class.java) restrictions.add("max ${ann.lower_than - 1}")
                    else restrictions.add("max length ${ann.lower_than - 1}")
                }
            }
            if (ann is ValueDoubleSize) {
                if (ann.greater_than > Double.NEGATIVE_INFINITY)
                    restrictions.add("greater than ${ann.greater_than}")
                if (ann.lower_than < Double.POSITIVE_INFINITY)
                    restrictions.add("lower than ${ann.lower_than}")
            }
        }

        return restrictions.joinToString()
    }

    fun checkRestrictionsFromString(check_value: String?): Pair<Boolean, Any?> {
        // Return pair <success: Boolean, typedValue: Any?>

        checkValueNull(check_value).apply { if (!first) return this }

        try {
            // Cast given value to field type
            val value = check_value!!.parseStringTo(field.type) ?: throw ClassCastException()

            return checkRestrictions(value)
        } catch (e: ClassCastException) {
            println("Wrong object type for ${field.name} (expected ${field.type.simpleName})!")

            return Pair(false, null)
        }
    }

    fun checkRestrictions(value: Any?): Pair<Boolean, Any?> {
        // Return pair <success: Boolean, typedValue: Any?>

        checkValueNull(value).apply { if (!first) return this }

        try {
            // Check annotations
            field.declaredAnnotations.forEach { ann: Annotation ->
                val check = when(ann) {
                    is ValueIntSize -> checkIntValueSize(
                        if (value !is String) value as Number else value.length, ann
                    )
                    is ValueDoubleSize -> checkDoubleValueSize(value as Number, ann)
                    else -> true
                }

                if (!check) {
                    println("Incorrect value! Check ${field.name} restrictions!")

                    return Pair(false, null)
                }
            }

            return Pair(true, value)
        } catch (e: ClassCastException) {
            println("Wrong object type for ${field.name} (expected ${field.type.simpleName})!")

            return Pair(false, null)
        }
    }

    fun checkValueNull(check_value: Any?) : Pair<Boolean, Any?> {
        if (check_value == null) {
            val nullable = field.kotlinProperty!!.returnType.isMarkedNullable

            if (!nullable) println("Field ${field.name} can't be null!")

            return Pair(nullable, check_value)
        }

        return Pair(true, null)
    }

    private fun checkIntValueSize(value: Number, ann: ValueIntSize) : Boolean {
        return ann.greater_than < value.toInt() && value.toInt() < ann.lower_than
    }

    private fun checkDoubleValueSize(value: Number, ann: ValueDoubleSize) : Boolean {
        return ann.greater_than < value.toDouble() && value.toDouble() < ann.lower_than
    }

    private fun String.parseStringTo(field_type: Class<*>): Any? {
        if (field_type == String::class.java) return this
        if (field_type == Byte::class.java) return this.toByteOrNull()
        if (field_type == Short::class.java) return this.toShortOrNull()
        if (field_type == Int::class.java) return this.toIntOrNull()
        if (field_type == Long::class.java) return this.toLongOrNull()
        if (field_type == Float::class.java) return this.toFloatOrNull()
        if (field_type == Double::class.java) return this.toDoubleOrNull()
        if (field_type == Date::class.java) {
            return try{
                LocalDate.parse(this, DateTimeFormatter.ofPattern("d/M/yyyy hh:mm:ss"))
            } catch (e: DateTimeParseException){ null }
        }
        return null
    }
}
