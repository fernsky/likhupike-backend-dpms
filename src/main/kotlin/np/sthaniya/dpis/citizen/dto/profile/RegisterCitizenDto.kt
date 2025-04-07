package np.sthaniya.dpis.citizen.dto.profile

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.*
import np.sthaniya.dpis.common.validation.ValidPassword
import java.time.LocalDate

/**
 * Data transfer object for citizen self-registration.
 * Contains the essential fields required for a citizen to register in the system.
 * 
 * When a citizen registers through this process, their account will be created in 
 * the PENDING_REGISTRATION state and require verification by an administrator.
 */
@Schema(
    description = "Request payload for citizen self-registration",
    title = "Register Citizen Request",
    requiredProperties = ["name", "nameDevnagari", "citizenshipNumber", "citizenshipIssuedDate", 
                          "citizenshipIssuedOffice", "password", "confirmPassword"]
)
data class RegisterCitizenDto(
    @Schema(
        description = "Full name of the citizen in English",
        example = "Hari Prasad Sharma",
        required = true
    )
    @field:NotBlank(message = "Name is required")
    val name: String,
    
    @Schema(
        description = "Full name of the citizen in Devanagari script",
        example = "हरि प्रसाद शर्मा",
        required = true
    )
    @field:NotBlank(message = "Name in Devanagari script is required")
    val nameDevnagari: String,
    
    @Schema(
        description = "Unique citizenship number",
        example = "123-456-78901",
        required = true
    )
    @field:NotBlank(message = "Citizenship number is required")
    val citizenshipNumber: String,
    
    @Schema(
        description = "Date when citizenship was issued",
        example = "2010-05-15",
        required = true
    )
    @field:NotNull(message = "Citizenship issued date is required")
    @field:Past(message = "Citizenship issued date must be in the past")
    val citizenshipIssuedDate: LocalDate,
    
    @Schema(
        description = "Office that issued the citizenship",
        example = "District Administration Office, Kathmandu",
        required = true
    )
    @field:NotBlank(message = "Citizenship issued office is required")
    val citizenshipIssuedOffice: String,
    
    @Schema(
        description = """
            Password for the citizen's account. Must meet the following criteria:
            - At least 8 characters long
            - Contains at least one digit
            - Contains at least one lowercase letter
            - Contains at least one uppercase letter
            - Contains at least one special character (@#$%^&+=)
        """,
        example = "StrongP@ss123",
        required = true
    )
    @field:NotBlank(message = "Password is required")
    @field:ValidPassword
    val password: String,
    
    @Schema(
        description = "Confirmation of the password - must match password field",
        example = "StrongP@ss123",
        required = true
    )
    @field:NotBlank(message = "Confirm password is required")
    val confirmPassword: String,
    
    @Schema(
        description = "Email address for the citizen",
        example = "hari.sharma@example.com"
    )
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Please provide a valid email address")
    val email: String,
    
    @Schema(
        description = "Phone number",
        example = "+977 9801234567"
    )
    val phoneNumber: String? = null
) {
    /**
     * Validates that password and confirmation match.
     * @return true if passwords match, false otherwise
     */
    @AssertTrue(message = "Passwords do not match")
    fun isPasswordValid(): Boolean = password == confirmPassword
}