package np.sthaniya.dpis.profile.institutions.cooperatives.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.CreateCooperativeTranslationDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.UpdateCooperativeTranslationDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeTranslationResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.model.ContentStatus
import np.sthaniya.dpis.common.dto.ApiResponse as DpisApiResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

/**
 * Controller interface for managing cooperative translations.
 */
@Tag(name = "Cooperative Translations", description = "APIs for managing cooperative translations")
@RequestMapping("/api/v1/cooperatives")
interface CooperativeTranslationController {

    /**
     * Creates a new translation for a cooperative.
     *
     * @param cooperativeId The cooperative ID
     * @param createDto The translation data
     * @return The created translation
     */
    @Operation(
        summary = "Create translation",
        description = "Creates a new translation for a cooperative. " +
                "Requires UPDATE_COOPERATIVE permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Translation created successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "400", description = "Invalid input data"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Not found - Cooperative does not exist"),
            ApiResponse(responseCode = "409", description = "Conflict - Translation for this locale already exists")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_UPDATE_COOPERATIVE')")
    @PostMapping(
        path = ["/{cooperativeId}/translations"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createTranslation(
        @Parameter(description = "Cooperative ID", required = true)
        @PathVariable cooperativeId: UUID,
        @Parameter(description = "Translation data", required = true)
        @Valid @RequestBody createDto: CreateCooperativeTranslationDto
    ): ResponseEntity<DpisApiResponse<CooperativeTranslationResponse>>

    /**
     * Updates an existing translation.
     *
     * @param cooperativeId The cooperative ID
     * @param translationId The translation ID
     * @param updateDto The updated translation data
     * @return The updated translation
     */
    @Operation(
        summary = "Update translation",
        description = "Updates an existing translation for a cooperative. " +
                "Requires UPDATE_COOPERATIVE permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Translation updated successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "400", description = "Invalid input data"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Not found - Translation does not exist")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_UPDATE_COOPERATIVE')")
    @PutMapping(
        path = ["/{cooperativeId}/translations/{translationId}"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateTranslation(
        @Parameter(description = "Cooperative ID", required = true)
        @PathVariable cooperativeId: UUID,
        @Parameter(description = "Translation ID", required = true)
        @PathVariable translationId: UUID,
        @Parameter(description = "Updated translation data", required = true)
        @Valid @RequestBody updateDto: UpdateCooperativeTranslationDto
    ): ResponseEntity<DpisApiResponse<CooperativeTranslationResponse>>

    /**
     * Retrieves a translation by ID.
     *
     * @param cooperativeId The cooperative ID
     * @param translationId The translation ID
     * @return The translation
     */
    @Operation(
        summary = "Get translation by ID",
        description = "Retrieves a translation by its ID. " +
                "Requires VIEW_COOPERATIVE permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Translation retrieved successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Not found - Translation does not exist")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_VIEW_COOPERATIVE')")
    @GetMapping(
        path = ["/{cooperativeId}/translations/{translationId}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getTranslationById(
        @Parameter(description = "Cooperative ID", required = true)
        @PathVariable cooperativeId: UUID,
        @Parameter(description = "Translation ID", required = true)
        @PathVariable translationId: UUID
    ): ResponseEntity<DpisApiResponse<CooperativeTranslationResponse>>

    /**
     * Retrieves a translation by locale.
     *
     * @param cooperativeId The cooperative ID
     * @param locale The locale code
     * @return The translation
     */
    @Operation(
        summary = "Get translation by locale",
        description = "Retrieves a translation by its locale. " +
                "Requires VIEW_COOPERATIVE permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Translation retrieved successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Not found - Translation does not exist")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_VIEW_COOPERATIVE')")
    @GetMapping(
        path = ["/{cooperativeId}/translations/locale/{locale}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getTranslationByLocale(
        @Parameter(description = "Cooperative ID", required = true)
        @PathVariable cooperativeId: UUID,
        @Parameter(description = "Locale code", required = true)
        @PathVariable locale: String
    ): ResponseEntity<DpisApiResponse<CooperativeTranslationResponse>>

    /**
     * Retrieves all translations for a cooperative.
     *
     * @param cooperativeId The cooperative ID
     * @return A list of all translations
     */
    @Operation(
        summary = "Get all translations",
        description = "Retrieves all translations for a cooperative. " +
                "Requires VIEW_COOPERATIVE permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Translations retrieved successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Not found - Cooperative does not exist")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_VIEW_COOPERATIVE')")
    @GetMapping(
        path = ["/{cooperativeId}/translations"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAllTranslations(
        @Parameter(description = "Cooperative ID", required = true)
        @PathVariable cooperativeId: UUID
    ): ResponseEntity<DpisApiResponse<List<CooperativeTranslationResponse>>>

    /**
     * Deletes a translation.
     *
     * @param cooperativeId The cooperative ID
     * @param translationId The translation ID
     * @return A success response
     */
    @Operation(
        summary = "Delete translation",
        description = "Deletes a translation from the system. " +
                "Requires UPDATE_COOPERATIVE permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Translation deleted successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Not found - Translation does not exist"),
            ApiResponse(responseCode = "409", description = "Conflict - Cannot delete the default locale translation")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_UPDATE_COOPERATIVE')")
    @DeleteMapping(
        path = ["/{cooperativeId}/translations/{translationId}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun deleteTranslation(
        @Parameter(description = "Cooperative ID", required = true)
        @PathVariable cooperativeId: UUID,
        @Parameter(description = "Translation ID", required = true)
        @PathVariable translationId: UUID
    ): ResponseEntity<DpisApiResponse<Void>>

    /**
     * Updates the status of a translation.
     *
     * @param cooperativeId The cooperative ID
     * @param translationId The translation ID
     * @param status The new status
     * @return The updated translation
     */
    @Operation(
        summary = "Update translation status",
        description = "Updates the status of a translation. " +
                "Requires UPDATE_COOPERATIVE permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Status updated successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "400", description = "Invalid status"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Not found - Translation does not exist")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_UPDATE_COOPERATIVE')")
    @PatchMapping(
        path = ["/{cooperativeId}/translations/{translationId}/status"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateTranslationStatus(
        @Parameter(description = "Cooperative ID", required = true)
        @PathVariable cooperativeId: UUID,
        @Parameter(description = "Translation ID", required = true)
        @PathVariable translationId: UUID,
        @Parameter(description = "New status", required = true)
        @RequestParam status: ContentStatus
    ): ResponseEntity<DpisApiResponse<CooperativeTranslationResponse>>
}
