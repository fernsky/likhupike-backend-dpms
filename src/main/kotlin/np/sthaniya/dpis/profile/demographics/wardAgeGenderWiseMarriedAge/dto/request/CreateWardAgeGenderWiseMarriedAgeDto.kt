package np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import np.sthaniya.dpis.profile.demographics.common.model.Gender
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.model.MarriedAgeGroup

/**
 * DTO for creating ward-age-gender-wise married age data.
 *
 * @property wardNumber The ward number (must be positive)
 * @property ageGroup The age group at marriage
 * @property gender The gender
 * @property population The population count (must be non-negative)
 */
data class CreateWardAgeGenderWiseMarriedAgeDto(
        @field:NotNull(message = "Ward number is required")
        @field:Min(value = 1, message = "Ward number must be positive")
        val wardNumber: Int,

        @field:NotNull(message = "Age group is required")
        val ageGroup: MarriedAgeGroup,

        @field:NotNull(message = "Gender is required")
        val gender: Gender,

        @field:NotNull(message = "Population is required")
        @field:Min(value = 0, message = "Population must be non-negative")
        val population: Int
)
