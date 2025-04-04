package np.sthaniya.dpis.auth.service.impl

import np.sthaniya.dpis.auth.domain.entity.User
import np.sthaniya.dpis.auth.service.AuthService
import np.sthaniya.dpis.auth.service.UserService
import np.sthaniya.dpis.auth.dto.*
import np.sthaniya.dpis.auth.exception.AuthException
import np.sthaniya.dpis.auth.security.JwtService
import np.sthaniya.dpis.auth.security.TokenPair
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.authentication.DisabledException
import org.springframework.transaction.annotation.Transactional
import org.slf4j.LoggerFactory
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import np.sthaniya.dpis.auth.domain.enums.PermissionType
import np.sthaniya.dpis.common.service.EmailService
import np.sthaniya.dpis.auth.repository.PasswordResetOtpRepository
import np.sthaniya.dpis.auth.domain.entity.PasswordResetOtp
import java.time.LocalDateTime

/**
 * Implementation of [AuthService] that provides authentication and user management functionality.
 *
 * This service handles user registration, authentication, password management, and token operations
 * while maintaining security best practices and proper error handling.
 *
 * @property userService Service for user-related operations
 * @property passwordEncoder Encoder for secure password handling
 * @property jwtService Service for JWT token operations
 * @property authenticationManager Spring Security authentication manager
 * @property emailService Service for sending email notifications
 * @property otpRepository Repository for password reset OTP operations
 */
