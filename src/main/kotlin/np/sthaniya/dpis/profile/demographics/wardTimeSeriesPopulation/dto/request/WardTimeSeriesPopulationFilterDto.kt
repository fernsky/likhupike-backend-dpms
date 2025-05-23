package np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.dto.request

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

/**
 * DTO for filtering ward time series population data.
 *
 * @property wardNumber Optional ward number filter
 * @property year Optional year filter
 */
data class WardTimeSeriesPopulationFilterDto(
        @field:Min(value = 1, message = "Ward number must be positive")
        val wardNumber: Int? = null,

        @field:Min(value = 2000, message = "Year must be after 2000")
        @field:Max(value = 2100, message = "Year must be before 2100")
        val year: Int? = null
)
