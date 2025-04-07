package np.sthaniya.dpis.citizen.dto.state

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import np.sthaniya.dpis.citizen.domain.entity.DocumentState

/**
 * DTO for updating document state.
 *
 * @property state The new state to set for the document
 * @property note Optional note providing context for the state change
 */
data class DocumentStateUpdateDto(
    @field:NotNull(message = "Document state is required")
    val state: DocumentState,
    
    @field:Size(max = 255, message = "Note must be less than 255 characters")
    val note: String? = null
)
