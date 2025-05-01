package np.sthaniya.dpis.profile.institutions.cooperatives.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import np.sthaniya.dpis.common.dto.ApiResponse as DpisApiResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeProjection
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.search.CooperativeSearchCriteria
import org.springframework.data.domain.Page
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
     * @param criteria The search criteria for filtering cooperatives
     * @return A paginated list of cooperatives matching the criteria
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
    @PostMapping
    fun searchCooperatives(
            @Parameter(description = "Search criteria", required = true)
            @Valid
            @RequestBody
            criteria: CooperativeSearchCriteria
    ): ResponseEntity<DpisApiResponse<Page<CooperativeProjection>>>
}

// Schema for Swagger documentation
class CooperativeSearchResponse {
    var success: Boolean = true
    var message: String? = null
    var data: Page<CooperativeProjection>? = null
}
