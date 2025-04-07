package np.sthaniya.dpis.citizen.service.impl

import np.sthaniya.dpis.citizen.domain.entity.Citizen
import np.sthaniya.dpis.citizen.domain.entity.CitizenPasswordResetOtp
import np.sthaniya.dpis.citizen.domain.entity.CitizenState
import np.sthaniya.dpis.citizen.dto.auth.*
import np.sthaniya.dpis.citizen.exception.CitizenAuthException
import np.sthaniya.dpis.citizen.repository.CitizenPasswordResetOtpRepository
import np.sthaniya.dpis.citizen.security.CitizenJwtService
import np.sthaniya.dpis.citizen.service.CitizenAuthService
import np.sthaniya.dpis.citizen.service.CitizenService
import np.sthaniya.dpis.common.service.EmailService
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Qualifier
import java.time.LocalDateTime
import java.util.UUID

/**
 * Implementation of [CitizenAuthService] that provides authentication and citizen management functionality.
 *
 * This service handles citizen authentication, password management, and token operations
 * while maintaining security best practices and proper error handling.
 */
@Service
@Transactional
class CitizenAuthServiceImpl(
    private val citizenService: CitizenService,
    private val passwordEncoder: PasswordEncoder,
    private val citizenJwtService: CitizenJwtService,
    private val emailService: EmailService,
    private val otpRepository: CitizenPasswordResetOtpRepository
) : CitizenAuthService {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates an [CitizenAuthResponse] from a citizen and token pair.
     *
     * @param citizen The authenticated citizen
     * @param tokenPair The generated access and refresh tokens
     * @return [CitizenAuthResponse] containing citizen details and tokens
     */
    private fun createAuthResponseFromCitizen(
        citizen: Citizen, 
        accessToken: String,
        refreshToken: String
    ): CitizenAuthResponse {
        return CitizenAuthResponse(
            token = accessToken,
            refreshToken = refreshToken,
            citizenId = citizen.id!!.toString(),
            email = citizen.email!!,
            expiresIn = citizenJwtService.getExpirationDuration().toSeconds(),
            state = citizen.state
        )
    }

    /**
     * Authenticates a citizen and generates an authentication response.
     *
     * @param request The login request containing citizen credentials
     * @return [CitizenAuthResponse] containing citizen details and tokens
     */
    override fun login(request: CitizenLoginRequest): CitizenAuthResponse {
        // Find citizen but don't expose its existence yet
        val citizen = citizenService.findCitizenByEmail(request.email)
            ?: throw CitizenAuthException.InvalidCredentialsException()

        try {
            // Direct password validation
            // Doing from authentication manager conflicts with the user level 
            if (!passwordEncoder.matches(request.password, citizen.password)) {
                throw CitizenAuthException.InvalidCredentialsException()
            }

            // Check citizen state after successful password validation
            when (citizen.state) {
                CitizenState.REJECTED -> {
                    val message = citizen.stateNote ?: "Your citizen profile has been rejected."
                    throw CitizenAuthException.CitizenAccountRejectedException(message)
                }
                CitizenState.PENDING_REGISTRATION,
                CitizenState.ACTION_REQUIRED,
                CitizenState.APPROVED,
                CitizenState.UNDER_REVIEW -> {
                    // Continue with authentication
                }
            }

            if (citizen.isDeleted) {
                throw CitizenAuthException.CitizenAccountDisabledException()
            }

            val tokenPair = citizenJwtService.generateTokenPair(citizen)
            logger.debug("Citizen logged in successfully: {}", request.email)
            
            return createAuthResponseFromCitizen(
                citizen, 
                tokenPair.accessToken,
                tokenPair.refreshToken
            )

        } catch (e: Exception) {
            when (e) {
                is BadCredentialsException -> {
                    logger.debug("Invalid credentials for citizen")
                    throw CitizenAuthException.InvalidCredentialsException()
                }
                is DisabledException -> {
                    logger.debug("Disabled citizen account")
                    throw CitizenAuthException.CitizenAccountDisabledException()
                }
                is CitizenAuthException -> throw e
                else -> {
                    logger.error("Authentication error", e)
                    throw CitizenAuthException.InvalidCredentialsException()
                }
            }
        }
    }

    /**
     * Refreshes an expired token and generates a new authentication response.
     *
     * @param refreshToken The refresh token to validate and use
     * @return [CitizenAuthResponse] containing citizen details and new tokens
     */
    override fun refreshToken(refreshToken: String): CitizenAuthResponse {
        if (!citizenJwtService.validateToken(refreshToken)) {
            throw CitizenAuthException.InvalidTokenException()
        }

        val email = citizenJwtService.extractEmail(refreshToken)
            ?: throw CitizenAuthException.InvalidTokenException()

        val citizen = citizenService.findCitizenByEmail(email)
            ?: throw CitizenAuthException.CitizenNotFoundException(email)

        val tokenPair = citizenJwtService.generateTokenPair(citizen)
        logger.info("Token refreshed for citizen: {}", email)

        return createAuthResponseFromCitizen(
            citizen, 
            tokenPair.accessToken,
            tokenPair.refreshToken
        )
    }

    /**
     * Logs out a citizen by invalidating their token.
     *
     * @param token The token to invalidate
     */
    override fun logout(token: String) {
        citizenJwtService.invalidateToken(token)
        val email = citizenJwtService.extractEmail(token)
        logger.info("Citizen logged out: {}", email)
    }

    /**
     * Generates a random 6-digit OTP for password reset.
     *
     * @return String containing the generated OTP
     */
    private fun generateOtp(): String =
        (100000..999999).random().toString()

    /**
     * Requests a password reset by generating and sending an OTP to the citizen's email.
     *
     * @param request The request containing the citizen's email
     */
    override fun requestPasswordReset(request: CitizenRequestPasswordResetRequest) {
        val citizen = citizenService.findCitizenByEmail(request.email) ?: run {
            logger.info("Password reset requested for non-existing citizen: {}", request.email)
            return
        }

        // Generate new OTP
        val otp = generateOtp()
        
        // Save OTP to database
        val passwordResetOtp = CitizenPasswordResetOtp().apply {
            email = request.email
            this.otp = otp
            expiresAt = LocalDateTime.now().plusMinutes(15)
        }
        otpRepository.save(passwordResetOtp)
        
        try {
            // Use the generic email service for now, you might want to create citizen-specific templates later
            emailService.sendPasswordResetOtpAsync(request.email, otp)
            logger.info("Password reset OTP sent to citizen: {}", request.email)
        } catch (e: Exception) {
            logger.error("Failed to send password reset OTP to: {}", request.email, e)
            throw RuntimeException("Failed to send password reset OTP", e)
        }
    }

    /**
     * Resets a citizen's password using a valid OTP.
     *
     * @param request The request containing the citizen's email, OTP, and new password
     */
    override fun resetPassword(request: CitizenResetPasswordRequest) {
        // Check if passwords match
        if (!request.passwordsMatch()) {
            throw CitizenAuthException.PasswordsDoNotMatchException()
        }

        // Validate OTP
        val otpEntity = otpRepository.findByEmailAndOtpAndIsUsedFalseAndExpiresAtAfter(
            email = request.email,
            otp = request.otp,
            currentTime = LocalDateTime.now()
        ) ?: throw CitizenAuthException.InvalidPasswordResetTokenException("Invalid or expired OTP")

        // Check attempts - throw specific exception
        if (otpEntity.attempts >= 3) {
            throw CitizenAuthException.TooManyAttemptsException()
        }

        // Increment attempts
        otpEntity.attempts += 1

        if (!otpEntity.isValid()) {
            otpRepository.save(otpEntity)
            throw CitizenAuthException.InvalidPasswordResetTokenException("Invalid or expired OTP")
        }

        val citizen = citizenService.findCitizenByEmail(request.email)
            ?: throw CitizenAuthException.CitizenNotFoundException(request.email)

        // Mark OTP as used
        otpEntity.isUsed = true
        otpRepository.save(otpEntity)

        // Reset password
        citizenService.resetPassword(citizen.id!!, request.newPassword)
        logger.info("Password reset successful for citizen: {}", request.email)

        try {
            emailService.sendPasswordResetConfirmationAsync(request.email)
        } catch (e: Exception) {
            logger.error("Failed to send password reset confirmation email to: {}", request.email, e)
            // Don't throw since password reset was successful
        }
    }

    /**
     * Changes a citizen's password after verifying the current password.
     *
     * @param citizenId The ID of the current citizen
     * @param request The request containing the current and new passwords
     */
    override fun changePassword(citizenId: UUID, request: CitizenChangePasswordRequest) {
        if (!request.isValid()) {
            throw CitizenAuthException.PasswordsDoNotMatchException()
        }

        val citizen = citizenService.getCitizenById(citizenId)

        // Verify current password
        if (!passwordEncoder.matches(request.currentPassword, citizen.password)) {
            throw CitizenAuthException.InvalidPasswordException("Current password is incorrect")
        }

        // Ensure new password is different from current
        if (passwordEncoder.matches(request.newPassword, citizen.password)) {
            throw CitizenAuthException.InvalidPasswordException("New password must be different from current password")
        }

        // Update password
        citizenService.resetPassword(citizenId, request.newPassword)
        
        try {
            emailService.sendPasswordResetConfirmationAsync(citizen.email!!)
        } catch (e: Exception) {
            logger.error("Failed to send password change confirmation email to: {}", citizen.email, e)
            // Don't throw since password change was successful
        }

        logger.info("Password changed successfully for citizen: {}", citizenId)
    }
}
