package np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.request

import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.model.MaritalAgeGroup
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.model.MaritalStatusGroup

/**
 * DTO for filtering ward age-wise marital status data.
 *
 * @property wardNumber Optional ward number filter
 * @property ageGroup Optional age group filter
 * @property maritalStatus Optional marital status filter
 */
data class WardAgeWiseMaritalStatusFilterDto(
    val wardNumber: Int? = null,
    val ageGroup: MaritalAgeGroup? = null,
    val maritalStatus: MaritalStatusGroup? = null
)
