package np.sthaniya.dpis.citizen.dto.management

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.*
import np.sthaniya.dpis.citizen.dto.shared.AddressDto
import np.sthaniya.dpis.common.validation.ValidPassword
import java.time.LocalDate
import java.util.UUID

/**
 * Data transfer object for administrative updates to citizen records.
 * Used by system administrators to update existing citizen information.
 */
@Schema(
    description = "Request payload for updating an existing citizen record",
    title = "Update Citizen Request"
)
data class UpdateCitizenDto(
    @Schema(
        description = "Full name of the citizen in English",
        example = "Hari Prasad Sharma"
    )
    val name: String? = null,
    
    @Schema(
        description = "Full name of the citizen in Devanagari script",
        example = "हरि प्रसाद शर्मा"
    )
    val nameDevnagari: String? = null,
    
    @Schema(
        description = "Unique citizenship number",
        example = "123-456-78901"
    )
    val citizenshipNumber: String? = null,
    
    @Schema(
        description = "Date when citizenship was issued",
        example = "2010-05-15"
    )
    val citizenshipIssuedDate: LocalDate? = null,
    
    @Schema(
        description = "Office that issued the citizenship",
        example = "District Administration Office, Kathmandu"
    )
    val citizenshipIssuedOffice: String? = null,
    
    @Schema(
        description = "Email address for the citizen",
        example = "hari.sharma@example.com"
    )
    @field:Email(message = "Please provide a valid email address")
    val email: String? = null,
    
    @Schema(
        description = """
            Optional password for the citizen's account. If provided, must meet the following criteria:
            - At least 8 characters long
            - Contains at least one digit
            - Contains at least one lowercase letter
            - Contains at least one uppercase letter
            - Contains at least one special character (@#$%^&+=)
        """,
        example = "StrongP@ss123"
    )
    @field:ValidPassword(optional = true)
    val password: String? = null,
    
    @Schema(
        description = "Phone number",
        example = "+977 9801234567"
    )
    val phoneNumber: String? = null,
    
    @Schema(
        description = "Permanent address details"
    )
    @field:Valid
    val permanentAddress: AddressDto? = null,
    
    @Schema(
        description = "Temporary or current address details"
    )
    @field:Valid
    val temporaryAddress: AddressDto? = null,
    
    @Schema(
        description = "Name of citizen's father",
        example = "Ram Prasad Sharma"
    )
    val fatherName: String? = null,
    
    @Schema(
        description = "Name of citizen's grandfather",
        example = "Krishna Prasad Sharma"
    )
    val grandfatherName: String? = null,
    
    @Schema(
        description = "Name of citizen's spouse, if applicable",
        example = "Sita Sharma"
    )
    val spouseName: String? = null,
    
    @Schema(
        description = "Whether the citizen record should be approved",
        defaultValue = "null"
    )
    val isApproved: Boolean? = null,
    
    @Schema(
        description = "ID of the administrator who is performing this update",
        example = "550e8400-e29b-41d4-a716-446655440000"
    )
    val updatedBy: UUID? = null
)