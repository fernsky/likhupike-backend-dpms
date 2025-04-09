package np.sthaniya.dpis.citizen.dto.response

import io.swagger.v3.oas.annotations.media.Schema

/**
 * Data transfer object representing document URLs for a citizen.
 */
@Schema(
    description = "URLs for citizen documents",
    title = "Document URL Response"
)
data class DocumentUrlResponse(
    @Schema(
        description = "URL to the citizen's photo",
        example = "https://storage.example.com/citizens/photos/550e8400-e29b-41d4-a716-446655440000.jpg?token=abc123"
    )
    val photo: String? = null,
    
    @Schema(
        description = "URL to the front side of the citizen's citizenship document",
        example = "https://storage.example.com/citizens/citizenship/front/550e8400-e29b-41d4-a716-446655440000.jpg?token=abc123"
    )
    val citizenshipFront: String? = null,
    
    @Schema(
        description = "URL to the back side of the citizen's citizenship document",
        example = "https://storage.example.com/citizens/citizenship/back/550e8400-e29b-41d4-a716-446655440000.jpg?token=abc123"
    )
    val citizenshipBack: String? = null
)
