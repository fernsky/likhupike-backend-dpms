package np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.response

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.profile.economics.common.model.SkillType

/**
 * Response DTO for ward-wise major skills data.
 *
 * @property id Unique identifier
 * @property wardNumber The ward number
 * @property skill The type of skill
 * @property population The population count with this skill
 * @property createdAt The creation timestamp
 * @property updatedAt The last update timestamp
 */
data class WardWiseMajorSkillsResponse(
    val id: UUID,
    val wardNumber: Int,
    val skill: SkillType,
    val population: Int,
    val createdAt: Instant?,
    val updatedAt: Instant?
)
