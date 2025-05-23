package np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.response

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.profile.demographics.common.model.EducationalLevelType

/**
 * Response DTO for ward-wise absentee educational level data.
 *
 * @property id Unique identifier
 * @property wardNumber The ward number
 * @property educationalLevel The educational level
 * @property population The population count
 * @property createdAt The creation timestamp
 * @property updatedAt The last update timestamp
 */
data class WardWiseAbsenteeEducationalLevelResponse(
        val id: UUID,
        val wardNumber: Int,
        val educationalLevel: EducationalLevelType,
        val population: Int,
        val createdAt: Instant?,
        val updatedAt: Instant?
)
