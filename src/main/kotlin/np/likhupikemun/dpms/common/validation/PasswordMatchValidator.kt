package np.likhupikemun.dpis.common.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import np.likhupikemun.dpis.auth.dto.ResetUserPasswordRequest

class PasswordMatchValidator : ConstraintValidator<PasswordMatch, ResetUserPasswordRequest> {
    override fun isValid(request: ResetUserPasswordRequest?, context: ConstraintValidatorContext?): Boolean {
        if (request == null) return true
        return request.newPassword == request.confirmPassword
    }
}
