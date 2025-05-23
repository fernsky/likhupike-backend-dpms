package np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.request

import np.sthaniya.dpis.profile.demographics.common.model.Gender
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.model.MarriedAgeGroup

/**
 * DTO for filtering ward-age-gender-wise married age data.
 *
 * @property wardNumber Optional ward number filter
 * @property ageGroup Optional age group filter
 * @property gender Optional gender filter
 */
data class WardAgeGenderWiseMarriedAgeFilterDto(
        val wardNumber: Int? = null,
        val ageGroup: MarriedAgeGroup? = null,
        val gender: Gender? = null
)
