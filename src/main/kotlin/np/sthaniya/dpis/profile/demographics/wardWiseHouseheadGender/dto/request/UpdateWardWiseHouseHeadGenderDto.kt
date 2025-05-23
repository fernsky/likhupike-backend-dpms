package np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import np.sthaniya.dpis.profile.demographics.common.model.Gender

/**
 * DTO for updating ward-wise house head gender data.
 *
 * @property wardNumber The ward number (must be positive)
 * @property wardName Optional ward name for reference
 * @property gender The gender of household heads
 * @property population The population count (must be non-negative)
 */
data class UpdateWardWiseHouseHeadGenderDto(
        @field:NotNull(message = "Ward number is required")
        @field:Min(value = 1, message = "Ward number must be positive")
        val wardNumber: Int,

        val wardName: String? = null,

        @field:NotNull(message = "Gender is required")
        val gender: Gender,

        @field:NotNull(message = "Population is required")
        @field:Min(value = 0, message = "Population must be non-negative")
        val population: Int
)
