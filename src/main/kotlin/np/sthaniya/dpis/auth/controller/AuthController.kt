package np.sthaniya.dpis.auth.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import np.sthaniya.dpis.common.annotation.Public
import np.sthaniya.dpis.auth.service.AuthService
import np.sthaniya.dpis.auth.dto.*
import np.sthaniya.dpis.common.dto.ApiResponse
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import np.sthaniya.dpis.common.service.I18nMessageService
import np.sthaniya.dpis.auth.resolver.CurrentUserId
import java.util.*

/**
 * REST controller handling all authentication-related endpoints in the Digital Profile System.
 *
 * This controller provides a comprehensive authentication API including:
 * 1. User Registration Flow:
 *    - New user registration with email verification
 *    - Pending admin approval system
 *
 * 2. Authentication Flow:
 *    - Login with email/password
 *    - JWT token generation and refresh
 *    - Secure logout mechanism
 *
 * 3. Password Management:
 *    - Password reset via email OTP
 *    - Password change for authenticated users
 *
 * Security Features:
 * - JWT-based stateless authentication
 * - Rate limiting on sensitive operations
 * - Email verification for password reset
 * - Secure password handling
 *
 * Integration Points:
 * - Uses [AuthService] for business logic
 * - Integrates with [I18nMessageService] for internationalized responses
 * - OpenAPI/Swagger documentation for API discovery
 * - Standardized [ApiResponse] wrapper for all responses
 *
 * Error Handling:
 * - Comprehensive validation error responses
 * - Proper HTTP status codes for different scenarios
 * - Detailed error messages through i18n
 *
 * @property authService Service handling core authentication logic
 * @property i18nMessageService Service for internationalized message handling
 */
@Tag(name = "Authentication", description = "Endpoints for user authentication and account management")
@RestController
@RequestMapping("/api/v1/auth", produces = [MediaType.APPLICATION_JSON_VALUE])
class AuthController(
    private val authService: AuthService,
    private val i18nMessageService: I18nMessageService
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Registers a new user in the system.
     *
     * This endpoint:
     * 1. Validates user input (email format, password requirements)
     * 2. Creates a new unverified user account
     * 3. Queues account for admin approval
     *
     * Registration Flow:
     * 1. User submits registration data
     * 2. System validates input
     * 3. Creates pending account
     * 4. Admin must approve account
     * 5. User can then login
     *
     * Security:
     * - Public endpoint (no authentication required)
     * - Rate limited to prevent abuse
     * - Password complexity validation
     *
     * @param request Registration details including email and password
     * @return ResponseEntity containing registration confirmation
     * @throws AuthException.UserAlreadyExistsException if email is taken
     */
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

    /**
     * Authenticates a user and provides JWT access tokens.
     *
     * Authentication Process:
     * 1. Validates credentials
     * 2. Checks account status (approved, not deleted)
     * 3. Generates JWT token pair (access + refresh)
     * 4. Returns tokens with user details
     *
     * Security Features:
     * - Rate limiting on failed attempts
     * - Account status validation
     * - Secure token generation
     *
     * @param request Login credentials (email and password)
     * @return JWT tokens and user information
     * @throws AuthException.InvalidCredentialsException for wrong credentials
     * @throws AuthException.AccountNotApprovedException if account pending approval
     */
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

    /**
     * Refreshes authentication token using a valid refresh token.
     *
     * Token Refresh Process:
     * 1. Validates refresh token
     * 2. Generates new access token
     * 3. Returns new token pair
     *
     * Security Features:
     * - Requires valid refresh token
     * - Invalidates old tokens
     *
     * @param refreshToken Refresh token provided in the request header
     * @return New JWT token pair
     * @throws AuthException.InvalidTokenException if refresh token is invalid or expired
     */
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

    /**
     * Logs out the authenticated user by invalidating the current token.
     *
     * Logout Process:
     * 1. Validates current token
     * 2. Invalidates token in the system
     * 3. Confirms logout
     *
     * Security Features:
     * - Requires valid authentication token
     * - Invalidates token securely
     *
     * @param token Bearer token provided in the request header
     * @return Confirmation of logout
     * @throws AuthException.InvalidTokenException if token is invalid
     */
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

    /**
     * Requests a password reset OTP to be sent via email.
     *
     * Password Reset Request Process:
     * 1. Validates user email
     * 2. Generates OTP
     * 3. Sends OTP via email
     *
     * Security Features:
     * - Public endpoint (no authentication required)
     * - Rate limited to prevent abuse
     *
     * @param request Email address for password reset
     * @return Confirmation of OTP sent
     * @throws AuthException.UserNotFoundException if user does not exist
     */
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

    /**
     * Resets password using the OTP received via email.
     *
     * Password Reset Process:
     * 1. Validates OTP
     * 2. Checks new password requirements
     * 3. Updates password securely
     *
     * Security Features:
     * - Public endpoint (no authentication required)
     * - Validates OTP and password complexity
     *
     * @param request OTP and new password details
     * @return Confirmation of password reset
     * @throws AuthException.InvalidOtpException if OTP is invalid
     * @throws AuthException.InvalidPasswordException if new password doesn't meet requirements
     */
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

    /**
     * Changes password for an authenticated user.
     *
     * Password Change Process:
     * 1. Validates current password
     * 2. Checks new password requirements
     * 3. Updates password securely
     * 4. Invalidates all existing sessions
     *
     * Security:
     * - Requires authentication
     * - Validates current password
     * - Enforces password complexity
     * - Logs out all sessions
     *
     * @param currentUserId ID of the authenticated user
     * @param request Password change details
     * @return Confirmation of password change
     * @throws AuthException.InvalidCredentialsException if current password is wrong
     * @throws AuthException.InvalidPasswordException if new password doesn't meet requirements
     */
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
