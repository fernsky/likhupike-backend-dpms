package np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.response

import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.model.MaritalStatusGroup

/**
 * Response DTO for summarized marital status data.
 *
 * @property maritalStatus The marital status type
 * @property totalPopulation The total population for this marital status
 * @property malePopulation The total male population for this marital status
 * @property femalePopulation The total female population for this marital status
 * @property otherPopulation The total other gender population for this marital status
 */
data class MaritalStatusSummaryResponse(
    val maritalStatus: MaritalStatusGroup,
    val totalPopulation: Long,
    val malePopulation: Long?,
    val femalePopulation: Long?,
    val otherPopulation: Long?
)
