package np.sthaniya.dpis.citizen.api.controller.impl

import jakarta.validation.Valid
import np.sthaniya.dpis.citizen.api.controller.CitizenAuthController
import np.sthaniya.dpis.citizen.dto.auth.*
import np.sthaniya.dpis.citizen.resolver.CurrentCitizenId
import np.sthaniya.dpis.citizen.service.CitizenAuthService
import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

/**
 * Implementation of CitizenAuthController providing citizen authentication functionality.
 *
 * This controller handles the authentication API for citizens, including:
 * - Login and token issuance
 * - Token refresh
 * - Logout
 * - Password reset workflows
 * - Password changes
 *
 * @property citizenAuthService Service handling core citizen authentication logic
 * @property i18nMessageService Service for internationalized message handling
 */
@RestController
@RequestMapping("/api/v1/citizen-auth", produces = [MediaType.APPLICATION_JSON_VALUE])
class CitizenAuthControllerImpl(
    private val citizenAuthService: CitizenAuthService,
    private val i18nMessageService: I18nMessageService
) : CitizenAuthController {
    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Authenticates a citizen and provides JWT access tokens.
     *
     * @param request Login credentials (email and password)
     * @return JWT tokens and citizen information
     */
    @PostMapping("/login", consumes = [MediaType.APPLICATION_JSON_VALUE])
    override fun login(
        @Valid @RequestBody request: CitizenLoginRequest
    ): ResponseEntity<ApiResponse<CitizenAuthResponse>> {
        logger.debug("Processing login request for citizen email: {}", request.email)
        val response = citizenAuthService.login(request)
        return ResponseEntity.ok(
            ApiResponse.success(
                data = response,
                message = i18nMessageService.getMessage("citizen.auth.login.success")
            )
        )
    }

    /**
     * Refreshes authentication token using a valid refresh token.
     *
     * @param refreshToken Refresh token provided in the request header
     * @return New JWT token pair
     */
    @PostMapping("/refresh")
    override fun refreshToken(
        @RequestHeader("X-Refresh-Token") refreshToken: String
    ): ResponseEntity<ApiResponse<CitizenAuthResponse>> {
        logger.debug("Processing token refresh request for citizen")
        val response = citizenAuthService.refreshToken(refreshToken)
        return ResponseEntity.ok(
            ApiResponse.success(
                data = response,
                message = i18nMessageService.getMessage("citizen.auth.token.refresh.success")
            )
        )
    }

    /**
     * Logs out the authenticated citizen by invalidating the current token.
     *
     * @param token Bearer token provided in the request header
     * @return Confirmation of logout
     */
    @PostMapping("/logout")
    override fun logout(
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<ApiResponse<Nothing>> {
        logger.debug("Processing logout request for citizen")
        val cleanToken = token.substringAfter("Bearer ").trim()
        citizenAuthService.logout(cleanToken)
        return ResponseEntity.ok(
            ApiResponse.success(
                message = i18nMessageService.getMessage("citizen.auth.logout.success")
            )
        )
    }

    /**
     * Requests a password reset OTP to be sent via email.
     *
     * @param request Email address for password reset
     * @return Confirmation of OTP sent
     */
    @PostMapping("/password-reset/request")
    override fun requestPasswordReset(
        @Valid @RequestBody request: CitizenRequestPasswordResetRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        logger.debug("Processing password reset request for citizen email: {}", request.email)
        citizenAuthService.requestPasswordReset(request)
        return ResponseEntity.ok(
            ApiResponse.success(
                message = i18nMessageService.getMessage("citizen.auth.password.reset.request.success")
            )
        )
    }

    /**
     * Resets password using the OTP received via email.
     *
     * @param request OTP and new password details
     * @return Confirmation of password reset
     */
    @PostMapping("/password-reset/reset")
    override fun resetPassword(
        @Valid @RequestBody request: CitizenResetPasswordRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        logger.debug("Processing password reset for citizen")
        citizenAuthService.resetPassword(request)
        return ResponseEntity.ok(
            ApiResponse.success(
                message = i18nMessageService.getMessage("citizen.auth.password.reset.success")
            )
        )
    }

    /**
     * Changes password for an authenticated citizen.
     *
     * @param citizenId ID of the authenticated citizen
     * @param request Password change details
     * @return Confirmation of password change
     */
    @PostMapping("/change-password")
    override fun changePassword(
        @CurrentCitizenId citizenId: UUID,
        @Valid @RequestBody request: CitizenChangePasswordRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        logger.debug("Processing change password request for citizen: {}", citizenId)
        citizenAuthService.changePassword(citizenId, request)
        return ResponseEntity.ok(
            ApiResponse.success(
                message = i18nMessageService.getMessage("citizen.auth.password.change.success")
            )
        )
    }
}
