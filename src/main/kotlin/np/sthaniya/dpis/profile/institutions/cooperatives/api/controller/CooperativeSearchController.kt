package np.sthaniya.dpis.profile.institutions.cooperatives.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeStatus
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeType
import np.sthaniya.dpis.common.dto.ApiResponse as DpisApiResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

/**
 * Controller interface for searching and filtering cooperatives.
 */
@Tag(name = "Cooperative Search", description = "APIs for searching and filtering cooperatives")
@RequestMapping("/api/v1/cooperatives/search")
interface CooperativeSearchController {

    /**
     * Searches for cooperatives by name.
     *
     * @param nameQuery The name search query
     * @param pageable Pagination information
     * @return A paginated list of cooperatives matching the name query
     */
    @Operation(
        summary = "Search cooperatives by name",
        description = "Searches for cooperatives by name across all translations. " +
                "Requires VIEW_COOPERATIVE permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Search completed successfully",
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
        path = ["/by-name"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun searchCooperativesByName(
        @Parameter(description = "Name search query", required = true)
        @RequestParam nameQuery: String,
        @Parameter(description = "Pagination parameters")
        pageable: Pageable
    ): ResponseEntity<DpisApiResponse<Page<CooperativeResponse>>>

    /**
     * Retrieves cooperatives by type.
     *
     * @param type The cooperative type
     * @param pageable Pagination information
     * @return A paginated list of cooperatives of the specified type
     */
    @Operation(
        summary = "Get cooperatives by type",
        description = "Retrieves cooperatives filtered by their type. " +
                "Requires VIEW_COOPERATIVE permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Cooperatives retrieved successfully",
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
        path = ["/by-type/{type}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getCooperativesByType(
        @Parameter(description = "Cooperative type", required = true)
        @PathVariable type: CooperativeType,
        @Parameter(description = "Pagination parameters")
        pageable: Pageable
    ): ResponseEntity<DpisApiResponse<Page<CooperativeResponse>>>

    /**
     * Retrieves cooperatives by status.
     *
     * @param status The cooperative status
     * @param pageable Pagination information
     * @return A paginated list of cooperatives with the specified status
     */
    @Operation(
        summary = "Get cooperatives by status",
        description = "Retrieves cooperatives filtered by their status. " +
                "Requires VIEW_COOPERATIVE permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Cooperatives retrieved successfully",
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
        path = ["/by-status/{status}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getCooperativesByStatus(
        @Parameter(description = "Cooperative status", required = true)
        @PathVariable status: CooperativeStatus,
        @Parameter(description = "Pagination parameters")
        pageable: Pageable
    ): ResponseEntity<DpisApiResponse<Page<CooperativeResponse>>>

    /**
     * Retrieves cooperatives by ward.
     *
     * @param ward The ward number
     * @param pageable Pagination information
     * @return A paginated list of cooperatives in the specified ward
     */
    @Operation(
        summary = "Get cooperatives by ward",
        description = "Retrieves cooperatives located in a specific ward. " +
                "Requires VIEW_COOPERATIVE permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Cooperatives retrieved successfully",
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
        path = ["/by-ward/{ward}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getCooperativesByWard(
        @Parameter(description = "Ward number", required = true)
        @PathVariable ward: Int,
        @Parameter(description = "Pagination parameters")
        pageable: Pageable
    ): ResponseEntity<DpisApiResponse<Page<CooperativeResponse>>>

    /**
     * Finds cooperatives near a geographic point.
     *
     * @param longitude The longitude coordinate
     * @param latitude The latitude coordinate
     * @param distanceInMeters The search radius in meters
     * @param pageable Pagination information
     * @return A paginated list of cooperatives near the specified location
     */
    @Operation(
        summary = "Find cooperatives near location",
        description = "Finds cooperatives within a specified distance of a geographic point. " +
                "Requires VIEW_COOPERATIVE permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Search completed successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "400", description = "Invalid coordinates or distance"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_VIEW_COOPERATIVE')")
    @GetMapping(
        path = ["/near"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun findCooperativesNear(
        @Parameter(description = "Longitude coordinate", required = true)
        @RequestParam longitude: Double,
        @Parameter(description = "Latitude coordinate", required = true)
        @RequestParam latitude: Double,
        @Parameter(description = "Search radius in meters", required = true)
        @RequestParam distanceInMeters: Double,
        @Parameter(description = "Pagination parameters")
        pageable: Pageable
    ): ResponseEntity<DpisApiResponse<Page<CooperativeResponse>>>

    /**
     * Gets cooperative statistics by type.
     *
     * @return A map of cooperative types to counts
     */
    @Operation(
        summary = "Get cooperative statistics by type",
        description = "Retrieves statistics on cooperatives grouped by type. " +
                "Requires VIEW_COOPERATIVE permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Statistics retrieved successfully",
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
        path = ["/statistics/by-type"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getCooperativeStatisticsByType(): ResponseEntity<DpisApiResponse<Map<CooperativeType, Long>>>

    /**
     * Gets cooperative statistics by ward.
     *
     * @return A map of ward numbers to counts
     */
    @Operation(
        summary = "Get cooperative statistics by ward",
        description = "Retrieves statistics on cooperatives grouped by ward. " +
                "Requires VIEW_COOPERATIVE permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Statistics retrieved successfully",
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
        path = ["/statistics/by-ward"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getCooperativeStatisticsByWard(): ResponseEntity<DpisApiResponse<Map<Int, Long>>>
}
