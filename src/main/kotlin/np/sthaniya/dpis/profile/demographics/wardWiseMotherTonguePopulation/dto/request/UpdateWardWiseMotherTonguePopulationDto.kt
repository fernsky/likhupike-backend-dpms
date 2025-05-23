package np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.model.LanguageType

/**
 * DTO for updating ward-wise mother tongue population data.
 *
 * @property wardNumber The ward number (must be positive)
 * @property languageType The type of language
 * @property population The population count (must be non-negative)
 */
data class UpdateWardWiseMotherTonguePopulationDto(
        @field:NotNull(message = "Ward number is required")
        @field:Min(value = 1, message = "Ward number must be positive")
        val wardNumber: Int,
        @field:NotNull(message = "Language type is required") val languageType: LanguageType,
        @field:NotNull(message = "Population is required")
        @field:Min(value = 0, message = "Population must be non-negative")
        val population: Int
)