@Service
@Transactional
class AuthServiceImpl(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
    private val emailService: EmailService,
    private val otpRepository: PasswordResetOtpRepository
) : AuthService {
    private val logger = LoggerFactory.getLogger(javaClass)
    
    // Simple in-memory store for reset tokens with 15-minute expiry
    private val resetTokens = ConcurrentHashMap<String, Pair<String, Long>>()
    private val RESET_TOKEN_VALIDITY = 15 * 60 * 1000L // 15 minutes in milliseconds

    /**
     * Creates an [AuthResponse] from a user and token pair.
     *
     * @param user The authenticated user
     * @param tokenPair The generated access and refresh tokens
     * @return [AuthResponse] containing user details and tokens
     */
    private fun createAuthResponseFromUser(user: User, tokenPair: TokenPair): AuthResponse {
        val permissions = user.getAuthorities()
            .mapNotNull { authority -> 
                runCatching { 
                    PermissionType.valueOf(authority.authority.removePrefix("PERMISSION_"))
                }.getOrNull()
            }
            .toSet()

        val roles = user.getRoles()
            .mapNotNull { role -> 
                runCatching { 
                    role.type
                }.getOrNull()
            }
            .toSet()
           

        return AuthResponse(
            token = tokenPair.accessToken,
            refreshToken = tokenPair.refreshToken,
            userId = user.id!!.toString(),
            email = user.email!!,
            permissions = permissions,
            roles = roles,
            expiresIn = jwtService.getExpirationDuration().toSeconds(),
            isWardLevelUser = user.isWardLevelUser,
            wardNumber = user.wardNumber
        )
    }

    /**
     * Registers a new user in the system.
     *
     * @param request The registration request containing user details
     * @return [RegisterResponse] containing the registered user's email
     */
    override fun register(request: RegisterRequest): RegisterResponse {
        val user = userService.createUser(
            CreateUserDto(
                email = request.email,
                password = request.password,
                isWardLevelUser = request.isWardLevelUser,
                wardNumber = request.wardNumber
            )
        )

        try {
            // Send welcome email using template
            emailService.sendWelcomeEmailAsync(user.email!!)
            logger.info("Welcome email sent to: {}", user.email)
        } catch (e: Exception) {
            logger.error("Failed to send welcome email to: {}", user.email, e)
            // Don't throw since registration was successful
        }

        return RegisterResponse(email = user.email!!)
    }

    /**
     * Authenticates a user and generates an authentication response.
     *
     * @param request The login request containing user credentials
     * @return [AuthResponse] containing user details and tokens
     */
    override fun login(request: LoginRequest): AuthResponse {
        // First find user but don't expose its existence
        val user = userService.findByEmail(request.email)
            ?: throw AuthException.InvalidCredentialsException()

        try {
            // Attempt password validation first without revealing user status
            val auth = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.email, request.password)
            )

            // If credentials are valid, then check approval status
            if (!auth.isAuthenticated) {
                throw AuthException.InvalidCredentialsException()
            }

            // Only check approval after successful password validation
            if (!user.isApproved) {
                throw AuthException.UserNotApprovedException()
            }

            val tokenPair = jwtService.generateTokenPair(user)
            logger.debug("User logged in successfully: {}", request.email)
            return createAuthResponseFromUser(user, tokenPair)

        } catch (e: Exception) {
            when (e) {
                is BadCredentialsException,
                is InternalAuthenticationServiceException -> {
                    logger.debug("Invalid credentials for user")
                    throw AuthException.InvalidCredentialsException()
                }
                is DisabledException -> {
                    logger.debug("User not approved")
                    throw AuthException.UserNotApprovedException()
                }
                is AuthException -> throw e
                else -> {
                    logger.error("Authentication error", e)
                    throw AuthException.InvalidCredentialsException()
                }
            }
        }
    }

    /**
     * Refreshes an expired token and generates a new authentication response.
     *
     * @param refreshToken The refresh token to validate and use
     * @return [AuthResponse] containing user details and new tokens
     */
    override fun refreshToken(refreshToken: String): AuthResponse {
        if (!jwtService.validateToken(refreshToken)) {
            throw AuthException.InvalidTokenException()
        }

        val email = jwtService.extractEmail(refreshToken)
            ?: throw AuthException.InvalidTokenException()

        val user = userService.findByEmail(email)
            ?: throw AuthException.UserNotFoundException(email)

        val tokenPair = jwtService.generateTokenPair(user)
        logger.info("Token refreshed for user: {}", email)

        return createAuthResponseFromUser(user, tokenPair)
    }

    /**
     * Logs out a user by invalidating their token.
     *
     * @param token The token to invalidate
     */
    override fun logout(token: String) {
        jwtService.invalidateToken(token)
        val email = jwtService.extractEmail(token)
        logger.info("User logged out: {}", email)
    }

    /**
     * Generates a random 6-digit OTP for password reset.
     *
     * @return String containing the generated OTP
     */
    private fun generateOtp(): String =
        (100000..999999).random().toString()

    /**
     * Requests a password reset by generating and sending an OTP to the user's email.
     *
     * @param request The request containing the user's email
     */
    override fun requestPasswordReset(request: RequestPasswordResetRequest) {
        val user = userService.findByEmail(request.email) ?: run {
            logger.info("Password reset requested for non-existing user: {}", request.email)
            return
        }

        // Generate new OTP
        val otp = generateOtp()
        
        // Save OTP to database
        val passwordResetOtp = PasswordResetOtp().apply {
            email = request.email
            this.otp = otp
            expiresAt = LocalDateTime.now().plusMinutes(15)
        }
        otpRepository.save(passwordResetOtp)
        
        try {
            emailService.sendPasswordResetOtpAsync(request.email, otp)
            logger.info("Password reset OTP sent to user: {}", request.email)
        } catch (e: Exception) {
            logger.error("Failed to send password reset OTP to: {}", request.email, e)
            throw RuntimeException("Failed to send password reset OTP", e)
        }
    }

    /**
     * Resets a user's password using a valid OTP.
     *
     * @param request The request containing the user's email, OTP, and new password
     */
    override fun resetPassword(request: ResetPasswordRequest) {
        // Check if passwords match
        if (request.newPassword != request.confirmPassword) {
            throw AuthException.PasswordsDoNotMatchException()
        }

        // Validate OTP
        val otpEntity = otpRepository.findByEmailAndOtpAndIsUsedFalseAndExpiresAtAfter(
            email = request.email,
            otp = request.otp,
            currentTime = LocalDateTime.now()
        ) ?: throw AuthException.InvalidPasswordResetTokenException("Invalid or expired OTP")

        // Check attempts - throw specific exception
        if (otpEntity.attempts >= 3) {
            throw AuthException.TooManyAttemptsException()
        }

        // Increment attempts
        otpEntity.attempts += 1

        if (!otpEntity.isValid()) {
            otpRepository.save(otpEntity)
            throw AuthException.InvalidPasswordResetTokenException("Invalid or expired OTP")
        }

        val user = userService.findByEmail(request.email)
            ?: throw AuthException.UserNotFoundException(request.email)

        // Mark OTP as used
        otpEntity.isUsed = true
        otpRepository.save(otpEntity)

        // Reset password
        userService.resetPassword(user.id!!, request.newPassword)
        logger.info("Password reset successful for user: {}", request.email)

        try {
            emailService.sendPasswordResetConfirmationAsync(request.email)
        } catch (e: Exception) {
            logger.error("Failed to send password reset confirmation email to: {}", request.email, e)
            // Don't throw since password reset was successful
        }
    }

    /**
     * Changes a user's password after verifying the current password.
     *
     * @param currentUserId The ID of the current user
     * @param request The request containing the current and new passwords
     */
    override fun changePassword(currentUserId: UUID, request: ChangePasswordRequest) {
        if (!request.isValid()) {
            throw AuthException.PasswordsDoNotMatchException()
        }

        val user = userService.getUserById(currentUserId)

        // Verify current password
        if (!passwordEncoder.matches(request.currentPassword, user.password)) {
            throw AuthException.InvalidPasswordException("Current password is incorrect")
        }

        // Ensure new password is different from current
        if (passwordEncoder.matches(request.newPassword, user.password)) {
            throw AuthException.InvalidPasswordException("New password must be different from current password")
        }

        // Update password
        userService.resetPassword(currentUserId, request.newPassword)
        
        try {
            emailService.sendPasswordResetConfirmationAsync(user.email!!)
        } catch (e: Exception) {
            logger.error("Failed to send password change confirmation email to: {}", user.email, e)
            // Don't throw since password change was successful
        }

        logger.info("Password changed successfully for user: {}", currentUserId)
    }
}
