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

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
    private val i18nMessageService: I18nMessageService,
    private val log: Logger = LoggerFactory.getLogger(UserController::class.java)
) {
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
