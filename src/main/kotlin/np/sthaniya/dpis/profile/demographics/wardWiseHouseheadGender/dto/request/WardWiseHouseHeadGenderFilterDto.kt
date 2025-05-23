package np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.request

import np.sthaniya.dpis.profile.demographics.common.model.Gender

/**
 * DTO for filtering ward-wise house head gender data.
 *
 * @property wardNumber Optional ward number filter
 * @property gender Optional gender filter
 */
data class WardWiseHouseHeadGenderFilterDto(
        val wardNumber: Int? = null,
        val gender: Gender? = null
)
