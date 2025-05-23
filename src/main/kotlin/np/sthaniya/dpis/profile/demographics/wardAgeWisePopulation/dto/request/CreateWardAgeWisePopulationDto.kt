package np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.model.AgeGroup
import np.sthaniya.dpis.profile.demographics.common.model.Gender

/**
 * DTO for creating ward-age-wise population data.
 *
 * @property wardNumber The ward number (must be positive)
 * @property ageGroup The age group
 * @property gender The gender
 * @property population The population count (must be non-negative)
 */
data class CreateWardAgeWisePopulationDto(
        @field:NotNull(message = "Ward number is required")
        @field:Min(value = 1, message = "Ward number must be positive")
        val wardNumber: Int,
        @field:NotNull(message = "Age group is required") val ageGroup: AgeGroup,
        @field:NotNull(message = "Gender is required") val gender: Gender,
        @field:NotNull(message = "Population is required")
        @field:Min(value = 0, message = "Population must be non-negative")
        val population: Int
)
