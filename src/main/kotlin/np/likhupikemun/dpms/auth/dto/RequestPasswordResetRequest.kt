package np.likhupikemun.dpms.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class RequestPasswordResetRequest(
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Please provide a valid email address")
    val email: String
)
