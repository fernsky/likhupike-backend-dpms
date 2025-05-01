package np.sthaniya.dpis.profile.institutions.cooperatives.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import np.sthaniya.dpis.common.dto.ApiResponse as DpisApiResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeTypeStatistics
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeWardStatistics
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

/** Controller for retrieving cooperative statistics. */
@Tag(
        name = "Cooperative Statistics",
        description = "APIs for retrieving statistical information about cooperatives"
)
@RequestMapping("/api/v1/cooperatives/statistics")
interface CooperativeStatisticsController {

    /**
     * Get statistics of cooperatives by type.
     *
     * @return Statistical data grouped by cooperative type
     */
    @Operation(
            summary = "Get cooperative statistics by type",
            description = "Retrieve counts of cooperatives grouped by their type",
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
                                                                                    CooperativeTypeStatisticsResponse::class
                                                                    )
                                                    )]
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
    @GetMapping("/by-type")
    fun getStatisticsByType(): ResponseEntity<DpisApiResponse<CooperativeTypeStatistics>>

    /**
     * Get statistics of cooperatives by ward.
     *
     * @return Statistical data grouped by ward number
     */
    @Operation(
            summary = "Get cooperative statistics by ward",
            description = "Retrieve counts of cooperatives grouped by ward number",
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
                                                                                    CooperativeWardStatisticsResponse::class
                                                                    )
                                                    )]
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
    @GetMapping("/by-ward")
    fun getStatisticsByWard(): ResponseEntity<DpisApiResponse<CooperativeWardStatistics>>
}

// Schema classes for Swagger documentation
class CooperativeTypeStatisticsResponse {
    var success: Boolean = true
    var message: String? = null
    var data: CooperativeTypeStatistics? = null
}

class CooperativeWardStatisticsResponse {
    var success: Boolean = true
    var message: String? = null
    var data: CooperativeWardStatistics? = null
}
