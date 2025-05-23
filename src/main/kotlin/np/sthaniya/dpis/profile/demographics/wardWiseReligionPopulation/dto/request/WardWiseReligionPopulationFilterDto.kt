package np.sthaniya.dpis.profile.demographics.wardWiseReligionPopulation.dto.request

import np.sthaniya.dpis.profile.demographics.wardWiseReligionPopulation.model.ReligionType

/**
 * DTO for filtering ward-wise religion population data.
 *
 * @property wardNumber Optional ward number filter
 * @property religionType Optional religion type filter
 */
data class WardWiseReligionPopulationFilterDto(
        val wardNumber: Int? = null,
        val religionType: ReligionType? = null
)
