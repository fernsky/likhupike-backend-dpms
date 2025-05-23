package np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.response

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.model.MaritalAgeGroup
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.model.MaritalStatusGroup

/**
 * Response DTO for ward age-wise marital status data.
 *
 * @property id Unique identifier
 * @property wardNumber The ward number
 * @property ageGroup The age group
 * @property maritalStatus The marital status
 * @property population The total population count
 * @property malePopulation The male population count
 * @property femalePopulation The female population count
 * @property otherPopulation The other gender population count
 * @property createdAt The creation timestamp
 * @property updatedAt The last update timestamp
 */
data class WardAgeWiseMaritalStatusResponse(
    val id: UUID,
    val wardNumber: Int,
    val ageGroup: MaritalAgeGroup,
    val maritalStatus: MaritalStatusGroup,
    val population: Int,
    val malePopulation: Int?,
    val femalePopulation: Int?,
    val otherPopulation: Int?,
    val createdAt: Instant?,
    val updatedAt: Instant?
)
