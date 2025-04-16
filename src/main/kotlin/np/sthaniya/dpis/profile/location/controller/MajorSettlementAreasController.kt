package np.sthaniya.dpis.profile.location.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerResponse
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import np.sthaniya.dpis.profile.location.dto.MajorSettlementAreasCreateRequest
import np.sthaniya.dpis.profile.location.dto.MajorSettlementAreasResponse
import np.sthaniya.dpis.profile.location.dto.MajorSettlementAreasUpdateRequest
import np.sthaniya.dpis.profile.location.service.MajorSettlementAreasService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@Tag(
    name = "Settlement Area Management",
    description = "Endpoints for managing major settlement areas within wards"
)
@RestController
@RequestMapping("/api/v1/profile/location/settlements")
class MajorSettlementAreasController(
    private val settlementService: MajorSettlementAreasService,
    private val i18nMessageService: I18nMessageService
) {

    @Operation(
        summary = "Create a new settlement area",
        description = "Create a new major settlement area within a ward"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "201", description = "Settlement area created successfully"),
        SwaggerResponse(responseCode = "400", description = "Invalid input data"),
        SwaggerResponse(responseCode = "401", description = "Not authenticated"),
        SwaggerResponse(responseCode = "403", description = "Missing required permission"),
        SwaggerResponse(responseCode = "404", description = "Referenced ward not found")
    ])
    @PostMapping
    @PreAuthorize("hasPermission(null, 'MANAGE_PROFILE')")
    fun createSettlement(
        @Valid @RequestBody request: MajorSettlementAreasCreateRequest
    ): ResponseEntity<ApiResponse<MajorSettlementAreasResponse>> {
        val settlement = settlementService.createSettlement(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.success(
                data = settlement,
                message = i18nMessageService.getMessage("settlement.create.success")
            )
        )
    }

    @Operation(
        summary = "Get all settlement areas",
        description = "Get a list of all major settlement areas across all wards"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "Settlement areas retrieved successfully"),
        SwaggerResponse(responseCode = "401", description = "Not authenticated"),
        SwaggerResponse(responseCode = "403", description = "Missing required permission")
    ])
    @GetMapping
    @PreAuthorize("hasPermission(null, 'MANAGE_PROFILE')")
    fun getAllSettlements(): ResponseEntity<ApiResponse<List<MajorSettlementAreasResponse>>> {
        val settlements = settlementService.getAllSettlements()
        return ResponseEntity.ok(
            ApiResponse.success(
                data = settlements,
                message = i18nMessageService.getMessage("settlement.getAll.success")
            )
        )
    }

    @Operation(
        summary = "Get settlement area by ID",
        description = "Get detailed information about a specific settlement area"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "Settlement area retrieved successfully"),
        SwaggerResponse(responseCode = "401", description = "Not authenticated"),
        SwaggerResponse(responseCode = "403", description = "Missing required permission"),
        SwaggerResponse(responseCode = "404", description = "Settlement area not found")
    ])
    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'MANAGE_PROFILE')")
    fun getSettlementById(@PathVariable id: UUID): ResponseEntity<ApiResponse<MajorSettlementAreasResponse>> {
        val settlement = settlementService.getSettlementById(id)
        return ResponseEntity.ok(
            ApiResponse.success(
                data = settlement,
                message = i18nMessageService.getMessage("settlement.get.success")
            )
        )
    }

    @Operation(
        summary = "Get settlement areas by ward",
        description = "Get all settlement areas within a specific ward"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "Settlement areas retrieved successfully"),
        SwaggerResponse(responseCode = "401", description = "Not authenticated"),
        SwaggerResponse(responseCode = "403", description = "Missing required permission"),
        SwaggerResponse(responseCode = "404", description = "Ward not found")
    ])
    @GetMapping("/ward/{wardId}")
    @PreAuthorize("hasPermission(null, 'MANAGE_PROFILE')")
    fun getSettlementsByWard(@PathVariable wardId: UUID): ResponseEntity<ApiResponse<List<MajorSettlementAreasResponse>>> {
        val settlements = settlementService.getSettlementsByWardId(wardId)
        return ResponseEntity.ok(
            ApiResponse.success(
                data = settlements,
                message = i18nMessageService.getMessage("settlement.getByWard.success")
            )
        )
    }

    @Operation(
        summary = "Search settlement areas by name",
        description = "Search for settlement areas containing the specified name"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "Search completed successfully"),
        SwaggerResponse(responseCode = "401", description = "Not authenticated"),
        SwaggerResponse(responseCode = "403", description = "Missing required permission")
    ])
    @GetMapping("/search")
    @PreAuthorize("hasPermission(null, 'MANAGE_PROFILE')")
    fun searchSettlementsByName(@RequestParam name: String): ResponseEntity<ApiResponse<List<MajorSettlementAreasResponse>>> {
        val settlements = settlementService.searchSettlementsByName(name)
        return ResponseEntity.ok(
            ApiResponse.success(
                data = settlements,
                message = i18nMessageService.getMessage("settlement.search.success")
            )
        )
    }

    @Operation(
        summary = "Update settlement area",
        description = "Update a settlement area's information"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "Settlement area updated successfully"),
        SwaggerResponse(responseCode = "400", description = "Invalid input data"),
        SwaggerResponse(responseCode = "401", description = "Not authenticated"),
        SwaggerResponse(responseCode = "403", description = "Missing required permission"),
        SwaggerResponse(responseCode = "404", description = "Settlement area not found")
    ])
    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'MANAGE_PROFILE')")
    fun updateSettlement(
        @PathVariable id: UUID,
        @Valid @RequestBody request: MajorSettlementAreasUpdateRequest
    ): ResponseEntity<ApiResponse<MajorSettlementAreasResponse>> {
        val settlement = settlementService.updateSettlement(id, request)
        return ResponseEntity.ok(
            ApiResponse.success(
                data = settlement,
                message = i18nMessageService.getMessage("settlement.update.success")
            )
        )
    }

    @Operation(
        summary = "Delete settlement area",
        description = "Delete a settlement area"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "Settlement area deleted successfully"),
        SwaggerResponse(responseCode = "401", description = "Not authenticated"),
        SwaggerResponse(responseCode = "403", description = "Missing required permission"),
        SwaggerResponse(responseCode = "404", description = "Settlement area not found")
    ])
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'MANAGE_PROFILE')")
    fun deleteSettlement(@PathVariable id: UUID): ResponseEntity<ApiResponse<Void>> {
        settlementService.deleteSettlement(id)
        return ResponseEntity.ok(
            ApiResponse.success(
                message = i18nMessageService.getMessage("settlement.delete.success")
            )
        )
    }
}
