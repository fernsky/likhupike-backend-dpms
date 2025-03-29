package np.sthaniya.dpis.common.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import np.sthaniya.dpis.auth.dto.ResetUserPasswordRequest

class PasswordMatchValidator : ConstraintValidator<PasswordMatch, ResetUserPasswordRequest> {
    override fun isValid(request: ResetUserPasswordRequest?, context: ConstraintValidatorContext?): Boolean {
        if (request == null) return true
        return request.newPassword == request.confirmPassword
    }
}
