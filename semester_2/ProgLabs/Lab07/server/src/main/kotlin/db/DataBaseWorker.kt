package db

import entities.Route
import entities.User
import utils.DATABASE_USERS_TABLE
import utils.dbObjectMap
import utils.getHiddenToken
import utils.logger
import java.lang.reflect.Field
import java.sql.*
import kotlin.system.exitProcess

/**
 * Class to work with PostgreSQL database.
 * On initialization connect to DB and create necessary tables and sequences if they not exist.
 *
 * @see DataBaseConnection
 * @see DataBaseInit
 * @see Route
 * @see User
 *
 * @author Matthew I.
 */
class DataBaseWorker {
    private val db: Connection

    init {
        try {
            // Get connection to db
            db = DataBaseConnection().connect() ?: throw SQLException("Can't get connection to DataBase")

            // Init db
            logger.info("Start initializing db...")
            DataBaseInit(db).initialize()
        } catch (e: SQLException) {
            logger.error("Error while initializing DataBase worker:", e)
            logger.info("Exit server..")

            exitProcess(2)
        }
    }

    /**
     * Entity methods
     */

    fun getCollection(): ArrayList<Route>? {
        return try {
            val statement = arrayOf(SELECT_ALL, FROM_ENTITIES).joinToString(" ")

            val preparedStatement = db.prepareStatement(statement)

            val resSet = preparedStatement.executeQuery()

            createObjectsFromResultSet(resSet, Route::class.java, ENTITIES_COLUMNS_NAMES)
        } catch (e: SQLException) {
            logger.error("SQL problem with taking all collection!", e)
            null
        }
    }

    fun generateId() : Int? {
        try {
            val sqlRequest = arrayOf(SELECT, NEXT_ID).joinToString(" ")
            // 3 attempts max
            for (i in 1..3) {
                val statement: Statement = db.createStatement()
                val resultSet: ResultSet = statement.executeQuery(sqlRequest)

                if (resultSet.next())
                    return resultSet.getInt(1)
            }
        } catch (e: SQLException) {
            logger.warn("SQL problem with generating id!", e)
        }

        return null
    }

    fun getEntityById(entityId: Int): Route? {
        val statement = arrayOf(SELECT_ALL, FROM_ENTITIES, BY_ID).joinToString(" ")

        return try {
            val preparedStatement: PreparedStatement = db.prepareStatement(statement)

            preparedStatement.setInt(1, entityId)

            val resSet: ResultSet = preparedStatement.executeQuery()

            getObjectFromResultSet(resSet, Route::class.java, ENTITIES_COLUMNS_NAMES, checkNext = true)
        } catch (e: SQLException) {
            logger.error("SQL problem with getting entity by id $entityId!", e); null
        }
    }

    fun addNewEntity(entity: Route): Boolean {
        val statement = arrayOf(INSERT_TO_ENTITIES, ENTITIES_TABLE_SIGNATURE, ENTITIES_VALUES).joinToString(" ")

        try {
            val preparedStatement: PreparedStatement = db.prepareStatement(statement)

            val success = setObjectToStatement(entity, ENTITIES_COLUMNS_NAMES, preparedStatement)

            if (!success) return false

            preparedStatement.executeUpdate()

            return true
        } catch (e: SQLException) {
            logger.error("SQL problem with adding new entity! Entity:\n$entity", e)

            return false
        }
    }

    fun removeEntityById(entityId: Int): Boolean {
        if (getEntityById(entityId) == null) return false

        val statement = arrayOf(DELETE, FROM_ENTITIES, BY_ID).joinToString(" ")

        return try {
            val preparedStatement: PreparedStatement = db.prepareStatement(statement)

            preparedStatement.setInt(1, entityId)

            preparedStatement.executeUpdate()
            true
        } catch (e: SQLException) {
            logger.error("SQL problem with removing entity $entityId!", e)
            false
        }
    }

    fun clearEntitiesByAuthor(authorUsername: String): Boolean {
        val statement = arrayOf(DELETE, FROM_ENTITIES, BY_AUTHOR).joinToString(" ")

        return try {
            val preparedStatement: PreparedStatement = db.prepareStatement(statement)

            preparedStatement.setString(1, authorUsername)

            preparedStatement.executeUpdate()
            true
        } catch (e: SQLException) {
            logger.error("SQL problem with clearing entities of user $authorUsername!", e)
            false
        }
    }

    /**
     * User methods
     */

    fun getUserByToken(token: String): User? {
        val statement = arrayOf(SELECT_ALL, FROM_USERS, BY_TOKEN).joinToString(" ")

        return try {
            val preparedStatement: PreparedStatement = db.prepareStatement(statement)

            preparedStatement.setString(1, token)

            val resSet: ResultSet = preparedStatement.executeQuery()

            getObjectFromResultSet(resSet, User::class.java, USERS_COLUMNS_NAMES, checkNext = true)
        } catch (e: SQLException) {
            logger.error("SQL problem with getting user by token ${getHiddenToken(token)}!", e); null
        }
    }

    fun getUserByUsername(username: String, password: String? = null) : User? {
        val statement = arrayOf(
            SELECT_ALL, FROM_USERS, if (password != null) BY_USERNAME_AND_PASSWORD else BY_USERNAME
        ).joinToString(" ")

        return try {
            val preparedStatement: PreparedStatement = db.prepareStatement(statement)

            preparedStatement.setString(1, username)

            if (password != null)
                preparedStatement.setString(2, password)

            val resSet: ResultSet = preparedStatement.executeQuery()

            getObjectFromResultSet(resSet, User::class.java, USERS_COLUMNS_NAMES, checkNext = true)
        } catch (e: SQLException) {
            logger.error("SQL problem with getting user by username and password: $username $password!", e); null
        }
    }

