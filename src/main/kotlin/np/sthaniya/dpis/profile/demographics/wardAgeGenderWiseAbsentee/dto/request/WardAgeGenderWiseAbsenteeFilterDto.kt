package np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.dto.request

import np.sthaniya.dpis.profile.demographics.common.model.Gender
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.model.AbsenteeAgeGroup

/**
 * DTO for filtering ward-age-gender-wise absentee data.
 *
 * @property wardNumber Optional ward number filter
 * @property ageGroup Optional age group filter
 * @property gender Optional gender filter
 */
data class WardAgeGenderWiseAbsenteeFilterDto(
        val wardNumber: Int? = null,
        val ageGroup: AbsenteeAgeGroup? = null,
        val gender: Gender? = null
)
