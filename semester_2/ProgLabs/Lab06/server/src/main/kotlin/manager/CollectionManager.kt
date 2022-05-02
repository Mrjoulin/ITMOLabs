package manager

import entities.Route
import manager.interfaces.CollectionManagerInterface
import utils.logger
import utils.objectMap

import java.util.*
import java.text.SimpleDateFormat
import kotlin.collections.HashSet

/**
 * Class Collection Manager that manages a collection of objects and info about it.
 * Implements CollectionManagerInterface
 *
 * @see CollectionManagerInterface
 * @see Route
 * @author Matthew I.
 */
class CollectionManager : CollectionManagerInterface<Route> {
    private val creationDate: String = SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(Date())
    private var entitiesSet = HashSet<Route>()
    private var collectionChanged: Boolean = false

    init {
        logger.info("Collection creation date set: $creationDate")
    }

    override fun getCreationDate(): String = creationDate

    override fun isCollectionChanged(): Boolean = this.collectionChanged

    override fun setCollectionChanged(collectionChanged: Boolean) {
        this.collectionChanged = collectionChanged
    }

    override fun getEntitiesSet(): HashSet<Route> = entitiesSet

    override fun addNewEntity(entity: Route?): Boolean {
        if (entity != null) {
            logger.info("Add new entity to collection: ${objectMap(entity)}")

            val success = this.entitiesSet.add(entity)

            collectionChanged = success

            return success
        }

        return false
    }

    override fun removeEntity(entity: Route?): Boolean {
        if (entity != null) {
            logger.info("Remove entity from collection: ${objectMap(entity)}")

            val success = this.entitiesSet.remove(entity)

            collectionChanged = success

            return success
        }

        return false
    }

    override fun setEntitiesSet(entitiesSet: HashSet<Route>) {
        logger.info("Set entities collection with ${entitiesSet.size} entities")

        this.entitiesSet = entitiesSet

        collectionChanged = true
    }
}