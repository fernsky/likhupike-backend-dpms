package np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.response

/**
 * Response DTO for summarized ward skills data.
 *
 * @property wardNumber The ward number
 * @property totalPopulation The total population with skills in this ward
 */
data class WardSkillsSummaryResponse(
    val wardNumber: Int,
    val totalPopulation: Long
)
