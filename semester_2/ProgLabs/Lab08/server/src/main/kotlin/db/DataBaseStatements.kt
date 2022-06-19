package db

import utils.DATABASE_ENTITIES_TABLE
import utils.DATABASE_IDS_SEQUENCE
import utils.DATABASE_USERS_TABLE

/**
 * Database statements (parts of SQL commands)
 */


val ENTITIES_COLUMNS_NAMES = ArrayList<String>()
val USERS_COLUMNS_NAMES = ArrayList<String>()

val ENTITIES_TABLE_SIGNATURE: String
    get() = ENTITIES_COLUMNS_NAMES.joinToString(prefix = "(", postfix = ")")
val USERS_TABLE_SIGNATURE: String
    get() = USERS_COLUMNS_NAMES.joinToString(prefix = "(", postfix = ")")

val ENTITIES_VALUES: String
    get() = ENTITIES_COLUMNS_NAMES.joinToString(prefix = "VALUES(", postfix = ")") { "?" }
val USERS_VALUES: String
    get() = USERS_COLUMNS_NAMES.joinToString(prefix = "VALUES(", postfix = ")") { "?" }

const val CREATE_IDS_SEQUENCE = "CREATE SEQUENCE IF NOT EXISTS $DATABASE_IDS_SEQUENCE"
const val CREATE_ENTITIES_TABLE = "CREATE TABLE IF NOT EXISTS $DATABASE_ENTITIES_TABLE"
const val CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS $DATABASE_USERS_TABLE"

const val SELECT = "SELECT"
const val SELECT_ALL = "$SELECT *"
const val UPDATE = "UPDATE"
const val DELETE = "DELETE"

const val FROM_ENTITIES = "FROM $DATABASE_ENTITIES_TABLE"
const val FROM_USERS = "FROM $DATABASE_USERS_TABLE"

const val INSERT_TO_ENTITIES = "INSERT INTO $DATABASE_ENTITIES_TABLE"
const val INSERT_TO_USERS = "INSERT INTO $DATABASE_USERS_TABLE"

const val BY_ID = "WHERE id = ?"
const val BY_AUTHOR = "WHERE author = ?"
const val BY_TOKEN = "WHERE token = ?"
const val BY_USERNAME = "WHERE username = ?"
const val BY_USERNAME_AND_PASSWORD = "$BY_USERNAME AND password = ?"

const val SET_TOKEN_AND_EXPIRES = "SET token=?, tokenExpires=?"
const val SET_PASSWORD = "SET password=?"

const val NEXT_ID = "nextval('$DATABASE_IDS_SEQUENCE')"
