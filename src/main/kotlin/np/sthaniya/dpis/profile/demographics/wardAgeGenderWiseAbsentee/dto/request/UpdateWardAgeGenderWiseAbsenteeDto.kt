package np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import np.sthaniya.dpis.profile.demographics.common.model.Gender
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.model.AbsenteeAgeGroup

/**
 * DTO for updating ward-age-gender-wise absentee data.
 *
 * @property wardNumber The ward number (must be positive)
 * @property ageGroup The age group for absentee population
 * @property gender The gender for absentee population
 * @property population The absentee population count (must be non-negative)
 */
data class UpdateWardAgeGenderWiseAbsenteeDto(
        @field:NotNull(message = "Ward number is required")
        @field:Min(value = 1, message = "Ward number must be positive")
        val wardNumber: Int,

        @field:NotNull(message = "Age group is required")
        val ageGroup: AbsenteeAgeGroup,

        @field:NotNull(message = "Gender is required")
        val gender: Gender,

        @field:NotNull(message = "Population is required")
        @field:Min(value = 0, message = "Population must be non-negative")
        val population: Int
)
