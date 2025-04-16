package np.sthaniya.dpis.profile.location.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import np.sthaniya.dpis.profile.location.dto.WardCreateRequest
import np.sthaniya.dpis.profile.location.dto.WardResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import np.sthaniya.dpis.profile.location.dto.WardUpdateRequest
import np.sthaniya.dpis.profile.location.service.WardService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@Tag(
    name = "Ward Management",
    description = "Endpoints for managing wards within the municipality"
)
@RestController
@RequestMapping("/api/v1/wards")
class WardController(
    private val wardService: WardService,
    private val i18nMessageService: I18nMessageService
) {

    @Operation(
        summary = "Create a new ward",
        description = "Create a new ward within the municipality"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "201", description = "Ward created successfully"),
        SwaggerResponse(responseCode = "400", description = "Invalid input data"),
        SwaggerResponse(responseCode = "401", description = "Not authenticated"),
        SwaggerResponse(responseCode = "403", description = "Missing required permission"),
        SwaggerResponse(responseCode = "409", description = "Ward number already exists")
    ])
    @PostMapping
    @PreAuthorize("hasPermission(null, 'MANAGE_PROFILE')")
    fun createWard(
        @Valid @RequestBody request: WardCreateRequest
    ): ResponseEntity<ApiResponse<WardResponse>> {
        val ward = wardService.createWard(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.success(
                data = ward,
                message = i18nMessageService.getMessage("ward.create.success")
            )
        )
    }

    @Operation(
        summary = "Get all wards",
        description = "Get a list of all wards in the municipality"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "Wards retrieved successfully"),
        SwaggerResponse(responseCode = "401", description = "Not authenticated"),
        SwaggerResponse(responseCode = "403", description = "Missing required permission")
    ])
    @GetMapping
    @PreAuthorize("hasPermission(null, 'MANAGE_PROFILE')")
    fun getAllWards(): ResponseEntity<ApiResponse<List<WardResponse>>> {
        val wards = wardService.getAllWards()
        return ResponseEntity.ok(
            ApiResponse.success(
                data = wards,
                message = i18nMessageService.getMessage("ward.getAll.success")
            )
        )
    }

    @Operation(
        summary = "Get ward by ID",
        description = "Get detailed information about a specific ward"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "Ward retrieved successfully"),
        SwaggerResponse(responseCode = "401", description = "Not authenticated"),
        SwaggerResponse(responseCode = "403", description = "Missing required permission"),
        SwaggerResponse(responseCode = "404", description = "Ward not found")
    ])
    @GetMapping("/{wardId}")
    @PreAuthorize("hasPermission(null, 'MANAGE_PROFILE')")
    fun getWardById(@PathVariable wardId: UUID): ResponseEntity<ApiResponse<WardResponse>> {
        val ward = wardService.getWardById(wardId)
        return ResponseEntity.ok(
            ApiResponse.success(
                data = ward,
                message = i18nMessageService.getMessage("ward.get.success")
            )
        )
    }

    @Operation(
        summary = "Get ward by number",
        description = "Get detailed information about a ward by its number"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "Ward retrieved successfully"),
        SwaggerResponse(responseCode = "401", description = "Not authenticated"),
        SwaggerResponse(responseCode = "403", description = "Missing required permission"),
        SwaggerResponse(responseCode = "404", description = "Ward not found")
    ])
    @GetMapping("/number/{number}")
    @PreAuthorize("hasPermission(null, 'MANAGE_PROFILE')")
    fun getWardByNumber(@PathVariable number: Int): ResponseEntity<ApiResponse<WardResponse>> {
        val ward = wardService.getWardByNumber(number)
        return ResponseEntity.ok(
            ApiResponse.success(
                data = ward,
                message = i18nMessageService.getMessage("ward.get.success")
            )
        )
    }

    @Operation(
        summary = "Update ward information",
        description = "Update details of an existing ward"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "Ward updated successfully"),
        SwaggerResponse(responseCode = "400", description = "Invalid input data"),
        SwaggerResponse(responseCode = "401", description = "Not authenticated"),
        SwaggerResponse(responseCode = "403", description = "Missing required permission"),
        SwaggerResponse(responseCode = "404", description = "Ward not found")
    ])
    @PutMapping("/{wardId}")
    @PreAuthorize("hasPermission(null, 'MANAGE_PROFILE')")
    fun updateWard(
        @PathVariable wardId: UUID,
        @Valid @RequestBody request: WardUpdateRequest
    ): ResponseEntity<ApiResponse<WardResponse>> {
        val ward = wardService.updateWard(wardId, request)
        return ResponseEntity.ok(
            ApiResponse.success(
                data = ward,
                message = i18nMessageService.getMessage("ward.update.success")
            )
        )
    }

    @Operation(
        summary = "Delete ward",
        description = "Delete a ward and all associated settlement areas"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "Ward deleted successfully"),
        SwaggerResponse(responseCode = "401", description = "Not authenticated"),
        SwaggerResponse(responseCode = "403", description = "Missing required permission"),
        SwaggerResponse(responseCode = "404", description = "Ward not found")
    ])
    @DeleteMapping("/{wardId}")
    @PreAuthorize("hasPermission(null, 'MANAGE_PROFILE')")
    fun deleteWard(@PathVariable wardId: UUID): ResponseEntity<ApiResponse<Void>> {
        wardService.deleteWard(wardId)
        return ResponseEntity.ok(
            ApiResponse.success(
                message = i18nMessageService.getMessage("ward.delete.success")
            )
        )
    }
}
