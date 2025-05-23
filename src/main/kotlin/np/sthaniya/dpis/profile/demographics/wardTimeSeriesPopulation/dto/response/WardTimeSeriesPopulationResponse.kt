package np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.dto.response

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

/**
 * Response DTO for ward time series population data.
 *
 * @property id Unique identifier
 * @property wardNumber The ward number
 * @property wardName The ward name (if available)
 * @property year The census year
 * @property totalPopulation The total population in the ward
 * @property malePopulation The male population in the ward
 * @property femalePopulation The female population in the ward
 * @property otherPopulation The other gender population in the ward
 * @property totalHouseholds The total number of households in the ward
 * @property averageHouseholdSize The average household size in the ward
 * @property population0To14 The population aged 0-14 years in the ward
 * @property population15To59 The population aged 15-59 years in the ward
 * @property population60AndAbove The population aged 60 and above in the ward
 * @property literacyRate The literacy rate in the ward
 * @property maleLiteracyRate The male literacy rate in the ward
 * @property femaleLiteracyRate The female literacy rate in the ward
 * @property growthRate The population growth rate in the ward
 * @property areaSqKm The area of the ward in square kilometers
 * @property populationDensity The population density in the ward
 * @property sexRatio The sex ratio in the ward
 * @property createdAt The creation timestamp
 * @property updatedAt The last update timestamp
 */
data class WardTimeSeriesPopulationResponse(
        val id: UUID,
        val wardNumber: Int,
        val wardName: String?,
        val year: Int,
        val totalPopulation: Int?,
        val malePopulation: Int?,
        val femalePopulation: Int?,
        val otherPopulation: Int?,
        val totalHouseholds: Int?,
        val averageHouseholdSize: BigDecimal?,
        val population0To14: Int?,
        val population15To59: Int?,
        val population60AndAbove: Int?,
        val literacyRate: BigDecimal?,
        val maleLiteracyRate: BigDecimal?,
        val femaleLiteracyRate: BigDecimal?,
        val growthRate: BigDecimal?,
        val areaSqKm: BigDecimal?,
        val populationDensity: BigDecimal?,
        val sexRatio: BigDecimal?,
        val createdAt: Instant?,
        val updatedAt: Instant?
)
