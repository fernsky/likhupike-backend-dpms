package np.sthaniya.dpis.citizen.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import np.sthaniya.dpis.citizen.domain.entity.CitizenState
import np.sthaniya.dpis.citizen.dto.shared.AddressResponse
import java.time.LocalDate
import java.time.Instant
import java.util.UUID

/**
 * Data transfer object representing a citizen entity for API responses.
 * Contains all relevant citizen data for client consumption.
 */
@Schema(
    description = "Citizen information including personal details and addresses",
    title = "Citizen Response"
)
data class CitizenResponse(
    @Schema(
        description = "Unique identifier of the citizen",
        example = "123e4567-e89b-12d3-a456-426614174000"
    )
    val id: UUID,
    
    @Schema(
        description = "Full name of the citizen in English",
        example = "Hari Prasad Sharma"
    )
    val name: String,
    
    @Schema(
        description = "Full name of the citizen in Devanagari script",
        example = "हरि प्रसाद शर्मा"
    )
    val nameDevnagari: String?,
    
    @Schema(
        description = "Unique citizenship number",
        example = "123-456-78901"
    )
    val citizenshipNumber: String?,
    
    @Schema(
        description = "Date when citizenship was issued",
        example = "2010-05-15"
    )
    val citizenshipIssuedDate: LocalDate?,
    
    @Schema(
        description = "Office that issued the citizenship",
        example = "District Administration Office, Kathmandu"
    )
    val citizenshipIssuedOffice: String?,
    
    @Schema(
        description = "Email address for the citizen",
        example = "hari.sharma@example.com"
    )
    val email: String?,
    
    @Schema(
        description = "Phone number",
        example = "+977 9801234567"
    )
    val phoneNumber: String?,
    
    @Schema(
        description = "Permanent address details"
    )
    val permanentAddress: AddressResponse?,
    
    @Schema(
        description = "Temporary or current address details"
    )
    val temporaryAddress: AddressResponse?,
    
    @Schema(
        description = "Name of citizen's father",
        example = "Ram Prasad Sharma"
    )
    val fatherName: String?,
    
    @Schema(
        description = "Name of citizen's grandfather",
        example = "Krishna Prasad Sharma"
    )
    val grandfatherName: String?,
    
    @Schema(
        description = "Name of citizen's spouse, if applicable",
        example = "Sita Sharma"
    )
    val spouseName: String?,
    
    @Schema(
        description = "Current state of citizen in the verification workflow",
        example = "APPROVED",
        enumAsRef = true
    )
    val state: CitizenState,
    
    @Schema(
        description = "Optional note about the current state",
        example = "Missing contact information"
    )
    val stateNote: String?,
    
    @Schema(
        description = "When the state was last updated",
        example = "2023-03-14T10:30:00Z"
    )
    val stateUpdatedAt: Instant?,
    
    @Schema(
        description = "ID of the administrator who last updated the state",
        example = "550e8400-e29b-41d4-a716-446655440000"
    )
    val stateUpdatedBy: UUID?,
    
    @Schema(
        description = "Whether the citizen record has been approved",
        example = "true"
    )
    val isApproved: Boolean,
    
    @Schema(
        description = "When the citizen record was approved",
        example = "2023-03-15T14:30:00Z"
    )
    val approvedAt: Instant?,
    
    @Schema(
        description = "Documents associated with the citizen (photo and citizenship documents) with their verification states",
    )
    val documents: CitizenDocumentsResponse,
    
    @Schema(
        description = "When the citizen record was created",
        example = "2023-03-10T09:15:00Z"
    )
    val createdAt: Instant,
    
    @Schema(
        description = "When the citizen record was last updated",
        example = "2023-03-15T14:30:00Z"
    )
    val updatedAt: Instant?
)
