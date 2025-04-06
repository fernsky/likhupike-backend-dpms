package np.sthaniya.dpis.citizen.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

/**
 * Response data for document upload operations.
 * Contains information about the uploaded document and access details.
 */
@Schema(
    description = "Response from document upload operations",
    title = "Document Upload Response"
)
data class DocumentUploadResponse(
    @Schema(
        description = "Unique storage key for the document",
        example = "citizens/photos/550e8400-e29b-41d4-a716-446655440000.jpg"
    )
    val storageKey: String,
    
    @Schema(
        description = "The original filename that was uploaded",
        example = "passport_photo.jpg"
    )
    val originalFilename: String,
    
    @Schema(
        description = "MIME type of the uploaded document",
        example = "image/jpeg"
    )
    val contentType: String,
    
    @Schema(
        description = "Size of the document in bytes",
        example = "256000"
    )
    val size: Long,
    
    @Schema(
        description = "Temporary URL to access the document (valid for limited time)",
        example = "https://storage.example.com/citizens/photos/550e8400-e29b-41d4-a716-446655440000.jpg?token=abc123"
    )
    val url: String,
    
    @Schema(
        description = "When the document was uploaded",
        example = "2023-05-20T14:30:15"
    )
    val uploadedAt: LocalDateTime = LocalDateTime.now()
)