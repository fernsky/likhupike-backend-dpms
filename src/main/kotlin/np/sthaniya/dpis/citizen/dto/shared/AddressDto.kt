package np.sthaniya.dpis.citizen.dto.shared

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

/**
 * Data transfer object for physical address information.
 * Used in both citizen and management endpoints for creating and updating address data.
 */
@Schema(
    description = "Physical address information following Nepal's administrative structure",
    title = "Address Information"
)
data class AddressDto(
    @Schema(
        description = "Code of the province",
        example = "3",
        required = true
    )
    @field:NotBlank(message = "Province code is required")
    val provinceCode: String,

    @Schema(
        description = "Code of the district",
        example = "27",
        required = true
    )
    @field:NotBlank(message = "District code is required")
    val districtCode: String,

    @Schema(
        description = "Code of the municipality",
        example = "27002",
        required = true
    )
    @field:NotBlank(message = "Municipality code is required")
    val municipalityCode: String,

    @Schema(
        description = "Ward number",
        example = "5",
        required = true
    )
    @field:NotNull(message = "Ward number is required")
    val wardNumber: Int,

    @Schema(
        description = "Detailed street address or location description",
        example = "Near Pashupati Temple, Gaurighat",
        required = false
    )
    val streetAddress: String? = null
)
