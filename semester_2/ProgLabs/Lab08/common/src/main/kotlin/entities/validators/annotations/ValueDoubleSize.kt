package entities.validators.annotations

/**
 * Annotation above the Double field, set limits on value of field: grater_than < field value < lower_than
 *
 * @param greater_than value, that field must be greater
 * @param lower_than value, that field must be lower
 *
 * @author Matthew I.
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class ValueDoubleSize(
    val greater_than: Double = Double.NEGATIVE_INFINITY,
    val lower_than: Double = Double.POSITIVE_INFINITY
)