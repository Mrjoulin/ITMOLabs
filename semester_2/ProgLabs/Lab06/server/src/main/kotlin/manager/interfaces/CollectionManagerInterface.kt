package manager.interfaces

/**
 * Interface of Any object Collection Manager which describes the behavior of a collectionManager that manages a collection of objects.
 *
 * @author Matthew I.
 */
interface CollectionManagerInterface<T: Any> {
    fun getCreationDate(): String

    fun isCollectionChanged(): Boolean

    fun setCollectionChanged(collectionChanged: Boolean)

    fun getEntitiesSet(): HashSet<T>

    fun addNewEntity(entity: T?) : Boolean

    fun removeEntity(entity: T?) : Boolean

    fun setEntitiesSet(entitiesSet: HashSet<T>)
}