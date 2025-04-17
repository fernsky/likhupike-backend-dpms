package np.sthaniya.dpis.profile.institutions.cooperatives.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.*
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeStatus
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeType
import java.time.LocalDate

/**
 * DTO for updating an existing cooperative.
 */
@Schema(description = "Request for updating an existing cooperative")
data class UpdateCooperativeDto(
    @field:Pattern(regexp = "^[a-z0-9-]+$", message = "Code must contain only lowercase letters, numbers, and hyphens")
    @field:Size(min = 3, max = 50, message = "Code must be between 3 and 50 characters")
    @Schema(description = "Unique code/slug for the cooperative (URL-friendly)", example = "upali-agriculture-coop")
    val code: String?,

    @field:Size(min = 2, max = 10, message = "Default locale must be between 2 and 10 characters")
    @Schema(description = "Default locale for this cooperative's content", example = "ne")
    val defaultLocale: String?,

    @Schema(description = "Date when the cooperative was established", example = "2018-03-15")
    val establishedDate: LocalDate?,

    @field:Min(1, message = "Ward number must be 1 or greater")
    @field:Max(value = 33, message = "Ward number is out of valid range")
    @Schema(description = "Ward where the cooperative is located", example = "5")
    val ward: Int?,

    @Schema(description = "Type of cooperative")
    val type: CooperativeType?,

    @Schema(description = "Status of the cooperative")
    val status: CooperativeStatus?,

    @Schema(description = "Official registration number of the cooperative", example = "REG-2075-123")
    val registrationNumber: String?,

    @Schema(description = "Geographic point location (longitude, latitude)")
    val point: GeoPointDto?,

    @Schema(description = "Primary contact email for the cooperative", example = "contact@upali-coop.np")
    @field:Email(message = "Invalid email format")
    val contactEmail: String?,

    @Schema(description = "Primary contact phone number", example = "+977 9812345678")
    val contactPhone: String?,

    @Schema(description = "Website URL of the cooperative", example = "https://upali-coop.np")
    val websiteUrl: String?
)
