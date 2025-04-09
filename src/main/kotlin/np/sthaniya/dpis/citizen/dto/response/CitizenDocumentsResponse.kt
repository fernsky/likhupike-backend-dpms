package np.sthaniya.dpis.citizen.dto.response

import io.swagger.v3.oas.annotations.media.Schema

/**
 * Data transfer object representing all documents for a citizen including their verification states.
 */
@Schema(
    description = "Complete document information for a citizen with verification states",
    title = "Citizen Documents Response"
)
data class CitizenDocumentsResponse(
    @Schema(
        description = "Citizen's photo document details"
    )
    val photo: DocumentDetailsResponse? = null,
    
    @Schema(
        description = "Front side of citizenship certificate details"
    )
    val citizenshipFront: DocumentDetailsResponse? = null,
    
    @Schema(
        description = "Back side of citizenship certificate details"
    )
    val citizenshipBack: DocumentDetailsResponse? = null
)
