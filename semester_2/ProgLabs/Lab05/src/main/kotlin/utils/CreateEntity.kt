package utils

import entities.Route
import entities.validators.FieldValidator
import entities.validators.annotations.*
import receiver.ReceiverInterface

import java.util.*
import java.io.FileReader
import java.io.IOException
import java.lang.reflect.Field
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


/**
 * Class to create new entity (Route) using Reflection API and annotations above the fields.
 *
 * @param receiver Receiver object to generate value for field with UniqueID annotation
 *
 * @author Matthew I.
 */
class CreateEntity(private val receiver: ReceiverInterface<Route>) {
    private val input = receiver.getInputStreamReader()
    private val inputFromFile = input.javaClass == FileReader::class.java

    /**
     * Method to create an object of given target class using Reflection API.
     *
     * The algorithm goes through all the fields of the target class and, if the field is annotated by
     * InputField annotation, then the value of this field is receiving from the getUserInputField() method,
     * else the field is added to the generated.
     * After receiving all input fields, the generated fields are receiving from the generateField() method,
     * if the previous Object was not passed, otherwise the values of the previous Object are set.
     * After that, the target class constructor is receiving and its object is created.
     *
     * @param target_class The class to create an object.
     * @param previousObject Map with fields names and fields values of object of target class, if field value
     *                       is not primitive, then another map. Object to update his input fields.
     *
     * @return New instance of target class
     *
     * @author Matthew I.
     */
    fun <T> getObjectInstanceFromInput(target_class: Class<T>, previousObject: Map<String, Any?>? = null) : T? {
        val fieldsValues = HashMap<String, Any?>()
        val fieldsToGenerate = ArrayList<Field>()

        logger.debug("Start creating new object of class \"${target_class.simpleName}\"")

        // Get input fields values
        target_class.declaredFields.forEach { field ->
            if (field.isAnnotationPresent(InputField::class.java))
                fieldsValues[field.name] = getUserInputField(field, previousObject) ?: return null
            else
                fieldsToGenerate.add(field)
        }

        // Generate fields after all Input fields are ready
        fieldsToGenerate.forEach { field ->
            if (previousObject == null)
                fieldsValues[field.name] = generateField(field) ?: return null
            else
                fieldsValues[field.name] = previousObject[field.name]
        }

        return try {
            target_class.getConstructor(fieldsValues.javaClass).newInstance(fieldsValues)
        } catch (e: IllegalArgumentException) {
            println("Constructor with HashMap argument in class ${target_class.simpleName} not found!"); null
        } catch (e: IllegalAccessException) {
            println("Can't get access to constructor of class ${target_class.simpleName}!"); null
        }
    }

    /**
     * Method to get field value from input (file or console)
     *
     * If field value is not primitive, than recursive call method getObjectInstanceFromInput()
     * Else get value from input and check field restrictions by RouteValidator
     *
     * @param field The field to process
     * @param previousObject Map with fields names and fields values of object of target class, if field value
     *                       is not primitive, then another map. Object to update his input fields.
     *
     * @return Checked field value
     *
     * @author Matthew I.
     */
    private fun getUserInputField(field: Field, previousObject: Map<String, Any?>? = null): Any? {
        val ann = field.getAnnotation(InputField::class.java) ?: return null
        // Generate field restrictions (by annotations)
        val fieldValidator = FieldValidator(field)
        val restrictions = fieldValidator.getFieldRestrictions()

        logger.debug("Get new field \"${field.name}\", restrictions $restrictions")

        // If field type is not primitive (Number, String, Date)
        if (!ann.isPrimitiveType) {
            if (!inputFromFile) println("Input object of ${ann.description} ($restrictions)")

            @Suppress("UNCHECKED_CAST")
            val previousObjectVal = previousObject?.get(field.name) as? Map<String, Any?>

            // Recursive get object of this field type
            return getObjectInstanceFromInput(field.type, previousObjectVal)
        }

        // Trying to get field value until user input is correct
        while (true) {
            // Get string of previous object field value
            val quota = if (field.type == String::class.java) "\"" else ""
            val oldValue = if (previousObject != null) ", old value $quota${previousObject[field.name]}$quota" else ""

            // Get user input
            val userInput: ArrayList<String>?
            try {
                userInput = getInput(input, "Input ${ann.description} ($restrictions$oldValue): ")
            } catch (e: IOException) {
                return null
            }

            if (userInput != null && userInput.size > 1) {
                println(
                    "Too many values for ${ann.description}! " +
                    if (quota.isNotEmpty()) "If you want to type several words use \"\"" else ""
                )
                if (!inputFromFile) continue else return null
            }

            // Validate input value for this field
            val value = if (userInput != null) userInput[0] else null
            val typedValue = fieldValidator.checkRestrictionsFromString(value)

            if (typedValue.first)
                return typedValue.second
            else if (inputFromFile)
                // Interrupt processing if field value received from file and not correct
                return null
        }
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
            val maxOther: Int = receiver.getEntitiesSet().map { route ->
                route.javaClass.getDeclaredField(field.name).apply { isAccessible = true }.getInt(route)
            }.maxByOrNull{ it } ?: 0

            return maxOther + 1
        } else if (field.isAnnotationPresent(CurrentDate::class.java))
            return Date()

        return null
    }
}