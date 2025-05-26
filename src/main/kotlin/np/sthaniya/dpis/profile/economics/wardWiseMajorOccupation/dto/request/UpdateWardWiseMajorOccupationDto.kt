package np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import np.sthaniya.dpis.profile.economics.common.model.OccupationType

/**
 * DTO for updating ward-wise major occupation data.
 *
 * @property wardNumber The ward number (must be positive)
 * @property occupation The type of occupation
 * @property population The population count (must be non-negative)
 */
data class UpdateWardWiseMajorOccupationDto(
        @field:NotNull(message = "Ward number is required")
        @field:Min(value = 1, message = "Ward number must be positive")
        val wardNumber: Int,
        @field:NotNull(message = "Occupation type is required")
        val occupation: OccupationType,
        @field:NotNull(message = "Population is required")
        @field:Min(value = 0, message = "Population must be non-negative")
        val population: Int
)
