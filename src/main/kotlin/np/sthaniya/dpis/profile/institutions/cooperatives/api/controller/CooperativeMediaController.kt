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
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.CreateCooperativeMediaDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.UpdateCooperativeMediaDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeMediaResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.MediaUploadResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeMediaType
import np.sthaniya.dpis.profile.institutions.cooperatives.model.MediaVisibilityStatus
import np.sthaniya.dpis.common.dto.ApiResponse as DpisApiResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

/**
 * Controller interface for managing cooperative media assets.
 */
@Tag(name = "Cooperative Media", description = "APIs for managing cooperative media assets")
@RequestMapping("/api/v1/cooperatives")
interface CooperativeMediaController {

    /**
     * Uploads a media file for a cooperative.
     *
     * @param cooperativeId The cooperative ID
     * @param file The media file to upload
     * @param createDto The media metadata
     * @return The upload response with file information
     */
    @Operation(
        summary = "Upload media",
        description = "Uploads a media file for a cooperative. " +
                "Requires MANAGE_COOPERATIVE_MEDIA permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Media uploaded successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "400", description = "Invalid input or media too large"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Not found - Cooperative does not exist"),
            ApiResponse(responseCode = "415", description = "Unsupported media type")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_MANAGE_COOPERATIVE_MEDIA')")
    @PostMapping(
        path = ["/{cooperativeId}/media"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun uploadMedia(
        @Parameter(description = "Cooperative ID", required = true)
        @PathVariable cooperativeId: UUID,
        @Parameter(description = "Media file", required = true)
        @RequestParam("file") file: MultipartFile,
        @Parameter(description = "Media metadata", required = true)
        @Valid @ModelAttribute createDto: CreateCooperativeMediaDto
    ): ResponseEntity<DpisApiResponse<MediaUploadResponse>>

    /**
     * Updates metadata for an existing media item.
     *
     * @param cooperativeId The cooperative ID
     * @param mediaId The media ID
     * @param updateDto The updated metadata
     * @return The updated media item
     */
    @Operation(
        summary = "Update media metadata",
        description = "Updates metadata for an existing media item. " +
                "Requires MANAGE_COOPERATIVE_MEDIA permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Media metadata updated successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "400", description = "Invalid input data"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Not found - Media does not exist")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_MANAGE_COOPERATIVE_MEDIA')")
    @PutMapping(
        path = ["/{cooperativeId}/media/{mediaId}"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateMediaMetadata(
        @Parameter(description = "Cooperative ID", required = true)
        @PathVariable cooperativeId: UUID,
        @Parameter(description = "Media ID", required = true)
        @PathVariable mediaId: UUID,
        @Parameter(description = "Updated media metadata", required = true)
        @Valid @RequestBody updateDto: UpdateCooperativeMediaDto
    ): ResponseEntity<DpisApiResponse<CooperativeMediaResponse>>

    /**
     * Retrieves a media item by ID.
     *
     * @param cooperativeId The cooperative ID
     * @param mediaId The media ID
     * @return The media item
     */
    @Operation(
        summary = "Get media by ID",
        description = "Retrieves a media item by its ID. " +
                "Requires VIEW_COOPERATIVE permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Media retrieved successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Not found - Media does not exist")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_VIEW_COOPERATIVE')")
    @GetMapping(
        path = ["/{cooperativeId}/media/{mediaId}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getMediaById(
        @Parameter(description = "Cooperative ID", required = true)
        @PathVariable cooperativeId: UUID,
        @Parameter(description = "Media ID", required = true)
        @PathVariable mediaId: UUID
    ): ResponseEntity<DpisApiResponse<CooperativeMediaResponse>>

    /**
     * Retrieves all media for a cooperative with pagination.
     *
     * @param cooperativeId The cooperative ID
     * @param pageable Pagination information
     * @return A paginated list of media items
     */
    @Operation(
        summary = "Get all media for a cooperative",
        description = "Retrieves all media items for a cooperative with pagination. " +
                "Requires VIEW_COOPERATIVE permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Media items retrieved successfully",
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
        path = ["/{cooperativeId}/media"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAllMediaForCooperative(
        @Parameter(description = "Cooperative ID", required = true)
        @PathVariable cooperativeId: UUID,
        @Parameter(description = "Pagination parameters")
        pageable: Pageable
    ): ResponseEntity<DpisApiResponse<Page<CooperativeMediaResponse>>>

    /**
     * Retrieves media for a cooperative of a specific type.
     *
     * @param cooperativeId The cooperative ID
     * @param type The media type
     * @param pageable Pagination information
     * @return A paginated list of media items
     */
    @Operation(
        summary = "Get media by type",
        description = "Retrieves media items for a cooperative filtered by type. " +
                "Requires VIEW_COOPERATIVE permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Media items retrieved successfully",
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
        path = ["/{cooperativeId}/media/by-type/{type}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getMediaByType(
        @Parameter(description = "Cooperative ID", required = true)
        @PathVariable cooperativeId: UUID,
        @Parameter(description = "Media type", required = true)
        @PathVariable type: CooperativeMediaType,
        @Parameter(description = "Pagination parameters")
        pageable: Pageable
    ): ResponseEntity<DpisApiResponse<Page<CooperativeMediaResponse>>>

    /**
     * Deletes a media item.
     *
     * @param cooperativeId The cooperative ID
     * @param mediaId The media ID
     * @return A success response
     */
    @Operation(
        summary = "Delete media",
        description = "Deletes a media item from the system. " +
                "Requires MANAGE_COOPERATIVE_MEDIA permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Media deleted successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Not found - Media does not exist")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_MANAGE_COOPERATIVE_MEDIA')")
    @DeleteMapping(
        path = ["/{cooperativeId}/media/{mediaId}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun deleteMedia(
        @Parameter(description = "Cooperative ID", required = true)
        @PathVariable cooperativeId: UUID,
        @Parameter(description = "Media ID", required = true)
        @PathVariable mediaId: UUID
    ): ResponseEntity<DpisApiResponse<Void>>

    /**
     * Sets a media item as primary for its type.
     *
     * @param cooperativeId The cooperative ID
     * @param mediaId The media ID
     * @return The updated media item
     */
    @Operation(
        summary = "Set media as primary",
        description = "Sets a media item as the primary one for its type. " +
                "Requires MANAGE_COOPERATIVE_MEDIA permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Media set as primary successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Not found - Media does not exist")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_MANAGE_COOPERATIVE_MEDIA')")
    @PostMapping(
        path = ["/{cooperativeId}/media/{mediaId}/set-primary"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun setMediaAsPrimary(
        @Parameter(description = "Cooperative ID", required = true)
        @PathVariable cooperativeId: UUID,
        @Parameter(description = "Media ID", required = true)
        @PathVariable mediaId: UUID
    ): ResponseEntity<DpisApiResponse<CooperativeMediaResponse>>

    /**
     * Updates the visibility status of a media item.
     *
     * @param cooperativeId The cooperative ID
     * @param mediaId The media ID
     * @param status The new visibility status
     * @return The updated media item
     */
    @Operation(
        summary = "Update media visibility",
        description = "Updates the visibility status of a media item. " +
                "Requires MANAGE_COOPERATIVE_MEDIA permission.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Visibility updated successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "400", description = "Invalid status"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Not found - Media does not exist")
        ]
    )
    @PreAuthorize("hasAuthority('PERMISSION_MANAGE_COOPERATIVE_MEDIA')")
    @PatchMapping(
        path = ["/{cooperativeId}/media/{mediaId}/visibility"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateMediaVisibility(
        @Parameter(description = "Cooperative ID", required = true)
        @PathVariable cooperativeId: UUID,
        @Parameter(description = "Media ID", required = true)
        @PathVariable mediaId: UUID,
        @Parameter(description = "New visibility status", required = true)
        @RequestParam status: MediaVisibilityStatus
    ): ResponseEntity<DpisApiResponse<CooperativeMediaResponse>>
}
