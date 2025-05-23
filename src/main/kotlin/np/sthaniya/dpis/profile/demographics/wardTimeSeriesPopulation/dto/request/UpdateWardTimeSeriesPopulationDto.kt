package np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.dto.request

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

/**
 * DTO for updating ward time series population data.
 *
 * @property wardNumber The ward number (must be positive)
 * @property wardName The optional ward name
 * @property year The census year (must be between 2000 and 2100)
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
 */
data class UpdateWardTimeSeriesPopulationDto(
        @field:NotNull(message = "Ward number is required")
        @field:Min(value = 1, message = "Ward number must be positive")
        val wardNumber: Int,

        val wardName: String? = null,

        @field:NotNull(message = "Year is required")
        @field:Min(value = 2000, message = "Year must be after 2000")
        @field:Max(value = 2100, message = "Year must be before 2100")
        val year: Int,

        @field:Min(value = 0, message = "Population must be non-negative")
        val totalPopulation: Int? = null,

        @field:Min(value = 0, message = "Male population must be non-negative")
        val malePopulation: Int? = null,

        @field:Min(value = 0, message = "Female population must be non-negative")
        val femalePopulation: Int? = null,

        @field:Min(value = 0, message = "Other population must be non-negative")
        val otherPopulation: Int? = null,

        @field:Min(value = 0, message = "Total households must be non-negative")
        val totalHouseholds: Int? = null,

        val averageHouseholdSize: BigDecimal? = null,

        @field:Min(value = 0, message = "Population must be non-negative")
        val population0To14: Int? = null,

        @field:Min(value = 0, message = "Population must be non-negative")
        val population15To59: Int? = null,

        @field:Min(value = 0, message = "Population must be non-negative")
        val population60AndAbove: Int? = null,

        val literacyRate: BigDecimal? = null,
        val maleLiteracyRate: BigDecimal? = null,
        val femaleLiteracyRate: BigDecimal? = null,
        val growthRate: BigDecimal? = null,
        val areaSqKm: BigDecimal? = null,
        val populationDensity: BigDecimal? = null,
        val sexRatio: BigDecimal? = null
)
