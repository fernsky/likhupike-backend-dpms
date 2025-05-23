package np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.model.MaritalAgeGroup
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.model.MaritalStatusGroup

/**
 * DTO for updating ward age-wise marital status data.
 *
 * @property wardNumber The ward number (must be positive)
 * @property ageGroup The age group
 * @property maritalStatus The marital status
 * @property population The total population count (must be non-negative)
 * @property malePopulation The male population count (optional, must be non-negative if provided)
 * @property femalePopulation The female population count (optional, must be non-negative if provided)
 * @property otherPopulation The other gender population count (optional, must be non-negative if provided)
 */
data class UpdateWardAgeWiseMaritalStatusDto(
    @field:NotNull(message = "Ward number is required")
    @field:Min(value = 1, message = "Ward number must be positive")
    val wardNumber: Int,

    @field:NotNull(message = "Age group is required")
    val ageGroup: MaritalAgeGroup,

    @field:NotNull(message = "Marital status is required")
    val maritalStatus: MaritalStatusGroup,

    @field:NotNull(message = "Population is required")
    @field:Min(value = 0, message = "Population must be non-negative")
    val population: Int,

    @field:Min(value = 0, message = "Male population must be non-negative")
    val malePopulation: Int? = null,

    @field:Min(value = 0, message = "Female population must be non-negative")
    val femalePopulation: Int? = null,

    @field:Min(value = 0, message = "Other population must be non-negative")
    val otherPopulation: Int? = null
)
