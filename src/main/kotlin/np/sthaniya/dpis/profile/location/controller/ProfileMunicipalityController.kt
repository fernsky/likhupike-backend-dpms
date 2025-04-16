package np.sthaniya.dpis.profile.location.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import np.sthaniya.dpis.profile.location.dto.MunicipalityCreateRequest
import np.sthaniya.dpis.profile.location.dto.MunicipalityGeoLocationUpdateRequest
import np.sthaniya.dpis.profile.location.dto.MunicipalityResponse
import np.sthaniya.dpis.profile.location.dto.MunicipalityUpdateRequest
import np.sthaniya.dpis.profile.location.service.ProfileMunicipalityService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@Tag(
        name = "Municipality Management",
        description = "Endpoints for managing municipality information"
)
@RestController
@RequestMapping("/api/v1/profile/location/municipality")
class ProfileMunicipalityController(
        private val municipalityService: ProfileMunicipalityService,
        private val i18nMessageService: I18nMessageService
) {

        @Operation(
                summary = "Get or create municipality",
                description = "Get the municipality information or create it if it doesn't exist"
        )
        @ApiResponses(
                value =
                        [
                                SwaggerResponse(
                                        responseCode = "200",
                                        description =
                                                "Municipality information retrieved or created successfully"
                                ),
                                SwaggerResponse(
                                        responseCode = "400",
                                        description = "Invalid input data"
                                ),
                                SwaggerResponse(
                                        responseCode = "401",
                                        description = "Not authenticated"
                                ),
                                SwaggerResponse(
                                        responseCode = "403",
                                        description = "Missing required permission"
                                )]
        )
        @PostMapping
        @PreAuthorize("hasAuthority('PERMISSION_MANAGE_PROFILE')")
        fun getOrCreateMunicipality(
                @Valid @RequestBody request: MunicipalityCreateRequest
        ): ResponseEntity<ApiResponse<MunicipalityResponse>> {
                val municipality = municipalityService.getOrCreateMunicipality(request)
                return ResponseEntity.ok(
                        ApiResponse.success(
                                data = municipality,
                                message =
                                        i18nMessageService.getMessage("municipality.create.success")
                        )
                )
        }

        @Operation(
                summary = "Get municipality information",
                description = "Get the current municipality information"
        )
        @ApiResponses(
                value =
                        [
                                SwaggerResponse(
                                        responseCode = "200",
                                        description =
                                                "Municipality information retrieved successfully"
                                ),
                                SwaggerResponse(
                                        responseCode = "404",
                                        description = "Municipality not found"
                                )]
        )
        @GetMapping
        fun getMunicipalityData(): ResponseEntity<ApiResponse<MunicipalityResponse>> {
                val municipality = municipalityService.getMunicipalityData()
                return ResponseEntity.ok(
                        ApiResponse.success(
                                data = municipality,
                                message = i18nMessageService.getMessage("municipality.get.success")
                        )
                )
        }

        @Operation(
                summary = "Update municipality basic information",
                description = "Update the name, province, and district of the municipality"
        )
        @ApiResponses(
                value =
                        [
                                SwaggerResponse(
                                        responseCode = "200",
                                        description =
                                                "Municipality information updated successfully"
                                ),
                                SwaggerResponse(
                                        responseCode = "400",
                                        description = "Invalid input data"
                                ),
                                SwaggerResponse(
                                        responseCode = "401",
                                        description = "Not authenticated"
                                ),
                                SwaggerResponse(
                                        responseCode = "403",
                                        description = "Missing required permission"
                                ),
                                SwaggerResponse(
                                        responseCode = "404",
                                        description = "Municipality not found"
                                )]
        )
        @PutMapping("/info")
        @PreAuthorize("hasAuthority('PERMISSION_MANAGE_PROFILE')")
        fun updateMunicipalityInfo(
                @Valid @RequestBody request: MunicipalityUpdateRequest
        ): ResponseEntity<ApiResponse<MunicipalityResponse>> {
                val municipality = municipalityService.updateMunicipalityInfo(request)
                return ResponseEntity.ok(
                        ApiResponse.success(
                                data = municipality,
                                message =
                                        i18nMessageService.getMessage("municipality.update.success")
                        )
                )
        }

        @Operation(
                summary = "Update municipality geo-location information",
                description = "Update geographical coordinates and altitude information"
        )
        @ApiResponses(
                value =
                        [
                                SwaggerResponse(
                                        responseCode = "200",
                                        description =
                                                "Municipality geo-location updated successfully"
                                ),
                                SwaggerResponse(
                                        responseCode = "400",
                                        description = "Invalid input data"
                                ),
                                SwaggerResponse(
                                        responseCode = "401",
                                        description = "Not authenticated"
                                ),
                                SwaggerResponse(
                                        responseCode = "403",
                                        description = "Missing required permission"
                                ),
                                SwaggerResponse(
                                        responseCode = "404",
                                        description = "Municipality not found"
                                )]
        )
        @PutMapping("/geo-location")
        @PreAuthorize("hasAuthority('PERMISSION_MANAGE_PROFILE')")
        fun updateMunicipalityGeoLocation(
                @Valid @RequestBody request: MunicipalityGeoLocationUpdateRequest
        ): ResponseEntity<ApiResponse<MunicipalityResponse>> {
                val municipality = municipalityService.updateMunicipalityGeoLocation(request)
                return ResponseEntity.ok(
                        ApiResponse.success(
                                data = municipality,
                                message =
                                        i18nMessageService.getMessage(
                                                "municipality.geolocation.update.success"
                                        )
                        )
                )
        }
}
