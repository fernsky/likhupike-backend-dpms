package np.sthaniya.dpis.citizen.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import np.sthaniya.dpis.citizen.dto.projection.CitizenProjection
import np.sthaniya.dpis.citizen.dto.search.CitizenSearchCriteria
import np.sthaniya.dpis.common.dto.ApiResponse as DpisApiResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

/**
 * Controller interface for searching citizens with advanced filtering.
 */
@Tag(name = "Citizen Search", description = "APIs for searching and filtering citizen records")
@RequestMapping("/api/v1/admin/citizens")
interface CitizenSearchController {

    /**
     * Search for citizens with comprehensive filtering options.
     * 
     * @param criteria The search criteria to apply
     * @return Paginated list of matching citizens with selected fields
     */
    @Operation(
        summary = "Search for citizens",
        description = "Searches for citizens using comprehensive filter criteria. " +
                "Results are paginated and can be sorted. " +
                "Requires VIEW_CITIZEN permission.",
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
            ApiResponse(responseCode = "400", description = "Invalid search criteria"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Page not found - Requested page exceeds result count")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_VIEW_CITIZEN')")
    @PostMapping(
        path = ["/search"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun searchCitizens(
        @Parameter(
            description = "Search criteria",
            required = true
        )
        @Valid @RequestBody criteria: CitizenSearchCriteria
    ): ResponseEntity<DpisApiResponse<List<CitizenProjection>>>
}
