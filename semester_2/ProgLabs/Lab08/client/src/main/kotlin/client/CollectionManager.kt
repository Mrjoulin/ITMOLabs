package client

import entities.Route
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

class CollectionManager {
    private val entitiesCollection: HashSet<Route> = HashSet()

    private val lock: ReadWriteLock = ReentrantReadWriteLock(true)
    private var collectionInitialized: Boolean = false
    private var collectionChanged: Boolean = false

    fun isCollectionInitialized(): Boolean = collectionInitialized

    fun isCollectionChanged(): Boolean = collectionChanged

    fun collectionChangedProcessed() {
        collectionChanged = false
    }

    fun getEntitiesSet(): HashSet<Route> {
        lock.readLock().lock() // Lock writing

        val data = entitiesCollection

        lock.readLock().unlock()
        return data
    }

    fun initializeCollection(entities: Collection<Route>): Boolean {
        lock.writeLock().lock() // Lock reading and writing

        val success = entitiesCollection.addAll(entities)
        collectionInitialized = true

        lock.writeLock().unlock()

        return success
    }

    fun addNewEntity(entity: Route?): Boolean {
        if (entity != null) {
            lock.writeLock().lock() // Lock reading and writing

            val success = entitiesCollection.add(entity)
            collectionChanged = success

            lock.writeLock().unlock()

            return success
        }
        return false
    }

    fun removeEntityById(entity: Route?): Boolean {
        if (entity != null) {
            lock.writeLock().lock() // Lock reading and writing

            val success = entitiesCollection.removeIf { it.id == entity.id}
            collectionChanged = success

            lock.writeLock().unlock()

            return success
        }

        return false
    }

    fun removeEntitiesByAuthor(entity: Route?): Boolean {
        if (entity != null) {
            lock.writeLock().lock() // Lock reading and writing

            val success = entitiesCollection.removeIf { it.author == entity.author}
            collectionChanged = success

            lock.writeLock().unlock()

            return success
        }

        return false
    }

    fun clearCollection() {
        lock.writeLock().lock() // Lock reading and writing

        entitiesCollection.clear()
        collectionInitialized = false

        lock.writeLock().unlock()
    }
}