package np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.request

import np.sthaniya.dpis.profile.economics.common.model.SkillType

/**
 * DTO for filtering ward-wise major skills data.
 *
 * @property wardNumber Optional ward number filter
 * @property skill Optional skill type filter
 */
data class WardWiseMajorSkillsFilterDto(
    val wardNumber: Int? = null,
    val skill: SkillType? = null
)
