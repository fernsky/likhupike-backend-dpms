package np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.request

/**
 * DTO for filtering ward-wise trained population data.
 *
 * @property wardNumber Optional ward number filter
 */
data class WardWiseTrainedPopulationFilterDto(
        val wardNumber: Int? = null
)
