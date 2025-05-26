package np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.request

import np.sthaniya.dpis.profile.economics.common.model.OccupationType

/**
 * DTO for filtering ward-wise major occupation data.
 *
 * @property wardNumber Optional ward number filter
 * @property occupation Optional occupation type filter
 */
data class WardWiseMajorOccupationFilterDto(
        val wardNumber: Int? = null,
        val occupation: OccupationType? = null
)
