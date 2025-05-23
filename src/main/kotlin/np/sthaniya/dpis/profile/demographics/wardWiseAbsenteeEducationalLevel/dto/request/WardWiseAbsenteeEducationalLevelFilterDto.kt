package np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.request

import np.sthaniya.dpis.profile.demographics.common.model.EducationalLevelType

/**
 * DTO for filtering ward-wise absentee educational level data.
 *
 * @property wardNumber Optional ward number filter
 * @property educationalLevel Optional educational level filter
 */
data class WardWiseAbsenteeEducationalLevelFilterDto(
        val wardNumber: Int? = null,
        val educationalLevel: EducationalLevelType? = null
)
