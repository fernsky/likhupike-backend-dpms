package np.likhupikemun.dpms.auth.controller

import np.likhupikemun.dpms.auth.service.AuthService
import np.likhupikemun.dpms.auth.dto.*
import np.likhupikemun.dpms.common.dto.ApiResponse
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth", produces = [MediaType.APPLICATION_JSON_VALUE])
class AuthController(
    private val authService: AuthService,
    private val i18nMessageService: I18nMessageService
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping("/register", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun register(
        @Valid @RequestBody request: RegisterRequest
    ): ResponseEntity<ApiResponse<RegisterResponse>> {
        logger.debug("Processing registration request for email: {}", request.email)
        val response = authService.register(request)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(
                data = response,
                message = i18nMessageService.getMessage("auth.register.success")
            ))
    }

    @PostMapping("/login", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun login(
        @Valid @RequestBody request: LoginRequest
    ): ResponseEntity<ApiResponse<AuthResponse>> {
        logger.debug("Processing login request for email: {}", request.email)
        val response = authService.login(request)
        return ResponseEntity.ok(
            ApiResponse.success(
                data = response,
                message = i18nMessageService.getMessage("auth.login.success")
            )
        )
    }

    @PostMapping("/refresh")
    fun refreshToken(
        @RequestHeader("X-Refresh-Token") refreshToken: String
    ): ResponseEntity<ApiResponse<AuthResponse>> {
        logger.debug("Processing token refresh request")
        val response = authService.refreshToken(refreshToken)
        return ResponseEntity.ok(
            ApiResponse.success(
                data = response,
                message = i18nMessageService.getMessage("auth.token.refresh.success")
            )
        )
    }

    @PostMapping("/logout")
    fun logout(
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<ApiResponse<Nothing>> {
        logger.debug("Processing logout request")
        val cleanToken = token.substringAfter("Bearer ").trim()
        authService.logout(cleanToken)
        return ResponseEntity.ok(
            ApiResponse.success(
                message = i18nMessageService.getMessage("auth.logout.success")
            )
        )
    }

    @PostMapping("/password-reset/request")
    fun requestPasswordReset(
        @Valid @RequestBody request: RequestPasswordResetRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        logger.debug("Processing password reset request for email: {}", request.email)
        authService.requestPasswordReset(request)
        return ResponseEntity.ok(
            ApiResponse.success(
                message = i18nMessageService.getMessage("auth.password.reset.request.success")
            )
        )
    }

    @PostMapping("/password-reset/reset")
    fun resetPassword(
        @Valid @RequestBody request: ResetPasswordRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        logger.debug("Processing password reset")
        authService.resetPassword(request)
        return ResponseEntity.ok(
            ApiResponse.success(
                message = i18nMessageService.getMessage("auth.password.reset.success")
            )
        )
    }

    @PostMapping("/change-password")
    fun changePassword(
        @CurrentUserId currentUserId: UUID,
        @Valid @RequestBody request: ChangePasswordRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        logger.debug("Processing change password request for user: {}", currentUserId)
        authService.changePassword(currentUserId, request)
        return ResponseEntity.ok(
            ApiResponse.success(
                message = i18nMessageService.getMessage("auth.password.change.success")
            )
        )
    }
}
