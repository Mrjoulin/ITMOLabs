package file

import entities.Route
import receiver.ReceiverInterface
import utils.checkCollectionFile
import utils.COLLECTION_FILENAME
import utils.logger

import java.io.File
import java.io.PrintWriter
import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.security.AnyTypePermission


/**
 * Class to process (load and save) collection file, given in environment variable
 * File must be in XML format and using the library xStream, class parse file and write to it
 *
 * @param receiver Receiver object to add entities from file and get entities to save in file
 *
 * @author Matthew I.
 */
class CollectionFileProcess(private val receiver: ReceiverInterface<Route>) {
    private val xStream = XStream()

    init {
        xStream.addPermission(AnyTypePermission.ANY)
        xStream.alias("routes", RouteCollection::class.java)
        xStream.alias("route", Route::class.java)
        xStream.addImplicitCollection(RouteCollection::class.java, "routes")
    }

    fun load() : Boolean {
        val file = getFile() ?: return false

        try {
            val routesCollection = xStream.fromXML(file.readText()) as RouteCollection

            // Check that routes in file are correct
            if (routesCollection.routes != null) {
                val success = CollectionValidator(routes = routesCollection.routes!!).checkRoutes()

                if (!success) return false
            }

            // Add routes to entities set
            routesCollection.routes?.forEach { route ->
                val success = receiver.addNewEntity(route)

                if (!success) println("Route not added from file: $route")
            }

            return true
        } catch (e: Exception) {
            logger.error("Error while processing collection file: {}", e)
            println("Collection file in wrong format so not processed")

            return false
        }
    }

    fun save() : Boolean {
        val file = getFile() ?: return false

        try {
            // Get all entities in collection and sort by id
            val entitiesList = receiver.getEntitiesSet().sortedBy { it.id }
            val routeCollection = RouteCollection(entitiesList)

            // Get XML collection view
            val xmlCollectionData = xStream.toXML(routeCollection)

            // Write to the collection file
            PrintWriter(file).apply {
                write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
                print(xmlCollectionData)
                close()
            }

            return true
        } catch (e: Exception) {
            logger.error("Error while saving collection to file: {}", e)
            println("Can't save collection to file!")

            return false
        }
    }

    private fun getFile() : File? {
        val checkSuccess = checkCollectionFile()

        if (!checkSuccess.first) {
            println(checkSuccess.second)
            return null
        }

        return File(COLLECTION_FILENAME!!)
    }

    private class RouteCollection(var routes: List<Route>?)
}
