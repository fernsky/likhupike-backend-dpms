package np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import np.sthaniya.dpis.profile.economics.common.model.SkillType

/**
 * DTO for updating ward-wise major skills data.
 *
 * @property wardNumber The ward number (must be positive)
 * @property skill The type of skill
 * @property population The population count (must be non-negative)
 */
data class UpdateWardWiseMajorSkillsDto(
    @field:NotNull(message = "Ward number is required")
    @field:Min(value = 1, message = "Ward number must be positive")
    val wardNumber: Int,

    @field:NotNull(message = "Skill type is required")
    val skill: SkillType,

    @field:NotNull(message = "Population is required")
    @field:Min(value = 0, message = "Population must be non-negative")
    val population: Int
)
