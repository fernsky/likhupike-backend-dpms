package np.likhupikemun.dpms.auth.service.impl

import np.likhupikemun.dpms.auth.domain.entity.User
import np.likhupikemun.dpms.auth.service.AuthService
import np.likhupikemun.dpms.auth.service.UserService
import np.likhupikemun.dpms.auth.dto.*
import np.likhupikemun.dpms.auth.exception.AuthException
import np.likhupikemun.dpms.auth.security.JwtService
import np.likhupikemun.dpms.auth.security.TokenPair
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
import np.likhupikemun.dpms.auth.domain.enums.PermissionType
import np.likhupikemun.dpms.common.service.EmailService

@Service
@Transactional
class AuthServiceImpl(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
    private val emailService: EmailService  // Add email service
) : AuthService {
    private val logger = LoggerFactory.getLogger(javaClass)
    
    // Simple in-memory store for reset tokens with 15-minute expiry
    private val resetTokens = ConcurrentHashMap<String, Pair<String, Long>>()
    private val RESET_TOKEN_VALIDITY = 15 * 60 * 1000L // 15 minutes in milliseconds

    private fun createAuthResponseFromUser(user: User, tokenPair: TokenPair): AuthResponse {
        val permissions = user.getAuthorities()
            .mapNotNull { authority -> 
                runCatching { 
                    PermissionType.valueOf(authority.authority.removePrefix("PERMISSION_"))
                }.getOrNull()
            }
            .toSet()

        return AuthResponse(
            token = tokenPair.accessToken,
            refreshToken = tokenPair.refreshToken,
            userId = user.id!!.toString(),
            email = user.email!!,
            permissions = permissions,
            expiresIn = jwtService.getExpirationDuration().toSeconds(),
            isWardLevelUser = user.isWardLevelUser,
            wardNumber = user.wardNumber
        )
    }

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
            emailService.sendWelcomeEmail(user.email!!)
            logger.info("Welcome email sent to: {}", user.email)
        } catch (e: Exception) {
            logger.error("Failed to send welcome email to: {}", user.email, e)
            // Don't throw since registration was successful
        }

        return RegisterResponse(email = user.email!!)
    }

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

    override fun logout(token: String) {
        jwtService.invalidateToken(token)
        val email = jwtService.extractEmail(token)
        logger.info("User logged out: {}", email)
    }

    override fun resetPassword(request: ResetPasswordRequest) {
        // Check if passwords match first
        if (request.newPassword != request.confirmPassword) {
            throw AuthException.PasswordsDoNotMatchException()
        }

        if (!jwtService.validateToken(request.token)) {
            throw AuthException.InvalidPasswordResetTokenException()
        }

        val email = jwtService.extractEmail(request.token)
        val user = userService.findByEmail(email)
            ?: throw AuthException.UserNotFoundException(email)

        userService.resetPassword(user.id!!, request.newPassword)
        logger.info("Password reset successful for user: {}", email)

        try {
            // Send confirmation email
            emailService.sendEmail(
                to = email,
                subject = "Password Reset Successful",
                htmlContent = """
                    <p>Your password has been successfully reset.</p>
                    <p>If you did not perform this action, please contact support immediately.</p>
                """.trimIndent()
            )
        } catch (e: Exception) {
            logger.error("Failed to send password reset confirmation email to: {}", email, e)
            // Don't throw here since password reset was successful
        }
    }

    override fun requestPasswordReset(request: RequestPasswordResetRequest) {
        val user = userService.findByEmail(request.email)
            ?: throw AuthException.UserNotFoundException(request.email)

        // Generate reset token
        val token = jwtService.generateToken(user)
        
        try {
            emailService.sendPasswordResetEmail(request.email, token)
            logger.info("Password reset email sent to user: {}", request.email)
        } catch (e: Exception) {
            logger.error("Failed to send password reset email to: {}", request.email, e)
            throw RuntimeException("Failed to send password reset email", e)
        }
    }
}
