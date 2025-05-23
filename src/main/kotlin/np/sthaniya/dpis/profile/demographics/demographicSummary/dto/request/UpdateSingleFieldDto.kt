package np.sthaniya.dpis.profile.demographics.demographicSummary.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

/**
 * DTO for updating a single field in the demographic summary.
 *
 * @property field The name of the field to update
 * @property value The new value for the field (can be Int, String, BigDecimal, or null)
 */
data class UpdateSingleFieldDto(
    @field:NotBlank(message = "Field name is required")
    val field: String,

    @field:NotNull(message = "Value cannot be null")
    val value: Any?
)
