package entities.validators.annotations

/**
 * Annotation above the String field, says that this field contains information about object author.
 *
 * @author Matthew I.
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Author
