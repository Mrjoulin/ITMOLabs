package input

import entities.validators.FieldValidator
import entities.validators.annotations.*
import utils.logger

import java.util.*
import java.io.FileReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.reflect.Field
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


/**
 * Class to create new entity (Route) using Reflection API and annotations above the fields.
 *
 * @param input Input Stream Reader to get fields values from user or file
 *
 * @author Matthew I.
 */
class CreateEntityMap(private val input: InputStreamReader) {
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
    fun <T> getObjectMapFromInput(
        target_class: Class<T>, previousObject: Map<String, Any?>? = null
    ) : Map<String, Any?>? {

        val fieldsValues = HashMap<String, Any?>()
        val generateFields = ArrayList<Field>()

        logger.debug("Start creating new object of class \"${target_class.simpleName}\"")

        // Get input fields values
        target_class.declaredFields.forEach { field ->
            if (field.isAnnotationPresent(InputField::class.java))
                fieldsValues[field.name] = getUserInputField(field, previousObject) ?: return null
            else
                generateFields.add(field)
        }

        // Generate fields after all Input fields are ready
        generateFields.forEach { field ->
            if (previousObject != null)
                fieldsValues[field.name] = previousObject[field.name]
        }

        return fieldsValues
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
    @Suppress("UNCHECKED_CAST")
    private fun getUserInputField(field: Field, previousObject: Map<String, Any?>? = null): Any? {
        val ann = field.getAnnotation(InputField::class.java) ?: return null
        // Generate field restrictions (by annotations)
        val fieldValidator = FieldValidator(field)
        val restrictions = fieldValidator.getFieldRestrictions()

        logger.debug("Get new field \"${field.name}\", restrictions $restrictions")

        // If field type is not primitive (Number, String, Date)
        if (!ann.isPrimitiveType) {
            if (!inputFromFile) println("Input object of ${ann.description} ($restrictions)")

            val previousObjectVal = previousObject?.get(field.name) as? Map<String, Any?>

            // Recursive get object of this field type
            return getObjectMapFromInput(field.type, previousObjectVal)
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
}