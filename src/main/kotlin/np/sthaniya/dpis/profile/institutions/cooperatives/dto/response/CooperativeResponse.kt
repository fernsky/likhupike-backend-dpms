package np.sthaniya.dpis.profile.institutions.cooperatives.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeStatus
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeType
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

/**
 * Response DTO for a cooperative.
 */
@Schema(description = "Cooperative data")
data class CooperativeResponse(
    @Schema(description = "Unique identifier for the cooperative")
    val id: UUID,

    @Schema(description = "Unique code/slug for the cooperative", example = "upali-agriculture-coop")
    val code: String,

    @Schema(description = "Default locale for this cooperative's content", example = "ne")
    val defaultLocale: String,

    @Schema(description = "Date when the cooperative was established", example = "2018-03-15")
    val establishedDate: LocalDate?,

    @Schema(description = "Ward where the cooperative is located", example = "5")
    val ward: Int?,

    @Schema(description = "Type of cooperative")
    val type: CooperativeType,

    @Schema(description = "Status of the cooperative")
    val status: CooperativeStatus,

    @Schema(description = "Official registration number of the cooperative", example = "REG-2075-123")
    val registrationNumber: String?,

    @Schema(description = "Geographic point location (longitude, latitude)")
    val point: GeoPointResponse?,

    @Schema(description = "Primary contact email for the cooperative", example = "contact@upali-coop.np")
    val contactEmail: String?,

    @Schema(description = "Primary contact phone number", example = "+977 9812345678")
    val contactPhone: String?,

    @Schema(description = "Website URL of the cooperative", example = "https://upali-coop.np")
    val websiteUrl: String?,

    @Schema(description = "When this record was created")
    val createdAt: Instant,

    @Schema(description = "When this record was last updated")
    val updatedAt: Instant,

    @Schema(description = "Translations for this cooperative")
    val translations: List<CooperativeTranslationResponse> = emptyList(),

    @Schema(description = "Primary media items for this cooperative (one per type)")
    val primaryMedia: Map<String, CooperativeMediaResponse> = emptyMap()
)