    fun addNewUser(user: User): Boolean {
        val statement = arrayOf(INSERT_TO_USERS, USERS_TABLE_SIGNATURE, USERS_VALUES).joinToString(" ")

        try {
            val preparedStatement: PreparedStatement = db.prepareStatement(statement)

            val success = setObjectToStatement(user, USERS_COLUMNS_NAMES, preparedStatement)

            if (!success) return false

            preparedStatement.executeUpdate()

            return true
        } catch (e: SQLException) {
            logger.error("SQL problem with adding new user! User:\n$user", e)

            return false
        }
    }

    fun updateUserToken(userWithNewToken: User): Boolean {
        val statement = arrayOf(UPDATE, DATABASE_USERS_TABLE, SET_TOKEN_AND_EXPIRES, BY_USERNAME_AND_PASSWORD)
            .joinToString(" ")

        return try {
            val preparedStatement: PreparedStatement = db.prepareStatement(statement)

            preparedStatement.setString(1, userWithNewToken.token)
            preparedStatement.setDate(2, userWithNewToken.tokenExpires)
            preparedStatement.setString(3, userWithNewToken.username)
            preparedStatement.setString(4, userWithNewToken.password)

            preparedStatement.executeUpdate()
            true
        } catch (e: SQLException) {
            logger.error("Error while updating user ${userWithNewToken.username} token and tokenExpires!", e)
            false
        }
    }

    /**
     * Common/utility methods
     */

    private fun <T : Any> setObjectToStatement(
        obj: T,
        columns: ArrayList<String>,
        statement: PreparedStatement
    ): Boolean {
        val entityMap = dbObjectMap(obj)

        var numSetColumns = 0

        for (colName in entityMap.keys) {
            val columnNumber = columns.indexOf(colName) + 1

            if (columnNumber == 0) {
                logger.error("Can't find column $colName on db columns")
                return false
            }
            val success = setValueInStatementForColumn(columnNumber, entityMap[colName], statement)

            if (!success) {
                logger.error("Can't set value for $colName: ${entityMap[colName]?.javaClass}")
                return false
            }

            numSetColumns += 1
        }

        if (numSetColumns != columns.size) {
            logger.error("Set not all columns for statement! Set $numSetColumns of ${columns.size}")
            return false
        }

        return true
    }

    private fun setValueInStatementForColumn(colNumber: Int, value: Any?, statement: PreparedStatement): Boolean {
        when (value?.javaClass) {
            Byte::class.java, java.lang.Byte::class.java -> statement.setByte(colNumber, value as Byte)
            Short::class.java, java.lang.Short::class.java -> statement.setShort(colNumber, value as Short)
            Int::class.java, java.lang.Integer::class.java -> statement.setInt(colNumber, value as Int)
            Long::class.java, java.lang.Long::class.java -> statement.setLong(colNumber, value as Long)
            Float::class.java, java.lang.Float::class.java -> statement.setFloat(colNumber, value as Float)
            Double::class.java, java.lang.Double::class.java -> statement.setDouble(colNumber, value as Double)
            String::class.java -> statement.setString(colNumber, value as String)
            Date::class.java -> statement.setDate(colNumber, value as Date)
            else -> return false
        }

        return true
    }

    private fun <T : Any> createObjectsFromResultSet(
        resSet: ResultSet, objClass: Class<T>, columns: ArrayList<String>
    ): ArrayList<T>? {

        val objectList = ArrayList<T>()

        while (resSet.next()) {
            objectList.add(getObjectFromResultSet(resSet, objClass, columns) ?: return null)
        }

        return objectList
    }

    private fun <T: Any> getObjectFromResultSet(
        resSet: ResultSet, objClass: Class<T>, columns: List<String>, checkNext: Boolean = false
    ): T? {
        if (checkNext)
            // If result set has next element
            if (!resSet.next()) return null

        val objectMap = HashMap<String, Any?>()

        for (field in objClass.declaredFields) {
            if (field.name in columns) {
                val colNumber = columns.indexOf(field.name) + 1

                objectMap[field.name] = getValueForFieldFromResultSet(colNumber, field, resSet)

                continue
            }

            val nestedObjectColumns: List<String> = columns.map { cur ->
                if (cur.startsWith(field.name))
                    cur.removePrefix(field.name).replaceFirstChar { it.lowercase() }
                else ""
            }

            if (nestedObjectColumns.isEmpty()) {
                logger.error("No data for field ${field.name}!")
                return null
            } else {
                objectMap[field.name] = getObjectFromResultSet(resSet, field.type, nestedObjectColumns) ?: return null
            }
        }

        return try {
           objClass.getConstructor(Map::class.java).newInstance(objectMap)
        } catch (e: IllegalArgumentException) {
            logger.error("Constructor with HashMap argument in class ${objClass.simpleName} not found!"); null
        } catch (e: IllegalAccessException) {
            logger.error("Can't get access to constructor of class ${objClass.simpleName}!"); null
        }
    }

    private fun getValueForFieldFromResultSet(colNumber: Int, field: Field, resSet: ResultSet): Any? {
        return when (field.type) {
            Byte::class.java -> resSet.getByte(colNumber)
            Short::class.java -> resSet.getShort(colNumber)
            Int::class.java -> resSet.getInt(colNumber)
            Long::class.java -> resSet.getLong(colNumber)
            Float::class.java -> resSet.getFloat(colNumber)
            Double::class.java -> resSet.getDouble(colNumber)
            String::class.java -> resSet.getString(colNumber)
            Date::class.java, java.util.Date::class.java -> resSet.getDate(colNumber)
            else -> null
        }
    }
}