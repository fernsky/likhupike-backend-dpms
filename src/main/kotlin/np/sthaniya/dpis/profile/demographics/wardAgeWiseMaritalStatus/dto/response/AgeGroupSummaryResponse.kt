package np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.response

import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.model.MaritalAgeGroup

/**
 * Response DTO for summarized age group data.
 *
 * @property ageGroup The age group
 * @property totalPopulation The total population for this age group
 * @property malePopulation The total male population for this age group
 * @property femalePopulation The total female population for this age group
 * @property otherPopulation The total other gender population for this age group
 */
data class AgeGroupSummaryResponse(
    val ageGroup: MaritalAgeGroup,
    val totalPopulation: Long,
    val malePopulation: Long?,
    val femalePopulation: Long?,
    val otherPopulation: Long?
)
