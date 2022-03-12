package receiver

import entities.Route
import utils.logger
import utils.objectMap

import java.util.*
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

/**
 * Class Receiver that manages a collection of objects and info about it.
 * Implements ReceiverInterface
 *
 * @param inputStreamReader The reader from which the collection receives data
 *
 * @author Matthew I.
 */
class RouteReceiver(
    private var inputStreamReader: InputStreamReader = InputStreamReader(System.`in`)
) : ReceiverInterface<Route> {

    private val executeScriptPath = ArrayList<String>()
    private val creationDate: String = SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(Date())
    private var entitiesSet = HashSet<Route>()


    init {
        logger.debug("Collection creation date set: $creationDate")
    }

    override fun getInputStreamReader(): InputStreamReader = inputStreamReader

    override fun setInputStreamReader(newInputStreamReader: InputStreamReader) {
        logger.debug(
            "Change new input stream reader: ${inputStreamReader.javaClass.simpleName} " +
            "-> ${newInputStreamReader.javaClass.simpleName}"
        )
        this.inputStreamReader = newInputStreamReader
    }

    override fun addToExecuteScriptPath(filename: String) : Boolean {
        if (!executeScriptPath.contains(filename))
            return executeScriptPath.add(filename)
        return false
    }

    override fun removeFromExecuteScriptPath(filename: String) : Boolean {
        return executeScriptPath.remove(filename)
    }

    override fun getCreationDate(): String = creationDate

    override fun getEntitiesSet(): HashSet<Route> = entitiesSet

    override fun addNewEntity(entity: Route?): Boolean {
        if (entity != null) {
            logger.debug("Add new entity to collection: ${objectMap(entity)}")
            return this.entitiesSet.add(entity)
        }

        return false
    }

    override fun removeEntity(entity: Route?): Boolean {
        if (entity != null) {
            logger.debug("Remove entity from collection: ${objectMap(entity)}")
            return this.entitiesSet.remove(entity)
        }

        return false
    }

    override fun setEntitiesSet(entitiesSet: HashSet<Route>) {
        logger.debug("Set entities collection with ${entitiesSet.size} entities")

        this.entitiesSet = entitiesSet
    }
}