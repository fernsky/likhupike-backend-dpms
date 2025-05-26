package np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.response

import np.sthaniya.dpis.profile.economics.common.model.SkillType

/**
 * Response DTO for summarized skill population data.
 *
 * @property skill The type of skill
 * @property totalPopulation The total population for this skill type
 */
data class SkillPopulationSummaryResponse(
    val skill: SkillType,
    val totalPopulation: Long
)
