package np.sthaniya.dpis.common.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import np.sthaniya.dpis.auth.dto.ResetUserPasswordRequest

/**
 * Validator implementation for the [PasswordMatch] constraint annotation.
 *
 * This validator ensures that password fields in a [ResetUserPasswordRequest] match.
 * It is used automatically by the Jakarta Validation framework when the [PasswordMatch]
 * annotation is present on a class.
 */
class PasswordMatchValidator : ConstraintValidator<PasswordMatch, ResetUserPasswordRequest> {
    /**
     * Validates that the password fields in the request match.
     *
     * @param request The request object to validate, containing password fields
     * @param context The constraint validator context
     * @return `true` if passwords match or request is null, `false` otherwise
     */
    override fun isValid(request: ResetUserPasswordRequest?, context: ConstraintValidatorContext?): Boolean {
        if (request == null) return true
        return request.newPassword == request.confirmPassword
    }
}
