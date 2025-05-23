package np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.request

import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.model.LanguageType

/**
 * DTO for filtering ward-wise mother tongue population data.
 *
 * @property wardNumber Optional ward number filter
 * @property languageType Optional language type filter
 */
data class WardWiseMotherTonguePopulationFilterDto(
        val wardNumber: Int? = null,
        val languageType: LanguageType? = null
)
