package np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.model.CasteType

/**
 * DTO for updating ward-wise caste population data.
 *
 * @property wardNumber The ward number (must be positive)
 * @property casteType The type of caste
 * @property population The population count (must be non-negative)
 */
data class UpdateWardWiseCastePopulationDto(
        @field:NotNull(message = "Ward number is required")
        @field:Min(value = 1, message = "Ward number must be positive")
        val wardNumber: Int,
        @field:NotNull(message = "Caste type is required") val casteType: CasteType,
        @field:NotNull(message = "Population is required")
        @field:Min(value = 0, message = "Population must be non-negative")
        val population: Int
)
