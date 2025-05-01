package np.sthaniya.dpis.profile.institutions.cooperatives.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import java.time.LocalDate
import np.sthaniya.dpis.common.dto.ApiResponse as DpisApiResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeProjection
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeStatus
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeType
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/** Controller for searching cooperatives with advanced filtering. */
@Tag(
        name = "Cooperative Search",
        description = "APIs for searching cooperatives with advanced filtering"
)
@RequestMapping("/api/v1/cooperatives/search")
interface CooperativeSearchController {

    /**
     * Search cooperatives with advanced filtering and pagination.
     *
     * @param code Filter by exact cooperative code
     * @param searchTerm Generic search term that matches against multiple text fields
     * @param type Filter by cooperative type
     * @param types Filter by multiple cooperative types
     * @param status Filter by cooperative status
     * @param ward Filter by specific ward number
     * @param wards Filter by multiple ward numbers
     * @param establishedAfter Filter cooperatives established after this date (inclusive)
     * @param establishedBefore Filter cooperatives established before this date (inclusive)
     * @param locale Filter by locale for translation content
     * @param nameContains Filter by text contained in cooperative name
     * @param longitude Filter by geographic proximity (longitude)
     * @param latitude Filter by geographic proximity (latitude)
     * @param radiusInMeters Search radius in meters for geographic proximity search
     * @param hasMediaOfType Include cooperatives with media of specified type
     * @param includeTranslations Include translations in response
     * @param includePrimaryMedia Include primary media items in response
     * @param page Page number (1-based)
     * @param size Number of items per page
     * @param sortBy Field to sort by
     * @param sortDirection Sort direction
     * @return A paginated list of cooperatives matching the criteria wrapped in an ApiResponse
     */
    @Operation(
            summary = "Search cooperatives",
            description = "Search cooperatives with advanced filtering, sorting and pagination",
            responses =
                    [
                            ApiResponse(
                                    responseCode = "200",
                                    description = "Successful operation",
                                    content =
                                            [
                                                    Content(
                                                            schema =
                                                                    Schema(
                                                                            implementation =
                                                                                    CooperativeSearchResponse::class
                                                                    )
                                                    )]
                            ),
                            ApiResponse(
                                    responseCode = "400",
                                    description = "Invalid search criteria",
                                    content = [Content()]
                            ),
                            ApiResponse(
                                    responseCode = "401",
                                    description = "Unauthorized",
                                    content = [Content()]
                            ),
                            ApiResponse(
                                    responseCode = "403",
                                    description = "Forbidden",
                                    content = [Content()]
                            )]
    )
    @GetMapping
    fun searchCooperatives(
            @Parameter(description = "Filter by exact cooperative code")
            @RequestParam(required = false)
            code: String?,
            @Parameter(
                    description =
                            "Generic search term that matches against name, description, and other text fields"
            )
            @RequestParam(required = false)
            searchTerm: String?,
            @Parameter(description = "Filter by cooperative type")
            @RequestParam(required = false)
            type: CooperativeType?,
            @Parameter(description = "Filter by multiple cooperative types")
            @RequestParam(required = false)
            types: Set<CooperativeType>?,
            @Parameter(description = "Filter by cooperative status")
            @RequestParam(required = false)
            status: CooperativeStatus?,
            @Parameter(description = "Filter by specific ward number")
            @RequestParam(required = false)
            ward: Int?,
            @Parameter(description = "Filter by multiple ward numbers")
            @RequestParam(required = false)
            wards: Set<Int>?,
            @Parameter(description = "Filter cooperatives established after this date (inclusive)")
            @RequestParam(required = false)
            establishedAfter: LocalDate?,
            @Parameter(description = "Filter cooperatives established before this date (inclusive)")
            @RequestParam(required = false)
            establishedBefore: LocalDate?,
            @Parameter(description = "Filter by locale for translation content")
            @RequestParam(required = false)
            locale: String?,
            @Parameter(
                    description =
                            "Filter by text contained in cooperative name (in the specified locale)"
            )
            @RequestParam(required = false)
            nameContains: String?,
            @Parameter(description = "Filter by geographic proximity (longitude)")
            @RequestParam(required = false)
            longitude: Double?,
            @Parameter(description = "Filter by geographic proximity (latitude)")
            @RequestParam(required = false)
            latitude: Double?,
            @Parameter(description = "Search radius in meters for geographic proximity search")
            @RequestParam(required = false)
            radiusInMeters: Double?,
            @Parameter(description = "Include cooperatives with media of specified type")
            @RequestParam(required = false)
            hasMediaOfType: String?,
            @Parameter(description = "Include translations in response")
            @RequestParam(required = false, defaultValue = "false")
            includeTranslations: Boolean = false,
            @Parameter(description = "Include primary media items in response")
            @RequestParam(required = false, defaultValue = "false")
            includePrimaryMedia: Boolean = false,
            @Parameter(description = "Page number (1-based)")
            @RequestParam(required = false, defaultValue = "1")
            page: Int = 1,
            @Parameter(description = "Number of items per page")
            @RequestParam(required = false, defaultValue = "10")
            size: Int = 10,
            @Parameter(description = "Field to sort by")
            @RequestParam(required = false, defaultValue = "createdAt")
            sortBy: String = "createdAt",
            @Parameter(description = "Sort direction")
            @RequestParam(required = false, defaultValue = "DESC")
            sortDirection: Sort.Direction = Sort.Direction.DESC
    ): ResponseEntity<DpisApiResponse<List<CooperativeProjection>>>
}

// Schema for Swagger documentation
class CooperativeSearchResponse {
    var success: Boolean = true
    var message: String? = null
    var data: List<CooperativeProjection>? = null
}
