package np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.response

import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.model.LanguageType

/**
 * Response DTO for summarized language population data.
 *
 * @property languageType The type of language
 * @property totalPopulation The total population for this language type
 */
data class LanguagePopulationSummaryResponse(
        val languageType: LanguageType,
        val totalPopulation: Long
)
