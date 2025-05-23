package np.sthaniya.dpis.profile.demographics.wardWiseReligionPopulation.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import np.sthaniya.dpis.profile.demographics.wardWiseReligionPopulation.model.ReligionType

/**
 * DTO for updating ward-wise religion population data.
 *
 * @property wardNumber The ward number (must be positive)
 * @property religionType The type of religion
 * @property population The population count (must be non-negative)
 */
data class UpdateWardWiseReligionPopulationDto(
        @field:NotNull(message = "Ward number is required")
        @field:Min(value = 1, message = "Ward number must be positive")
        val wardNumber: Int,
        @field:NotNull(message = "Religion type is required") val religionType: ReligionType,
        @field:NotNull(message = "Population is required")
        @field:Min(value = 0, message = "Population must be non-negative")
        val population: Int
)
