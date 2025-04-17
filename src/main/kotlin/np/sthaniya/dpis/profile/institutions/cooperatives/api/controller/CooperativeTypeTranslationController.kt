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
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.CooperativeTypeTranslationDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeTypeTranslationResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeType
import np.sthaniya.dpis.common.dto.ApiResponse as DpisApiResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

/**
 * Controller interface for managing cooperative type translations.
 */
@Tag(name = "Cooperative Type Translations", description = "APIs for managing cooperative type translations")
@RequestMapping("/api/v1/cooperative-types")
interface CooperativeTypeTranslationController {

    /**
     * Creates or updates a translation for a cooperative type.
     *
     * @param createDto The translation data
     * @return The created or updated translation
     */
    @Operation(
        summary = "Create or update type translation",
        description = "Creates or updates a translation for a cooperative type. " +
                "Requires UPDATE_COOPERATIVE permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Translation created/updated successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "400", description = "Invalid input data"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_UPDATE_COOPERATIVE')")
    @PostMapping(
        path = ["/translations"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createOrUpdateTypeTranslation(
        @Parameter(description = "Type translation data", required = true)
        @Valid @RequestBody createDto: CooperativeTypeTranslationDto
    ): ResponseEntity<DpisApiResponse<CooperativeTypeTranslationResponse>>

    /**
     * Retrieves a type translation by ID.
     *
     * @param translationId The translation ID
     * @return The translation
     */
    @Operation(
        summary = "Get type translation by ID",
        description = "Retrieves a cooperative type translation by its ID. " +
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
        path = ["/translations/{translationId}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getTypeTranslationById(
        @Parameter(description = "Translation ID", required = true)
        @PathVariable translationId: UUID
    ): ResponseEntity<DpisApiResponse<CooperativeTypeTranslationResponse>>

    /**
     * Retrieves a type translation by type and locale.
     *
     * @param type The cooperative type
     * @param locale The locale code
     * @return The translation
     */
    @Operation(
        summary = "Get type translation by type and locale",
        description = "Retrieves a cooperative type translation by its type and locale. " +
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
        path = ["/translations/type/{type}/locale/{locale}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getTypeTranslationByTypeAndLocale(
        @Parameter(description = "Cooperative type", required = true)
        @PathVariable type: CooperativeType,
        @Parameter(description = "Locale code", required = true)
        @PathVariable locale: String
    ): ResponseEntity<DpisApiResponse<CooperativeTypeTranslationResponse>>

    /**
     * Retrieves all translations for a cooperative type.
     *
     * @param type The cooperative type
     * @return A list of translations
     */
    @Operation(
        summary = "Get all translations for type",
        description = "Retrieves all translations for a cooperative type. " +
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
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_VIEW_COOPERATIVE')")
    @GetMapping(
        path = ["/translations/type/{type}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAllTranslationsForType(
        @Parameter(description = "Cooperative type", required = true)
        @PathVariable type: CooperativeType
    ): ResponseEntity<DpisApiResponse<List<CooperativeTypeTranslationResponse>>>

    /**
     * Retrieves all translations for a specific locale.
     *
     * @param locale The locale code
     * @param pageable Pagination information
     * @return A paginated list of translations
     */
    @Operation(
        summary = "Get translations by locale",
        description = "Retrieves all cooperative type translations for a specific locale. " +
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
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_VIEW_COOPERATIVE')")
    @GetMapping(
        path = ["/translations/locale/{locale}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getTypeTranslationsByLocale(
        @Parameter(description = "Locale code", required = true)
        @PathVariable locale: String,
        @Parameter(description = "Pagination parameters")
        pageable: Pageable
    ): ResponseEntity<DpisApiResponse<Page<CooperativeTypeTranslationResponse>>>

    /**
     * Deletes a type translation.
     *
     * @param type The cooperative type
     * @param locale The locale code
     * @return A success response
     */
    @Operation(
        summary = "Delete type translation",
        description = "Deletes a cooperative type translation. " +
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
            ApiResponse(responseCode = "404", description = "Not found - Translation does not exist")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_UPDATE_COOPERATIVE')")
    @DeleteMapping(
        path = ["/translations/type/{type}/locale/{locale}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun deleteTypeTranslation(
        @Parameter(description = "Cooperative type", required = true)
        @PathVariable type: CooperativeType,
        @Parameter(description = "Locale code", required = true)
        @PathVariable locale: String
    ): ResponseEntity<DpisApiResponse<Void>>

    /**
     * Gets all available cooperative types with their translations for a specific locale.
     *
     * @param locale The locale code
     * @return A map of cooperative types to their translations
     */
    @Operation(
        summary = "Get all type translations for locale",
        description = "Gets all available cooperative types with their translations for a specific locale. " +
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
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_VIEW_COOPERATIVE')")
    @GetMapping(
        path = ["/translations/all-types/locale/{locale}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAllTypeTranslationsForLocale(
        @Parameter(description = "Locale code", required = true)
        @PathVariable locale: String
    ): ResponseEntity<DpisApiResponse<Map<CooperativeType, CooperativeTypeTranslationResponse>>>
}
