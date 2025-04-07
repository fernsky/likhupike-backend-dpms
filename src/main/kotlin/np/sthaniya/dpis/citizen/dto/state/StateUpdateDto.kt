package np.sthaniya.dpis.citizen.dto.state

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import np.sthaniya.dpis.citizen.domain.entity.CitizenState

/**
 * DTO for updating citizen state.
 *
 * @property state The new state to set for the citizen
 * @property note Optional note providing context for the state change
 */
data class StateUpdateDto(
    @field:NotNull(message = "State is required")
    val state: CitizenState,
    
    @field:Size(max = 500, message = "Note must be less than 500 characters")
    val note: String? = null
)
