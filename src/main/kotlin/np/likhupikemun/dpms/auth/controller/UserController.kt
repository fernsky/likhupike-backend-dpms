package np.likhupikemun.dpms.auth.controller

import jakarta.validation.Valid
import np.likhupikemun.dpms.auth.service.UserService
import np.likhupikemun.dpms.common.dto.ApiResponse
import np.likhupikemun.dpms.auth.mapper.UserMapper
import np.likhupikemun.dpms.auth.domain.entity.User
import np.likhupikemun.dpms.auth.dto.*
import np.likhupikemun.dpms.auth.resolver.CurrentUserId
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import np.likhupikemun.dpms.common.service.I18nMessageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(
    name = "User Management",
    description = "Endpoints for managing users, permissions, and account operations"
)
@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
    private val i18nMessageService: I18nMessageService,
    private val log: Logger = LoggerFactory.getLogger(UserController::class.java)
) {
    @Operation(
        summary = "Create new user",
        description = "Create and automatically approve a new user account with specified permissions"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "User created successfully"),
        SwaggerResponse(responseCode = "400", description = "Invalid input data"),
        SwaggerResponse(responseCode = "401", description = "Not authenticated"),
        SwaggerResponse(responseCode = "403", description = "Missing CREATE_USER permission"),
        SwaggerResponse(responseCode = "409", description = "User already exists")
    ])
    @PostMapping
    @PreAuthorize("hasPermission(null, 'CREATE_USER')")
    fun createUser(
        @Valid @RequestBody request: CreateUserDto,
        @CurrentUserId currentUserId: UUID
    ): ResponseEntity<ApiResponse<UserResponse>> {
        log.debug("Creating and auto-approving user with data: $request by user: $currentUserId")
        
        // Create the user first
        val createdUser = userService.createUser(request)
        
        // Automatically approve the created user
        val approvedUser = userService.approveUser(createdUser.id!!, currentUserId)
        
        return ResponseEntity.ok(
            ApiResponse.success(
                data = UserMapper.toResponse(approvedUser),
                message = i18nMessageService.getMessage("user.create.success")
            )
        )
    }

    @Operation(
        summary = "Search users",
        description = "Search and filter users with pagination"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "Search successful"),
        SwaggerResponse(responseCode = "400", description = "Invalid search criteria"),
        SwaggerResponse(responseCode = "401", description = "Not authenticated"),
        SwaggerResponse(responseCode = "403", description = "Missing VIEW_USER permission"),
        SwaggerResponse(responseCode = "404", description = "Page does not exist")
    ])
    @GetMapping("/search")
    @PreAuthorize("hasPermission(null, 'VIEW_USER')")
    fun searchUsers(
        @Valid criteria: UserSearchCriteria
    ): ResponseEntity<ApiResponse<List<UserProjection>>> {
        log.debug("Searching users with criteria: {}", criteria)
        val users = userService.searchUsers(criteria)
        return ResponseEntity.ok(
            ApiResponse.withPage(
                page = users,
                message = i18nMessageService.getMessage("user.search.success")
            )
        )
    }

    @Operation(
        summary = "Approve user",
        description = "Approve a pending user registration"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "User approved successfully"),
        SwaggerResponse(responseCode = "401", description = "Not authenticated"),
        SwaggerResponse(responseCode = "403", description = "Missing APPROVE_USER permission"),
        SwaggerResponse(responseCode = "404", description = "User not found"),
        SwaggerResponse(responseCode = "409", description = "User already approved")
    ])
    @PostMapping("/{userId}/approve")
    @PreAuthorize("hasPermission(null, 'APPROVE_USER')")
    fun approveUser(
        @PathVariable userId: UUID,
        @CurrentUserId currentUserId: UUID
    ): ResponseEntity<ApiResponse<UserResponse>> {
        val approvedUser = userService.approveUser(userId, currentUserId)
        return ResponseEntity.ok(
            ApiResponse.success(
                data = UserMapper.toResponse(approvedUser),
                message = i18nMessageService.getMessage("user.approve.success")
            )
        )
    }

    @Operation(
        summary = "Delete user",
        description = "Soft delete a user account"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "User deleted successfully"),
        SwaggerResponse(responseCode = "401", description = "Not authenticated"),
        SwaggerResponse(responseCode = "403", description = "Missing DELETE_USER permission"),
        SwaggerResponse(responseCode = "404", description = "User not found"),
        SwaggerResponse(responseCode = "409", description = "User already deleted")
    ])
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasPermission(null, 'DELETE_USER')")
    fun deleteUser(
        @PathVariable userId: UUID,
         @CurrentUserId currentUserId: UUID
    ): ResponseEntity<ApiResponse<User>> {
        val deletedUser = userService.deleteUser(userId, currentUserId)
        return ResponseEntity.ok(
            ApiResponse.success(
                data = deletedUser,
                message = i18nMessageService.getMessage("user.delete.success")
            )
        )
    }

    @Operation(
        summary = "Update user permissions",
        description = "Modify the permissions assigned to a user"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "Permissions updated successfully"),
        SwaggerResponse(responseCode = "400", description = "Invalid permissions"),
        SwaggerResponse(responseCode = "401", description = "Not authenticated"),
        SwaggerResponse(responseCode = "403", description = "Missing EDIT_USER permission"),
        SwaggerResponse(responseCode = "404", description = "User not found")
    ])
    @PutMapping("/{userId}/permissions")
    @PreAuthorize("hasPermission(null, 'EDIT_USER')")
    fun updatePermissions(
        @PathVariable userId: UUID,
        @Valid @RequestBody permissions: UserPermissionsDto
    ): ResponseEntity<ApiResponse<User>> {
        val updatedUser = userService.updatePermissions(userId, permissions)
        return ResponseEntity.ok(
            ApiResponse.success(
                data = updatedUser,
                message = i18nMessageService.getMessage("user.permissions.update.success")
            )
        )
    }

    @Operation(
        summary = "Reset user password",
        description = "Administrative password reset for a user"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "Password reset successful"),
        SwaggerResponse(responseCode = "400", description = "Invalid password or passwords don't match"),
        SwaggerResponse(responseCode = "401", description = "Not authenticated"),
        SwaggerResponse(responseCode = "403", description = "Missing RESET_USER_PASSWORD permission"),
        SwaggerResponse(responseCode = "404", description = "User not found")
    ])
    @PostMapping("/{userId}/reset-password")
    @PreAuthorize("hasPermission(null, 'RESET_USER_PASSWORD')")
    fun resetPassword(
        @PathVariable userId: UUID,
        @Valid @RequestBody request: ResetUserPasswordRequest
    ): ResponseEntity<ApiResponse<User>> {
        val user = userService.resetPassword(userId, request.newPassword)
        return ResponseEntity.ok(
            ApiResponse.success(
                data = user,
                message = i18nMessageService.getMessage("user.password.reset.success")
            )
        )
    }

    @Operation(
        summary = "Get user by ID",
        description = "Retrieve detailed user information"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "User retrieved successfully"),
        SwaggerResponse(responseCode = "401", description = "Not authenticated"),
        SwaggerResponse(responseCode = "403", description = "Missing VIEW_USER permission"),
        SwaggerResponse(responseCode = "404", description = "User not found")
    ])
    @GetMapping("/{userId}")
    @PreAuthorize("hasPermission(null, 'VIEW_USER')")
    fun getUserById(
        @PathVariable userId: UUID
    ): ResponseEntity<ApiResponse<UserResponse>> {
        log.debug("Getting user details for ID: {}", userId)
        val user = userService.getUserById(userId)
        return ResponseEntity.ok(
            ApiResponse.success(
                data = UserMapper.toResponse(user),
                message = i18nMessageService.getMessage("user.get.success")
            )
        )
    }

    @Operation(
        summary = "Update user",
        description = "Update user information (email, ward access)"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "User updated successfully"),
        SwaggerResponse(responseCode = "400", description = "Invalid input data"),
        SwaggerResponse(responseCode = "401", description = "Not authenticated"),
        SwaggerResponse(responseCode = "403", description = "Missing EDIT_USER permission"),
        SwaggerResponse(responseCode = "404", description = "User not found"),
        SwaggerResponse(responseCode = "409", description = "Email already exists")
    ])
    @PutMapping("/{userId}")
    @PreAuthorize("hasPermission(null, 'EDIT_USER')")
    fun updateUser(
        @PathVariable userId: UUID,
        @Valid @RequestBody request: UpdateUserRequest
    ): ResponseEntity<ApiResponse<UserResponse>> {
        log.debug("Updating user {} with data: {}", userId, request)
        val updatedUser = userService.updateUser(userId, request)
        return ResponseEntity.ok(
            ApiResponse.success(
                data = UserMapper.toResponse(updatedUser),
                message = i18nMessageService.getMessage("user.update.success")
            )
        )
    }
}
