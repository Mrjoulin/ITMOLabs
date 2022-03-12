package file

import entities.Route
import entities.validators.FieldValidator
import entities.validators.annotations.InputField
import entities.validators.annotations.UniqueId

import java.lang.reflect.Field


/**
 * Class to validate routes objects after loading from collection file
 *
 * @param routes List of Routes objects to check restrictions
 *
 * @author Matthew I.
 */
class CollectionValidator (private val routes: List<Route>) {
    fun checkRoutes(): Boolean {
        // Go throw all routes and validate
        routes.forEach { route ->
            val success = ObjectValidator(obj = route).checkObject()

            if (!success) return false
        }

        return true
    }

    /**
     * Class to validate obj using Reflection API
     * Check input and generated fields in correct format
     *
     * @param obj Object to check fields are correct
     *
     * @author Matthew I.
     */
    inner class ObjectValidator (private val obj: Any) {
        private var currentInputFields = HashMap<Field, Any?>()
        private var currentGenerateFields = HashMap<Field, Any?>()

        fun checkObject() : Boolean {
            var success = prepareFields()

            success = success && checkInputFields()
            success = success && checkGenerateFields()

            return success
        }

        private fun prepareFields() : Boolean {
            // Go throw all object fields, check on null and distribute to input and generate
            obj.javaClass.declaredFields.forEach { field ->
                field.isAccessible = true

                var value = field.get(obj)
                if (value != null && value.toString().isEmpty()) value = null

                FieldValidator(field).checkValueNull(value).apply { if (!first) return false }

                if (field.isAnnotationPresent(InputField::class.java))
                    currentInputFields[field] = value
                else
                    currentGenerateFields[field] = value
            }

            return true
        }

        private fun checkInputFields() : Boolean {
            // Go throw all input fields and check them correct
            currentInputFields.forEach { (field, value) ->
                val ann = field.getAnnotation(InputField::class.java)
                val success: Boolean

                // In field type is not primitive, recursive check all fields in this field value object
                if (!ann.isPrimitiveType) {
                    success = value == null || ObjectValidator(obj = value).checkObject()
                } else
                    success = FieldValidator(field = field).checkRestrictions(value = value).first

                if (!success) return false
            }

            return true
        }

        private fun checkGenerateFields(): Boolean {
            // Go throw all generate fields and check them correct
            currentGenerateFields.forEach { (field, value) ->
                if (field.isAnnotationPresent(UniqueId::class.java)) {
                    val otherObjects = routes.map {
                        it.javaClass.getDeclaredField(field.name).apply { isAccessible = true }.getInt(it)
                    }.filter { it == value }

                    if (otherObjects.size > 1) {
                        println("There is several objects in collection with same unique ID!")
                        return false
                    }
                }
            }

            return true
        }
    }
}