package utils

import manager.interfaces.CollectionManagerInterface
import entities.Route
import entities.validators.FieldValidator
import entities.validators.annotations.InputField
import entities.validators.annotations.CurrentDate
import entities.validators.annotations.UniqueId

import kotlin.ClassCastException
import java.lang.reflect.Field
import java.util.*
import kotlin.collections.ArrayList


/**
 * Class to create entity object from object map using Reflection API
 *
 * @param collectionManager Is needed to generate new object with unique id
 *
 * @see CollectionManagerInterface
 * @author Matthew I.
 */
class CreateEntityFromMap(private val collectionManager: CollectionManagerInterface<Route>) {
    /**
     * Creates an object (instance of target class) and returns pair
     *  ( Success < Boolean >; < An instance of target class or Error message on failure >)
     *
     * @return Pair<Boolean, Any> with the structure shown above
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> getObjectInstanceFromMap(target_class: Class<T>, objectMap: Map<String, Any?>): Pair<Boolean, Any> {
        val currentObjectMap = objectMap.toMutableMap()

        val notPresentedInputFields = ArrayList<String>()
        val fieldsToGenerate = ArrayList<Field>()

        for (field in target_class.declaredFields) {
            if (!field.isAnnotationPresent(InputField::class.java)) {
                fieldsToGenerate.add(field)
                continue
            }

            // If object map doesn't have input field
            if (!currentObjectMap.containsKey(field.name)) {
                notPresentedInputFields.add(field.name)
                continue
            }

            val ann = field.getAnnotation(InputField::class.java)

            if (!ann.isPrimitiveType) {
                // Recursive check field object map is correct
                val status = try {
                    getObjectInstanceFromMap(field.type, currentObjectMap[field.name] as Map<String, Any?>)
                } catch (e: ClassCastException) {
                    null
                }

                if (status == null)
                    notPresentedInputFields.add(field.name)
                else if (!status.first)
                    return status
                else
                    // Set in current map object instance instead object map
                    currentObjectMap[field.name] = status.second

            } else {
                // Validate input field value is correct
                val checkSuccess = FieldValidator(field).checkRestrictions(objectMap[field.name])

                if (!checkSuccess.first)
                    notPresentedInputFields.add(field.name)
            }
        }

        if (notPresentedInputFields.isNotEmpty())
            return Pair(
                false,
                "Fields \"${notPresentedInputFields.joinToString()}\" are not presented or incorrect in Map"
            )

        fieldsToGenerate.forEach { field ->
            if (!currentObjectMap.containsKey(field.name))
                currentObjectMap[field.name] = generateField(field)
        }

        val result = try {
            target_class.getConstructor(Map::class.java).newInstance(currentObjectMap)
        } catch (e: IllegalArgumentException) {
            "Constructor with HashMap argument in class ${target_class.simpleName} not found!"
        } catch (e: IllegalAccessException) {
            "Can't get access to constructor of class ${target_class.simpleName}!"
        }

        return Pair(result !is String, result!!)
    }

    /**
     * Method to generate field value (by annotations above field)
     *
     * @param field The field to process
     * @return Field value
     *
     * @author Matthew I.
     */
    private fun generateField(field: Field): Any? {
        logger.debug("Generate field \"${field.name}\"")

        if (field.isAnnotationPresent(UniqueId::class.java)) {
            val maxOther: Int = collectionManager.getEntitiesSet().map { route ->
                route.javaClass.getDeclaredField(field.name).apply { isAccessible = true }.getInt(route)
            }.maxByOrNull{ it } ?: 0

            return maxOther + 1
        } else if (field.isAnnotationPresent(CurrentDate::class.java))
            return Date()

        return null
    }
}