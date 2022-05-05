package db

import entities.Route
import entities.User
import entities.validators.annotations.*
import utils.logger
import java.lang.reflect.Field
import java.sql.SQLException
import java.sql.Connection
import java.sql.Statement
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Class to initialize DataBase with default tables and sequences.
 * Creates IDs sequence, entities and users tables. Tables signatures are created by Reflection API.
 *
 * @param connection Connection to database
 *
 * @see Route
 * @see User
 * @see Connection
 *
 * @author Matthew I.
 */
class DataBaseInit (private val connection: Connection) {
    @Throws(SQLException::class)
    fun initialize() {
        val statement: Statement = connection.createStatement()

        // Create ids sequence
        logger.info("Initialize ids sequence")
        statement.executeUpdate("$CREATE_IDS_SEQUENCE START 1")

        // Create table for Routes objects
        logger.info("Initialize routes table")

        val routeColumnsByNames = getSQLRequestToCreateTableForObject(Route::class.java) ?: return

        ENTITIES_COLUMNS_NAMES += routeColumnsByNames.keys
        val routesTableDescription = routeColumnsByNames.values.joinToString()

        logger.debug("Routes table signature:\n$routesTableDescription\nColumns: $ENTITIES_COLUMNS_NAMES")

        statement.executeUpdate("$CREATE_ENTITIES_TABLE ($routesTableDescription)")

        // Create table for Users objects
        logger.info("Initialize users table")

        val usersColumnsByNames = getSQLRequestToCreateTableForObject(User::class.java) ?: return

        USERS_COLUMNS_NAMES += usersColumnsByNames.keys
        val usersTableDescription = usersColumnsByNames.values.joinToString()

        logger.debug("Users table signature:\n$routesTableDescription\nColumns: $USERS_COLUMNS_NAMES")

        statement.executeUpdate("$CREATE_USERS_TABLE ($usersTableDescription)")
    }

    private fun <T> getSQLRequestToCreateTableForObject(
            objectClass: Class<T>, fieldNamePrefix: String = ""
    ) : LinkedHashMap<String, String>? {

        val columnsByNames = LinkedHashMap<String, String>()

        fun correctName(s: String): String {
            val name =  s.replaceFirstChar {
                if (it.isLowerCase() && fieldNamePrefix.isNotEmpty()) it.titlecase(Locale.getDefault())
                else it.toString()
            }
            return fieldNamePrefix + name
        }

        for (field in objectClass.declaredFields) {
            if (field.isAnnotationPresent(InputField::class.java) &&
                    !field.getAnnotation(InputField::class.java).isPrimitiveType) {

                columnsByNames += getSQLRequestToCreateTableForObject(
                    field.type, correctName(field.name)
                ) ?: return null

                continue
            }

            val colName = correctName(field.name)
            val curColumn: ArrayList<String> = arrayListOf(colName)
            val colType = getCorrectColumnType(field)

            if (colType == null) {
                logger.error("Unexpected field type in object ${objectClass.simpleName}: ${field.type.simpleName}")
                return null
            }
            // Add not null to each column
            curColumn.add("$colType NOT NULL")

            // Add restrictions by annotations
            curColumn += getRestrictions(field, colName)

            // Add to columns
            columnsByNames[colName] = curColumn.joinToString(" ")
        }

        return columnsByNames
    }


    private fun getCorrectColumnType(field: Field): String? {
        return when (field.type) {
            Byte::class.java -> "tinyint"
            Short::class.java -> "smallint"
            Int::class.java -> "int"
            Long::class.java -> "bigint"
            Float::class.java -> "real"
            Double::class.java -> "double precision"
            String::class.java -> {
                if (field.isAnnotationPresent(ValueIntSize::class.java)) {
                    val curAnn = field.getAnnotation(ValueIntSize::class.java)
                    if (curAnn.lower_than < Int.MAX_VALUE) "varchar(${curAnn.lower_than})"
                    else "varchar(256)"
                } else "varchar(256)"
            }
            Date::class.java, java.sql.Date::class.java -> "date"
            else -> null
        }
    }

    private fun getRestrictions(field: Field, colName: String) : ArrayList<String> {
        val restrictions = ArrayList<String>()

        for (ann in field.annotations) {
            when (ann) {
                is UniqueId -> restrictions.add("PRIMARY KEY")
                is CurrentDate -> restrictions.add("DEFAULT (current_date)")
                is ValueIntSize -> {
                    if (field.type == String::class.java) continue
                    val curAnn = field.getDeclaredAnnotation(ValueIntSize::class.java)

                    restrictions.add(
                        getValueRestrictionsText(
                            colName, curAnn.lower_than.toDouble(), curAnn.greater_than.toDouble(),
                            Int.MIN_VALUE.toDouble(), Int.MAX_VALUE.toDouble()
                        )
                    )
                }
                is ValueDoubleSize -> {
                    if (field.type == String::class.java) continue
                    val curAnn = field.getDeclaredAnnotation(ValueDoubleSize::class.java)

                    restrictions.add(
                        getValueRestrictionsText(
                            colName, curAnn.lower_than, curAnn.greater_than,
                            Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY
                        )
                    )
                }
            }
        }

        return restrictions
    }

    private fun getValueRestrictionsText(colName: String, lower_than: Double, greater_than: Double,
                                         minValue: Double, maxValue: Double): String {
        val restrictions = ArrayList<String>()

        if (greater_than > minValue)
            restrictions.add("$colName > $greater_than")
        if (lower_than < maxValue)
            restrictions.add("$colName < $lower_than")

        return if (restrictions.isNotEmpty()) {
            "CHECK(${restrictions.joinToString(" AND ")})"
        } else ""
    }
}