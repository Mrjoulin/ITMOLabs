package entities.validators.annotations

/**
 * Annotation above the Int field, says that this field is ID and must be Unique in collection
 *
 * @author Matthew I.
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class UniqueId
