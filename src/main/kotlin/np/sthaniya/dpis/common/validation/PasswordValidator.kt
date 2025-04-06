package np.sthaniya.dpis.common.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import np.sthaniya.dpis.common.service.I18nMessageService
import org.springframework.stereotype.Component

/**
 * Validator for the ValidPassword annotation.
 * 
 * This validator checks that passwords meet security requirements and provides
 * detailed, localized error messages.
 *
 * @property i18nMessageService Service for retrieving localized messages
 */
@Component
class PasswordValidator(
    private val i18nMessageService: I18nMessageService
) : ConstraintValidator<ValidPassword, String?> {
    
    private var optional: Boolean = false

    override fun initialize(constraintAnnotation: ValidPassword) {
        optional = constraintAnnotation.optional
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value.isNullOrBlank()) {
            return optional
        }

        val validations = listOf(
            value.length >= 8 to "validation.password.missing.length",
            value.contains(Regex("[0-9]")) to "validation.password.missing.digit",
            value.contains(Regex("[a-z]")) to "validation.password.missing.lowercase",
            value.contains(Regex("[A-Z]")) to "validation.password.missing.uppercase",
            value.contains(Regex("[@#$%^&+=]")) to "validation.password.missing.special"
        )

        val failedValidations = validations.filterNot { it.first }
            .map { i18nMessageService.getMessage(it.second) }

        if (failedValidations.isNotEmpty()) {
            context.disableDefaultConstraintViolation()
            context.buildConstraintViolationWithTemplate(
                i18nMessageService.getMessage(
                    "validation.password.requirements",
                    arrayOf(failedValidations.joinToString(", "))
                )
            ).addConstraintViolation()
            return false
        }

        return true
    }
}
