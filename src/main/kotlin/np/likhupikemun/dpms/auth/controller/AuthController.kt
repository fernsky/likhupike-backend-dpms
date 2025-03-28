package np.likhupikemun.dpms.auth.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import np.likhupikemun.dpms.common.annotation.Public
import np.likhupikemun.dpms.auth.service.AuthService
import np.likhupikemun.dpms.auth.dto.*
import np.likhupikemun.dpms.common.dto.ApiResponse
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import np.likhupikemun.dpms.common.service.I18nMessageService
import np.likhupikemun.dpms.auth.resolver.CurrentUserId
import java.util.*

@Tag(
    name = "Authentication",
    description = "Endpoints for user authentication and account management"
)
@RestController
@RequestMapping("/api/v1/auth", produces = [MediaType.APPLICATION_JSON_VALUE])
class AuthController(
    private val authService: AuthService,
    private val i18nMessageService: I18nMessageService
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Operation(
        summary = "Register new user",
        description = "Register a new user account. The account will require admin approval before it can be used."
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "201", description = "User registered successfully"),
        SwaggerResponse(responseCode = "400", description = "Invalid input"),
        SwaggerResponse(responseCode = "409", description = "User already exists", content = [
            Content(schema = Schema(implementation = ApiResponse::class))
        ])
    ])
    @Public
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

    @Operation(
        summary = "Authenticate user",
        description = "Authenticate user credentials and receive access token"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "Authentication successful"),
        SwaggerResponse(responseCode = "400", description = "Invalid credentials"),
        SwaggerResponse(responseCode = "403", description = "Account not approved"),
        SwaggerResponse(responseCode = "429", description = "Too many attempts")
    ])
    @Public
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

    @Operation(
        summary = "Refresh authentication token",
        description = "Get new access token using refresh token"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "Token refreshed successfully"),
        SwaggerResponse(responseCode = "401", description = "Invalid or expired refresh token")
    ])
    @Public
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

    @Operation(
        summary = "Logout user",
        description = "Invalidate current authentication token"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "Logged out successfully"),
        SwaggerResponse(responseCode = "401", description = "Invalid token")
    ])
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

    @Operation(
        summary = "Request password reset",
        description = "Request a password reset OTP that will be sent via email"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "Password reset OTP sent"),
        SwaggerResponse(responseCode = "404", description = "User not found"),
        SwaggerResponse(responseCode = "429", description = "Too many reset attempts")
    ])
    @Public
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

    @Operation(
        summary = "Reset password using OTP",
        description = "Reset password using the OTP received via email"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "Password reset successful"),
        SwaggerResponse(responseCode = "400", description = "Invalid OTP or password requirements not met"),
        SwaggerResponse(responseCode = "404", description = "User not found"),
        SwaggerResponse(responseCode = "429", description = "Too many attempts")
    ])
    @Public
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

    @Operation(
        summary = "Change password",
        description = "Change password for authenticated user"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "Password changed successfully"),
        SwaggerResponse(responseCode = "400", description = "Invalid current password or password requirements not met"),
        SwaggerResponse(responseCode = "401", description = "Not authenticated")
    ])
    @PostMapping("/change-password")
    fun changePassword(
        @Parameter(hidden = true)
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
