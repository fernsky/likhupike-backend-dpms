package np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import np.sthaniya.dpis.profile.demographics.common.model.EducationalLevelType

/**
 * DTO for updating ward-wise absentee educational level data.
 *
 * @property wardNumber The ward number (must be positive)
 * @property educationalLevel The educational level
 * @property population The population count (must be non-negative)
 */
data class UpdateWardWiseAbsenteeEducationalLevelDto(
        @field:NotNull(message = "Ward number is required")
        @field:Min(value = 1, message = "Ward number must be positive")
        val wardNumber: Int,
        @field:NotNull(message = "Educational level is required")
        val educationalLevel: EducationalLevelType,
        @field:NotNull(message = "Population is required")
        @field:Min(value = 0, message = "Population must be non-negative")
        val population: Int
)
