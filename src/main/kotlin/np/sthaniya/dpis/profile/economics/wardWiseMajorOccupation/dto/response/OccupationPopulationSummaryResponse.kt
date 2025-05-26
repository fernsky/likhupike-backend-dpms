package np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.response

import np.sthaniya.dpis.profile.economics.common.model.OccupationType

/**
 * Response DTO for summarized occupation population data.
 *
 * @property occupation The type of occupation
 * @property totalPopulation The total population for this occupation type
 */
data class OccupationPopulationSummaryResponse(
        val occupation: OccupationType,
        val totalPopulation: Long
)
