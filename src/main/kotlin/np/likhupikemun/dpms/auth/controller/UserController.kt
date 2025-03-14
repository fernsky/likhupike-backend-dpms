package np.likhupikemun.dpms.auth.controller

import jakarta.validation.Valid
import np.likhupikemun.dpms.auth.service.UserService
import np.likhupikemun.dpms.common.dto.ApiResponse
import np.likhupikemun.dpms.auth.mapper.UserMapper
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {
    @PostMapping
    @PreAuthorize("hasPermission('CREATE_USER')")
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
    @PreAuthorize("hasPermission('VIEW_USER')")
    fun searchUsers(
        @Valid criteria: UserSearchCriteria
    ): ResponseEntity<ApiResponse<List<UserProjection>>> {
        val users = userService.searchUsers(criteria)
        return ResponseEntity.ok(
            ApiResponse.success(
                data = users,
                message = "Users retrieved successfully"
            )
        )
    }

    @PostMapping("/{userId}/approve")
    @PreAuthorize("hasPermission('APPROVE_USER')")
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
    @PreAuthorize("hasPermission('DELETE_USER')")
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
    @PreAuthorize("hasPermission('EDIT_USER')")
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
    @PreAuthorize("hasPermission('RESET_USER_PASSWORD')")
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
