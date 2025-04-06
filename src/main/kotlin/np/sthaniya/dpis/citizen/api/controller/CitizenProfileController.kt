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
import np.sthaniya.dpis.citizen.dto.profile.ChangePasswordDto
import np.sthaniya.dpis.citizen.dto.profile.RegisterCitizenDto
import np.sthaniya.dpis.citizen.dto.profile.UpdateProfileDto
import np.sthaniya.dpis.citizen.dto.response.CitizenResponse
import np.sthaniya.dpis.citizen.dto.response.DocumentUploadResponse
import np.sthaniya.dpis.auth.resolver.CurrentUserId
import np.sthaniya.dpis.common.dto.ApiResponse as DpisApiResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

/**
 * Controller interface for citizen self-registration and profile management.
 * 
 * Provides endpoints for citizens to register and manage their own profiles.
 */
@Tag(name = "Citizen Profile", description = "APIs for citizen self-registration and profile management")
@RequestMapping("/api/v1/citizens")
interface CitizenProfileController {

    /**
     * Endpoint for citizen self-registration.
     * 
     * @param registerCitizenDto The data for citizen registration
     * @return The created citizen profile information
     */
    @Operation(
        summary = "Register as a new citizen",
        description = "Allows a citizen to register themselves in the system with essential information. " +
                "Registration requires approval before full access is granted."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201", 
                description = "Citizen registered successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "400", description = "Invalid input data"),
            ApiResponse(responseCode = "409", description = "Conflict - Citizen with same citizenship number or email already exists"),
            ApiResponse(responseCode = "503", description = "Self-registration is currently disabled")
        ]
    )
    @PostMapping(
        path = ["/register"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun registerCitizen(
        @Parameter(
            description = "Citizen registration data",
            required = true
        )
        @Valid @RequestBody registerCitizenDto: RegisterCitizenDto
    ): ResponseEntity<DpisApiResponse<CitizenResponse>>

    /**
     * Retrieves the profile of the currently logged-in citizen.
     * 
     * @param currentUserId ID of the currently logged-in citizen
     * @return The citizen's profile information
     */
    @Operation(
        summary = "Get my citizen profile",
        description = "Retrieves the profile information of the currently logged-in citizen.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", 
                description = "Profile retrieved successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Profile not found")
        ]
    )
    @GetMapping(
        path = ["/me"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getMyProfile(
        @CurrentUserId currentUserId: UUID
    ): ResponseEntity<DpisApiResponse<CitizenResponse>>

    /**
     * Updates specific fields of the logged-in citizen's profile.
     * 
     * @param currentUserId ID of the currently logged-in citizen
     * @param updateProfileDto DTO with fields to update in the profile
     * @return The updated citizen profile information
     */
    @Operation(
        summary = "Update my citizen profile",
        description = "Updates specific fields of the currently logged-in citizen's profile.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", 
                description = "Profile updated successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "400", description = "Invalid input data"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Profile not found"),
            ApiResponse(responseCode = "409", description = "Conflict - Email already exists")
        ]
    )
    @PatchMapping(
        path = ["/me"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateMyProfile(
        @CurrentUserId currentUserId: UUID,
        
        @Parameter(
            description = "Profile fields to update",
            required = true
        )
        @Valid @RequestBody updateProfileDto: UpdateProfileDto
    ): ResponseEntity<DpisApiResponse<CitizenResponse>>

    /**
     * Changes the password for the logged-in citizen.
     * 
     * @param currentUserId ID of the currently logged-in citizen
     * @param changePasswordDto Password change data containing current and new passwords
     * @return The updated citizen profile
     */
    @Operation(
        summary = "Change my password",
        description = "Changes the password for the currently logged-in citizen.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", 
                description = "Password changed successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "400", description = "Invalid password data"),
            ApiResponse(responseCode = "401", description = "Unauthorized or incorrect current password"),
            ApiResponse(responseCode = "404", description = "Profile not found")
        ]
    )
    @PostMapping(
        path = ["/me/change-password"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun changeMyPassword(
        @CurrentUserId currentUserId: UUID,
        
        @Parameter(
            description = "Password change data",
            required = true
        )
        @Valid @RequestBody changePasswordDto: ChangePasswordDto
    ): ResponseEntity<DpisApiResponse<CitizenResponse>>

    /**
     * Uploads a citizen's photo.
     *
     * @param currentUserId ID of the currently logged-in citizen
     * @param photo The photo file to upload
     * @return Details about the uploaded photo
     */
    @Operation(
        summary = "Upload my photo",
        description = "Uploads or replaces the photo for the currently logged-in citizen.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Photo uploaded successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "400", description = "Invalid file format or size"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Profile not found")
        ]
    )
    @PostMapping(
        path = ["/me/photo"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun uploadMyPhoto(
        @CurrentUserId currentUserId: UUID,
        
        @Parameter(
            description = "Photo file to upload (JPG or PNG format, max 5MB)",
            required = true,
            schema = Schema(type = "string", format = "binary")
        )
        @RequestParam("photo") photo: MultipartFile
    ): ResponseEntity<DpisApiResponse<DocumentUploadResponse>>

    /**
     * Uploads the front page of a citizen's citizenship certificate.
     *
     * @param currentUserId ID of the currently logged-in citizen
     * @param document The document file to upload
     * @return Details about the uploaded document
     */
    @Operation(
        summary = "Upload citizenship certificate front page",
        description = "Uploads or replaces the front page of the citizenship certificate for the currently logged-in citizen.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Document uploaded successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "400", description = "Invalid file format or size"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Profile not found")
        ]
    )
    @PostMapping(
        path = ["/me/citizenship/front"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun uploadMyCitizenshipFront(
        @CurrentUserId currentUserId: UUID,
        
        @Parameter(
            description = "Document file to upload (JPG, PNG or PDF format, max 10MB)",
            required = true,
            schema = Schema(type = "string", format = "binary")
        )
        @RequestParam("document") document: MultipartFile
    ): ResponseEntity<DpisApiResponse<DocumentUploadResponse>>

    /**
     * Uploads the back page of a citizen's citizenship certificate.
     *
     * @param currentUserId ID of the currently logged-in citizen
     * @param document The document file to upload
     * @return Details about the uploaded document
     */
    @Operation(
        summary = "Upload citizenship certificate back page",
        description = "Uploads or replaces the back page of the citizenship certificate for the currently logged-in citizen.",
        security = [SecurityRequirement(name = "bearer-auth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Document uploaded successfully",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = DpisApiResponse::class)
                )]
            ),
            ApiResponse(responseCode = "400", description = "Invalid file format or size"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Profile not found")
        ]
    )
    @PostMapping(
        path = ["/me/citizenship/back"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun uploadMyCitizenshipBack(
        @CurrentUserId currentUserId: UUID,
        
        @Parameter(
            description = "Document file to upload (JPG, PNG or PDF format, max 10MB)",
            required = true,
            schema = Schema(type = "string", format = "binary")
        )
        @RequestParam("document") document: MultipartFile
    ): ResponseEntity<DpisApiResponse<DocumentUploadResponse>>
}