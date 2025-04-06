package np.sthaniya.dpis.common.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

/**
 * Validation annotation for ensuring password strength requirements.
 * 
 * Validates that a password meets security requirements:
 * - Minimum length of 8 characters
 * - Contains at least one digit
 * - Contains at least one lowercase letter
 * - Contains at least one uppercase letter
 * - Contains at least one special character (@#$%^&+=)
 * 
 * @property message Default error message
 * @property optional Whether null or empty passwords are valid
 * @property groups Validation groups, for grouping related constraints
 * @property payload Used by clients to assign custom payload objects to a constraint
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PasswordValidator::class])
annotation class ValidPassword(
    val message: String = "Password does not meet security requirements",
    val optional: Boolean = false,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
