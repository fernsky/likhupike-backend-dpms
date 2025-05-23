package np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.request

import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.model.CasteType

/**
 * DTO for filtering ward-wise caste population data.
 *
 * @property wardNumber Optional ward number filter
 * @property casteType Optional caste type filter
 */
data class WardWiseCastePopulationFilterDto(
        val wardNumber: Int? = null,
        val casteType: CasteType? = null
)
