package np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.response

/**
 * Response DTO for ward trained population data.
 *
 * @property wardNumber The ward number
 * @property trainedPopulation The trained population for this ward
 */
data class WardTrainedPopulationResponse(
        val wardNumber: Int,
        val trainedPopulation: Int
)
