package db

import utils.DATABASE_LOGIN
import utils.DATABASE_PASSWORD
import utils.DATABASE_URL
import utils.logger

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

/**
 * Class to create connection to database.
 *
 * @see Connection
 *
 * @author Matthew I.
 */
class DataBaseConnection {
    @Throws(SQLException::class)
    fun connect(): Connection? {
        try {
            Class.forName("org.postgresql.Driver")
        } catch (e: ClassNotFoundException) {
            logger.warn("DB driver not found!")
            return null
        }

        return DriverManager.getConnection(DATABASE_URL, DATABASE_LOGIN, DATABASE_PASSWORD)
    }
}
