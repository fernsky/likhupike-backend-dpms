package np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.dto.response

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

/**
 * Response DTO for ward-wise demographic summary data.
 *
 * @property id Unique identifier
 * @property wardNumber The ward number
 * @property wardName The ward name (optional)
 * @property totalPopulation Total population in the ward
 * @property populationMale Male population in the ward
 * @property populationFemale Female population in the ward
 * @property populationOther Other gender population in the ward
 * @property totalHouseholds Total number of households in the ward
 * @property averageHouseholdSize Average household size in the ward
 * @property sexRatio Sex ratio (number of males per 100 females) in the ward
 * @property createdAt The creation timestamp
 * @property updatedAt The last update timestamp
 */
data class WardWiseDemographicSummaryResponse(
        val id: UUID,
        val wardNumber: Int,
        val wardName: String?,
        val totalPopulation: Int?,
        val populationMale: Int?,
        val populationFemale: Int?,
        val populationOther: Int?,
        val totalHouseholds: Int?,
        val averageHouseholdSize: BigDecimal?,
        val sexRatio: BigDecimal?,
        val createdAt: Instant?,
        val updatedAt: Instant?
)
