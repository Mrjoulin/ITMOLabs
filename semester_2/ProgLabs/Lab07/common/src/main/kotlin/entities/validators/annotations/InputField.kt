package entities.validators.annotations


/**
 * Annotation above the Any field, says that this field must be input from user
 *
 * @param description field description (may be used to show user while input)
 * @param isPrimitiveType Says Is this field value is primitive? (Primitives: Number, String, Date)
 *
 * @author Matthew I.
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class InputField(val description: String, val isPrimitiveType: Boolean = true)
