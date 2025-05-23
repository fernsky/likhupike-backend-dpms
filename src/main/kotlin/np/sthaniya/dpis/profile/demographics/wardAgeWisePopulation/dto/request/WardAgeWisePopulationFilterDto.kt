package np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.request

import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.model.AgeGroup
import np.sthaniya.dpis.profile.demographics.common.model.Gender

/**
 * DTO for filtering ward-age-wise population data.
 *
 * @property wardNumber Optional ward number filter
 * @property ageGroup Optional age group filter
 * @property gender Optional gender filter
 */
data class WardAgeWisePopulationFilterDto(
        val wardNumber: Int? = null,
        val ageGroup: AgeGroup? = null,
        val gender: Gender? = null
)
