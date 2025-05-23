package np.sthaniya.dpis.profile.demographics.demographicSummary.dto.response

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

/**
 * Response DTO for demographic summary data.
 *
 * @property id Unique identifier
 * @property totalPopulation Total population in the municipality
 * @property populationMale Male population in the municipality
 * @property populationFemale Female population in the municipality
 * @property populationOther Other gender population in the municipality
 * @property populationAbsenteeTotal Total absentee population in the municipality
 * @property populationMaleAbsentee Male absentee population in the municipality
 * @property populationFemaleAbsentee Female absentee population in the municipality
 * @property populationOtherAbsentee Other gender absentee population in the municipality
 * @property sexRatio Sex ratio (number of males per 100 females)
 * @property totalHouseholds Total number of households
 * @property averageHouseholdSize Average household size
 * @property populationDensity Population density (people per sq km)
 * @property population0To14 Population aged 0-14 years
 * @property population15To59 Population aged 15-59 years
 * @property population60AndAbove Population aged 60 and above
 * @property growthRate Population growth rate
 * @property literacyRateAbove15 Literacy rate for population above 15 years
 * @property literacyRate15To24 Literacy rate for population between 15-24 years
 * @property createdAt The creation timestamp
 * @property updatedAt The last update timestamp
 */
data class DemographicSummaryResponse(
    val id: UUID,
    val totalPopulation: Int?,
    val populationMale: Int?,
    val populationFemale: Int?,
    val populationOther: Int?,
    val populationAbsenteeTotal: Int?,
    val populationMaleAbsentee: Int?,
    val populationFemaleAbsentee: Int?,
    val populationOtherAbsentee: Int?,
    val sexRatio: BigDecimal?,
    val totalHouseholds: Int?,
    val averageHouseholdSize: BigDecimal?,
    val populationDensity: BigDecimal?,
    val population0To14: Int?,
    val population15To59: Int?,
    val population60AndAbove: Int?,
    val growthRate: BigDecimal?,
    val literacyRateAbove15: BigDecimal?,
    val literacyRate15To24: BigDecimal?,
    val createdAt: Instant?,
    val updatedAt: Instant?
)
