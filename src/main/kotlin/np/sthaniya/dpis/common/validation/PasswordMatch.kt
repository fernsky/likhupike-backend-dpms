package np.sthaniya.dpis.common.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

/**
 * Validation annotation to verify that two password fields in a class match.
 *
 * This annotation should be applied at the class level to verify password fields match.
 * The validation logic is implemented in [PasswordMatchValidator].
 *
 * Example usage:
 * ```
 * @PasswordMatch
 * data class PasswordChangeRequest(
 *     val password: String,
 *     val confirmPassword: String
 * )
 * ```
 *
 * @property message The error message to be used when validation fails
 * @property groups The validation groups to which this constraint belongs
 * @property payload The payload with which the constraint declaration is associated
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PasswordMatchValidator::class])
annotation class PasswordMatch(
    val message: String = "Passwords do not match",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
