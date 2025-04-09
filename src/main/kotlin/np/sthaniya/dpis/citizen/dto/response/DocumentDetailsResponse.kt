package np.sthaniya.dpis.citizen.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import np.sthaniya.dpis.citizen.domain.entity.DocumentState
import java.time.Instant

/**
 * Data transfer object representing details of a single document with its state information.
 */
@Schema(
    description = "Details of a citizen document including URL and verification state",
    title = "Document Details Response"
)
data class DocumentDetailsResponse(
    @Schema(
        description = "URL to access the document",
        example = "https://storage.example.com/citizens/photos/550e8400-e29b-41d4-a716-446655440000.jpg?token=abc123"
    )
    val url: String? = null,
    
    @Schema(
        description = "Current verification state of the document",
        example = "AWAITING_REVIEW"
    )
    val state: DocumentState? = null,
    
    @Schema(
        description = "Note about the document's verification state",
        example = "Photo is too blurry, please upload a clearer image"
    )
    val note: String? = null,
    
    @Schema(
        description = "When the document was uploaded",
        example = "2025-04-09T10:15:22Z"
    )
    val uploadedAt: Instant? = null
)
