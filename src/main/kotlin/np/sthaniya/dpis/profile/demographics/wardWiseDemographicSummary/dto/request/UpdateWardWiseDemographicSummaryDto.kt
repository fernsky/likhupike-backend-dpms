package np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

/**
 * DTO for updating an existing ward-wise demographic summary.
 *
 * @property wardNumber The ward number (must be positive)
 * @property wardName Optional ward name for reference
 * @property totalPopulation Total population in the ward
 * @property populationMale Male population in the ward
 * @property populationFemale Female population in the ward
 * @property populationOther Other gender population in the ward
 * @property totalHouseholds Total number of households in the ward
 * @property averageHouseholdSize Average household size in the ward
 * @property sexRatio Sex ratio (number of males per 100 females) in the ward
 */
data class UpdateWardWiseDemographicSummaryDto(
        @field:NotNull(message = "Ward number is required")
        @field:Min(value = 1, message = "Ward number must be positive")
        val wardNumber: Int,

        val wardName: String? = null,

        @field:Min(value = 0, message = "Total population must be non-negative")
        val totalPopulation: Int? = null,

        @field:Min(value = 0, message = "Male population must be non-negative")
        val populationMale: Int? = null,

        @field:Min(value = 0, message = "Female population must be non-negative")
        val populationFemale: Int? = null,

        @field:Min(value = 0, message = "Other population must be non-negative")
        val populationOther: Int? = null,

        @field:Min(value = 0, message = "Total households must be non-negative")
        val totalHouseholds: Int? = null,

        val averageHouseholdSize: BigDecimal? = null,

        val sexRatio: BigDecimal? = null
)
