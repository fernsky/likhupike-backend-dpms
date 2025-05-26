package np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

/**
 * DTO for creating ward-wise trained population data.
 *
 * @property wardNumber The ward number (must be positive)
 * @property trainedPopulation The trained population count (must be non-negative)
 */
data class CreateWardWiseTrainedPopulationDto(
        @field:NotNull(message = "Ward number is required")
        @field:Min(value = 1, message = "Ward number must be positive")
        val wardNumber: Int,

        @field:NotNull(message = "Trained population is required")
        @field:Min(value = 0, message = "Trained population must be non-negative")
        val trainedPopulation: Int
)
