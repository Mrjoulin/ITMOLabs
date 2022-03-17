package receiver.interfaces

import java.io.InputStreamReader

/**
 * Interface of Any object Receiver which describes the behavior of a receiver that manages a collection of objects.
 *
 * @author Matthew I.
 */
interface ReceiverInterface<T: Any> {
    fun getInputStreamReader(): InputStreamReader

    fun setInputStreamReader(newInputStreamReader: InputStreamReader)

    fun getCreationDate(): String

    fun getEntitiesSet(): HashSet<T>

    fun addNewEntity(entity: T?) : Boolean

    fun removeEntity(entity: T?) : Boolean

    fun setEntitiesSet(entitiesSet: HashSet<T>)
}