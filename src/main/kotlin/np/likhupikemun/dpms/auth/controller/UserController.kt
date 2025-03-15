package np.likhupikemun.dpms.auth.controller

import jakarta.validation.Valid
import np.likhupikemun.dpms.auth.service.UserService
import np.likhupikemun.dpms.common.dto.ApiResponse
import np.likhupikemun.dpms.auth.mapper.UserMapper
import np.likhupikemun.dpms.auth.domain.entity.User
import np.likhupikemun.dpms.auth.dto.*
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
    private val log: Logger = LoggerFactory.getLogger(UserController::class.java)
) {
    @PostMapping
    @PreAuthorize("hasPermission(null, 'CREATE_USER')")
    fun createUser(
        @Valid @RequestBody request: CreateUserDto
    ): ResponseEntity<ApiResponse<UserResponse>> {
        val createdUser = userService.createUser(request)
        return ResponseEntity.ok(
            ApiResponse.success(
                data = UserMapper.toResponse(createdUser),
                message = "User created successfully"
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
                message = "Users retrieved successfully"
            )
        )
    }

    @PostMapping("/{userId}/approve")
    @PreAuthorize("hasPermission(null, 'APPROVE_USER')")
    fun approveUser(
        @PathVariable userId: UUID,
        @RequestAttribute("currentUserId") currentUserId: UUID
    ): ResponseEntity<ApiResponse<UserResponse>> {
        val approvedUser = userService.approveUser(userId, currentUserId)
        return ResponseEntity.ok(
            ApiResponse.success(
                data = UserMapper.toResponse(approvedUser),
                message = "User approved successfully"
            )
        )
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasPermission(null, 'DELETE_USER')")
    fun deleteUser(
        @PathVariable userId: UUID,
        @RequestAttribute("currentUser") currentUser: String
    ): ResponseEntity<ApiResponse<User>> {
        val deletedUser = userService.deleteUser(userId, currentUser)
        return ResponseEntity.ok(
            ApiResponse.success(
                data = deletedUser,
                message = "User deleted successfully"
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
                message = "User permissions updated successfully"
            )
        )
    }

    @PostMapping("/{userId}/reset-password")
    @PreAuthorize("hasPermission(null, 'RESET_USER_PASSWORD')")
    fun resetPassword(
        @PathVariable userId: UUID,
        @Valid @RequestBody request: ResetPasswordRequest
    ): ResponseEntity<ApiResponse<User>> {
        val user = userService.resetPassword(userId, request.newPassword)
        return ResponseEntity.ok(
            ApiResponse.success(
                data = user,
                message = "Password reset successfully"
            )
        )
    }
}
