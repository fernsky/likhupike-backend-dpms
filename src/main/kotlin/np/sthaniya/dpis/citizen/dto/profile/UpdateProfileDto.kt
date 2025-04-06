package np.sthaniya.dpis.citizen.dto.profile

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email

/**
 * Data transfer object for citizen self-profile updates.
 * Contains fields that a citizen can update in their own profile.
 */
@Schema(
    description = "Request payload for citizen profile update",
    title = "Update Profile Request"
)
data class UpdateProfileDto(
    @Schema(
        description = "Email address for the citizen",
        example = "hari.sharma@example.com"
    )
    @field:Email(message = "Please provide a valid email address")
    val email: String? = null,
    
    @Schema(
        description = "Phone number",
        example = "+977 9801234567"
    )
    val phoneNumber: String? = null
)