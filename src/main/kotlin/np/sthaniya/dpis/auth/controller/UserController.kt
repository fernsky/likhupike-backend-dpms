package np.sthaniya.dpis.auth.controller

import jakarta.validation.Valid
import np.sthaniya.dpis.auth.service.UserService
import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.auth.mapper.UserMapper
import np.sthaniya.dpis.auth.domain.entity.User
import np.sthaniya.dpis.auth.dto.*
import np.sthaniya.dpis.auth.resolver.CurrentUserId
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import np.sthaniya.dpis.common.service.I18nMessageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag

/**
 * REST controller handling user management operations in the Digital Profile System.
 *
 * This controller provides comprehensive user management capabilities including:
 * 1. User Administration:
 *    - User creation and approval
 *    - User search and retrieval
 *    - User updates and deletion
 *
 * 2. Permission Management:
 *    - Permission assignment
 *    - Permission updates
 *    - Role-based access control
 *
 * 3. User Account Operations:
 *    - Administrative password reset
 *    - Ward-level access management
 *
 * Security:
 * - All endpoints require authentication
 * - Permission-based access control using @PreAuthorize
 * - Audit logging for administrative actions
 *
 * Integration:
 * - Uses [UserService] for business logic implementation
 * - Integrates with [I18nMessageService] for internationalized responses
 * - Implements OpenAPI/Swagger documentation
 *
 * @property userService Service handling user management operations
 * @property i18nMessageService Service for internationalized messages
 */
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

    /**
     * Creates and automatically approves a new user account.
     *
     * This endpoint handles the administrative creation of users, including:
     * 1. User account creation with specified permissions
     * 2. Automatic approval by the creating admin
     * 3. Initial password setup
     *
     * Implementation:
     * - Uses [UserServiceImpl.createUser] for account creation
     * - Uses [UserServiceImpl.approveUser] for automatic approval
     *
     * Security:
     * - Requires CREATE_USER permission
     * - Validates input data
     * - Prevents duplicate emails
     *
     * @param request User creation details including permissions
     * @param currentUserId ID of the admin creating the user
     * @throws AuthException.UserAlreadyExistsException if email is taken
     */
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

    /**
     * Searches and filters users with pagination.
     *
     * Features:
     * - Dynamic filtering based on multiple criteria
     * - Pagination support
     * - Permission-based filtering
     * - Ward-level access restrictions
     *
     * Implementation:
     * - Uses [UserServiceImpl.searchUsers] with specifications
     * - Supports complex search criteria through [UserSearchCriteria]
     *
     * @param criteria Search and filter parameters
     * @throws AuthException.PageDoesNotExistException for invalid page numbers
     */
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

    /**
     * Approves a pending user registration.
     *
     * Implementation:
     * - Uses [UserServiceImpl.approveUser] for approval process
     *
     * Security:
     * - Requires APPROVE_USER permission
     * - Validates user existence and approval status
     *
     * @param userId ID of the user to approve
     * @param currentUserId ID of the admin approving the user
     * @throws AuthException.UserNotFoundException if user doesn't exist
     * @throws AuthException.UserAlreadyApprovedException if user is already approved
     */
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

    /**
     * Soft deletes a user account.
     *
     * Implementation:
     * - Uses [UserServiceImpl.deleteUser] for deletion process
     *
     * Security:
     * - Requires DELETE_USER permission
     * - Validates user existence and deletion status
     *
     * @param userId ID of the user to delete
     * @param currentUserId ID of the admin deleting the user
     * @throws AuthException.UserNotFoundException if user doesn't exist
     * @throws AuthException.UserAlreadyDeletedException if user is already deleted
     */
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

    /**
     * Updates user permissions.
     *
     * Permission Update Process:
     * 1. Validates requested permission changes
     * 2. Applies permission updates atomically
     * 3. Records permission change audit
     *
     * Implementation:
     * - Uses [UserServiceImpl.updatePermissions] for permission management
     * - Handles permission comparison and modification
     *
     * Security:
     * - Requires EDIT_USER permission
     * - Validates permission combinations
     * - Prevents unauthorized elevation
     *
     * @param userId ID of the user to update
     * @param permissions New permission set
     * @throws AuthException.UserNotFoundException if user doesn't exist
     * @throws AuthException.UserAlreadyDeletedException if user is deleted
     */
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

    /**
     * Updates user roles.
     *
     * Role Update Process:
     * 1. Validates requested role changes
     * 2. Applies role updates atomically
     * 3. Records role change audit
     *
     * Implementation:
     * - Uses [UserServiceImpl.updateRoles] for role management
     * - Handles role comparison and modification
     *
     * Security:
     * - Requires EDIT_USER permission
     * - Validates role combinations
     * - Prevents unauthorized elevation
     *
     * @param userId ID of the user to update
     * @param roles New role set
     * @throws AuthException.UserNotFoundException if user doesn't exist
     */
    @Operation(
        summary = "Update user roles",
        description = "Modify the roles assigned to a user"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "Roles updated successfully"),
        SwaggerResponse(responseCode = "400", description = "Invalid roles"),
        SwaggerResponse(responseCode = "401", description = "Not authenticated"),
        SwaggerResponse(responseCode = "403", description = "Missing EDIT_USER permission"),
        SwaggerResponse(responseCode = "404", description = "User not found")
    ])
    @PutMapping("/{userId}/roles")
    @PreAuthorize("hasPermission(null, 'EDIT_USER')")
    fun updateRoles(
        @PathVariable userId: UUID,
        @Valid @RequestBody roles: UserRolesDto
    ): ResponseEntity<ApiResponse<User>> {
        val updatedUser = userService.updateRoles(userId, roles)
        return ResponseEntity.ok(
            ApiResponse.success(
                data = updatedUser,
                message = i18nMessageService.getMessage("user.roles.update.success")
            )
        )
    }

    /**
     * Resets user password administratively.
     *
     * Implementation:
     * - Uses [UserServiceImpl.resetPassword] for password reset
     *
     * Security:
     * - Requires RESET_USER_PASSWORD permission
     * - Validates password strength and match
     *
     * @param userId ID of the user to reset password
     * @param request Password reset details
     * @throws AuthException.UserNotFoundException if user doesn't exist
     */
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

    /**
     * Retrieves detailed user information by ID.
     *
     * Implementation:
     * - Uses [UserServiceImpl.getUserById] for user retrieval
     *
     * Security:
     * - Requires VIEW_USER permission
     * - Validates user existence
     *
     * @param userId ID of the user to retrieve
     * @throws AuthException.UserNotFoundException if user doesn't exist
     */
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

    /**
     * Updates user information such as email and ward access.
     *
     * Implementation:
     * - Uses [UserServiceImpl.updateUser] for user update
     *
     * Security:
     * - Requires EDIT_USER permission
     * - Validates input data
     * - Prevents duplicate emails
     *
     * @param userId ID of the user to update
     * @param request User update details
     * @throws AuthException.UserNotFoundException if user doesn't exist
     * @throws AuthException.EmailAlreadyExistsException if email is taken
     */
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
