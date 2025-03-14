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
import org.springframework.transaction.annotation.Transactional
import org.slf4j.LoggerFactory
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import np.likhupikemun.dpms.auth.domain.enums.PermissionType

@Service
@Transactional
class AuthServiceImpl(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager
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

        logger.info("User registered successfully and waiting for approval: {}", request.email)

        return RegisterResponse(email = user.email!!)
    }

    override fun login(request: LoginRequest): AuthResponse {
        try {
            // Try authentication first
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.email, request.password)
            )

            // If authentication succeeds, then get the user
            val user = userService.findByEmail(request.email)
                ?: throw AuthException.UserNotFoundException(request.email)

            if (!user.isApproved) {
                throw AuthException.UserNotApprovedException()
            }

            val tokenPair = jwtService.generateTokenPair(user)
            logger.info("User logged in successfully: {}", request.email)

            return createAuthResponseFromUser(user, tokenPair)

        } catch (e: Exception) {
            logger.error("Authentication failed for user: ${request.email}", e)
            when (e) {
                is AuthException -> throw e
                else -> throw AuthException.InvalidCredentialsException()
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
        if (!request.isValid()) {
            throw AuthException.InvalidPasswordException("Passwords do not match")
        }

        val tokenInfo = resetTokens[request.token] ?: 
            throw AuthException.InvalidPasswordResetTokenException()

        if (System.currentTimeMillis() > tokenInfo.second) {
            resetTokens.remove(request.token)
            throw AuthException.InvalidPasswordResetTokenException("Token has expired")
        }

        val user = userService.findByEmail(tokenInfo.first)
            ?: throw AuthException.UserNotFoundException(tokenInfo.first)

        userService.resetPassword(user.id!!, request.newPassword)
        resetTokens.remove(request.token)
        logger.info("Password reset successful for user: {}", tokenInfo.first)
    }

    override fun requestPasswordReset(request: RequestPasswordResetRequest) {
        val user = userService.findByEmail(request.email)
            ?: throw AuthException.UserNotFoundException(request.email)

        val token = UUID.randomUUID().toString()
        val expiryTime = System.currentTimeMillis() + RESET_TOKEN_VALIDITY
        resetTokens[token] = user.email!! to expiryTime
        
        // Cleanup expired tokens
        resetTokens.entries.removeIf { it.value.second < System.currentTimeMillis() }

        logger.info("Password reset requested for user: {}", request.email)
        logger.info("Reset token for {}: {}", request.email, token)
    }
}
