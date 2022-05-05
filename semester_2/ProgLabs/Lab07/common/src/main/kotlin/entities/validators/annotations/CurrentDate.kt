package entities.validators.annotations

/**
 * Annotation above the Date field, says that this field must store current datetime
 *
 * @author Matthew I.
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class CurrentDate
