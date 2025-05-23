package np.sthaniya.dpis.profile.demographics.demographicSummary.dto.request

import jakarta.validation.constraints.Min
import java.math.BigDecimal

/**
 * DTO for updating demographic summary data.
 *
 * All fields are optional for partial updates.
 *
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
 */
data class UpdateDemographicSummaryDto(
    @field:Min(value = 0, message = "Total population must be non-negative")
    val totalPopulation: Int? = null,

    @field:Min(value = 0, message = "Male population must be non-negative")
    val populationMale: Int? = null,

    @field:Min(value = 0, message = "Female population must be non-negative")
    val populationFemale: Int? = null,

    @field:Min(value = 0, message = "Other gender population must be non-negative")
    val populationOther: Int? = null,

    @field:Min(value = 0, message = "Total absentee population must be non-negative")
    val populationAbsenteeTotal: Int? = null,

    @field:Min(value = 0, message = "Male absentee population must be non-negative")
    val populationMaleAbsentee: Int? = null,

    @field:Min(value = 0, message = "Female absentee population must be non-negative")
    val populationFemaleAbsentee: Int? = null,

    @field:Min(value = 0, message = "Other gender absentee population must be non-negative")
    val populationOtherAbsentee: Int? = null,

    val sexRatio: BigDecimal? = null,

    @field:Min(value = 0, message = "Total households must be non-negative")
    val totalHouseholds: Int? = null,

    val averageHouseholdSize: BigDecimal? = null,

    val populationDensity: BigDecimal? = null,

    @field:Min(value = 0, message = "Population 0-14 must be non-negative")
    val population0To14: Int? = null,

    @field:Min(value = 0, message = "Population 15-59 must be non-negative")
    val population15To59: Int? = null,

    @field:Min(value = 0, message = "Population 60 and above must be non-negative")
    val population60AndAbove: Int? = null,

    val growthRate: BigDecimal? = null,

    val literacyRateAbove15: BigDecimal? = null,

    val literacyRate15To24: BigDecimal? = null
)
