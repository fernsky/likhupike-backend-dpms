package np.sthaniya.dpis.citizen.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import np.sthaniya.dpis.citizen.dto.auth.*
import np.sthaniya.dpis.common.annotation.Public
import np.sthaniya.dpis.common.dto.ApiResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import java.util.UUID

/**
 * REST controller interface for citizen authentication operations.
 *
 * This interface defines the API endpoints for citizen authentication, including:
 * 1. Authentication Flow:
 *    - Login with email/password
 *    - JWT token generation and refresh
 *    - Secure logout mechanism
 *
 * 2. Password Management:
 *    - Password reset via email OTP
 *    - Password change for authenticated citizens
 */
@Tag(name = "Citizen Authentication", description = "Endpoints for citizen authentication and account management")
interface CitizenAuthController {

    /**
     * Authenticates a citizen and provides JWT access tokens.
     *
     * @param request Login credentials (email and password)
     * @return JWT tokens and citizen information
     */
    @Operation(
        summary = "Authenticate citizen",
        description = "Authenticate citizen credentials and receive access token"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "Authentication successful"),
        SwaggerResponse(responseCode = "400", description = "Invalid credentials"),
        SwaggerResponse(responseCode = "403", description = "Account not approved or rejected"),
        SwaggerResponse(responseCode = "429", description = "Too many attempts")
    ])
    @Public
    @PostMapping("/login", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun login(
        @Valid @RequestBody request: CitizenLoginRequest
    ): ResponseEntity<ApiResponse<CitizenAuthResponse>>

    /**
     * Refreshes authentication token using a valid refresh token.
     *
     * @param refreshToken Refresh token provided in the request header
     * @return New JWT token pair
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
    ): ResponseEntity<ApiResponse<CitizenAuthResponse>>

    /**
     * Logs out the authenticated citizen by invalidating the current token.
     *
     * @param token Bearer token provided in the request header
     * @return Confirmation of logout
     */
    @Operation(
        summary = "Logout citizen",
        description = "Invalidate current authentication token"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "Logged out successfully"),
        SwaggerResponse(responseCode = "401", description = "Invalid token")
    ])
    @PostMapping("/logout")
    fun logout(
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<ApiResponse<Nothing>>

    /**
     * Requests a password reset OTP to be sent via email.
     *
     * @param request Email address for password reset
     * @return Confirmation of OTP sent
     */
    @Operation(
        summary = "Request password reset",
        description = "Request a password reset OTP that will be sent via email"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "Password reset OTP sent"),
        SwaggerResponse(responseCode = "429", description = "Too many reset attempts")
    ])
    @Public
    @PostMapping("/password-reset/request")
    fun requestPasswordReset(
        @Valid @RequestBody request: CitizenRequestPasswordResetRequest
    ): ResponseEntity<ApiResponse<Nothing>>

    /**
     * Resets password using the OTP received via email.
     *
     * @param request OTP and new password details
     * @return Confirmation of password reset
     */
    @Operation(
        summary = "Reset password using OTP",
        description = "Reset password using the OTP received via email"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "Password reset successful"),
        SwaggerResponse(responseCode = "400", description = "Invalid OTP or password requirements not met"),
        SwaggerResponse(responseCode = "404", description = "Citizen not found"),
        SwaggerResponse(responseCode = "429", description = "Too many attempts")
    ])
    @Public
    @PostMapping("/password-reset/reset")
    fun resetPassword(
        @Valid @RequestBody request: CitizenResetPasswordRequest
    ): ResponseEntity<ApiResponse<Nothing>>

    /**
     * Changes password for an authenticated citizen.
     *
     * @param citizenId ID of the authenticated citizen
     * @param request Password change details
     * @return Confirmation of password change
     */
    @Operation(
        summary = "Change password",
        description = "Change password for authenticated citizen"
    )
    @ApiResponses(value = [
        SwaggerResponse(responseCode = "200", description = "Password changed successfully"),
        SwaggerResponse(responseCode = "400", description = "Invalid current password or password requirements not met"),
        SwaggerResponse(responseCode = "401", description = "Not authenticated")
    ])
    @PostMapping("/change-password")
    fun changePassword(
        @Parameter(hidden = true) citizenId: UUID,
        @Valid @RequestBody request: CitizenChangePasswordRequest
    ): ResponseEntity<ApiResponse<Nothing>>
}
