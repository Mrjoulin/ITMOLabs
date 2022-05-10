package manager

import db.DataBaseWorker
import entities.Route
import manager.interfaces.CollectionManagerInterface
import utils.logger
import utils.objectMap
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * Class Collection Manager that manages a collection of objects and info about it.
 * Implements CollectionManagerInterface
 *
 * @see CollectionManagerInterface
 * @see DataBaseWorker
 * @see Route
 * @see ReentrantReadWriteLock
 *
 * @author Matthew I.
 */
class CollectionManager : CollectionManagerInterface<Route> {
    private val dbWorker: DataBaseWorker = DataBaseWorker()
    private var entitiesSet = HashSet<Route>()
    private val creationDate: String = SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(Date())
    private val lock: ReadWriteLock = ReentrantReadWriteLock(true)

    init {
        logger.info("Collection creation date set: $creationDate")
    }

    override fun getCreationDate(): String = creationDate

    override fun getDBWorker(): DataBaseWorker = dbWorker

    override fun getEntitiesSet(): HashSet<Route> {
        lock.readLock().lock() // Lock writing

        val data = entitiesSet

        lock.readLock().unlock()
        return data
    }

    override fun loadEntitiesSet(): Boolean {
        lock.writeLock().lock() // Lock reading and writing

        entitiesSet += dbWorker.getCollection() ?: return false

        lock.writeLock().unlock()
        return true
    }

    override fun addNewEntity(entity: Route?): Boolean {
        if (entity != null) {
            lock.writeLock().lock() // Lock reading and writing

            logger.info("Add new entity to collection: ${objectMap(entity)}")

            var success = dbWorker.addNewEntity(entity)

            success = success && this.entitiesSet.add(entity)

            lock.writeLock().unlock()
            return success
        }

        return false
    }

    override fun removeEntity(entity: Route?): Boolean {
        if (entity != null) {
            lock.writeLock().lock() // Lock reading and writing

            logger.info("Remove entity from collection: ${objectMap(entity)}")

            var success = dbWorker.removeEntityById(entityId = entity.id)

            success = success && this.entitiesSet.remove(entity)

            lock.writeLock().unlock()
            return success
        }

        return false
    }

    override fun clearUserEntities(username: String) : Boolean {
        lock.writeLock().lock() // Lock reading and writing

        logger.info("Clear user $username entities collection!")

        val success = dbWorker.clearEntitiesByAuthor(username)

        if (success)
            this.entitiesSet = entitiesSet.filter { it.author != username }.toHashSet()

        lock.writeLock().unlock()
        return success
    }
}