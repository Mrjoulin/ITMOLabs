package manager.interfaces

import db.DataBaseWorker

/**
 * Interface of Any object Collection Manager which describes the behavior of a collectionManager that manages a collection of objects.
 *
 * @see DataBaseWorker
 *
 * @author Matthew I.
 */
interface CollectionManagerInterface<T: Any> {
    fun getCreationDate(): String

    fun getDBWorker(): DataBaseWorker

    fun getEntitiesSet(): HashSet<T>

    fun loadEntitiesSet(): Boolean

    fun addNewEntity(entity: T?) : Boolean

    fun removeEntity(entity: T?) : Boolean

    fun clearUserEntities(username: String) : Boolean
}