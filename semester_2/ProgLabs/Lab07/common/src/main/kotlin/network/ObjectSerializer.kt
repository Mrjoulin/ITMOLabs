package network

import java.io.*
import java.lang.Exception

/**
 * Class to serialize and de-serialize object to/from ByteArray
 *
 * @see Request
 * @see Response
 *
 * @author Matthew I.
 */
class ObjectSerializer {
    companion object {
        fun toByteArray(obj: Serializable): ByteArray {
            val byteArrayOutputStream = ByteArrayOutputStream()
            val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)

            objectOutputStream.writeObject(obj)
            objectOutputStream.flush()

            val result = byteArrayOutputStream.toByteArray()

            byteArrayOutputStream.close()
            objectOutputStream.close()

            return result
        }

        @Suppress("UNCHECKED_CAST")
        fun <T : Serializable> fromByteArray(byteArray: ByteArray): T? {
            try {
                val byteArrayInputStream = ByteArrayInputStream(byteArray)
                val objectInput = ObjectInputStream(byteArrayInputStream)

                val result = objectInput.readObject() as T

                objectInput.close()
                byteArrayInputStream.close()

                return result
            } catch (e: Exception) {
                return null
            }
        }
    }
}