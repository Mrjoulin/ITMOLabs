package entities.validators.annotations

/**
 * Annotation above the Int field, set limits on value of field: grater_than < field value < lower_than
 *
 * @param greater_than value, that field must be greater
 * @param lower_than value, that field must be lower
 *
 * @author Matthew I.
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class ValueIntSize(
    val greater_than: Int = Int.MIN_VALUE,
    val lower_than: Int = Int.MAX_VALUE
)