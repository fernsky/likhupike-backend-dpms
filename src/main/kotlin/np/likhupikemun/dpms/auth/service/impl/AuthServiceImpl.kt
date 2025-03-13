package np.likhupikemun.dpms.auth.service.impl

import np.likhupikemun.dpms.auth.service.AuthService
import np.likhupikemun.dpms.auth.service.UserService
import np.likhupikemun.dpms.auth.dto.*
import np.likhupikemun.dpms.auth.exception.AuthException
import np.likhupikemun.dpms.auth.security.JwtService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import org.slf4j.LoggerFactory
import java.util.UUID
import jakarta.annotation.PostConstruct

@Service
@Transactional
class AuthServiceImpl(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager
) : AuthService {
    private val logger = LoggerFactory.getLogger(javaClass)
    private lateinit var resetTokenCache: Cache<String, String>

    @PostConstruct
    fun init() {
        resetTokenCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(15))
            .maximumSize(1000)
            .build()
    }

    override fun register(request: LoginRequest): AuthResponse {
        val user = userService.createUser(
            CreateUserDto(
                email = request.email,
                password = request.password,
                isWardLevelUser = false
            )
        )

        val tokenPair = jwtService.generateTokenPair(user)
        logger.info("User registered successfully: {}", request.email)

        return AuthResponse(
            token = tokenPair.accessToken,
            refreshToken = tokenPair.refreshToken,
            userId = user.id!!.toString(),
            email = user.email!!,
            permissions = user.getAuthorities().map { it.authority }.toSet(),
            expiresIn = jwtService.getExpirationDuration().toSeconds(),
            isWardLevelUser = user.isWardLevelUser,
            wardNumber = user.wardNumber
        )
    }

    override fun login(request: LoginRequest): AuthResponse {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.email, request.password)
            )
        } catch (e: Exception) {
            throw AuthException.InvalidCredentialsException()
        }

        val user = userService.findByEmail(request.email)
            ?: throw AuthException.UserNotFoundException(request.email)

        if (!user.isApproved) {
            throw AuthException.UserNotApprovedException()
        }

        val tokenPair = jwtService.generateTokenPair(user)
        logger.info("User logged in successfully: {}", request.email)

        return AuthResponse(
            token = tokenPair.accessToken,
            refreshToken = tokenPair.refreshToken,
            userId = user.id!!.toString(),
            email = user.email!!,
            permissions = user.getAuthorities().map { it.authority }.toSet(),
            expiresIn = jwtService.getExpirationDuration().toSeconds(),
            isWardLevelUser = user.isWardLevelUser,
            wardNumber = user.wardNumber
        )
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

        return AuthResponse(
            token = tokenPair.accessToken,
            refreshToken = tokenPair.refreshToken,
            userId = user.id!!.toString(),
            email = user.email!!,
            permissions = user.getAuthorities().map { it.authority }.toSet(),
            expiresIn = jwtService.getExpirationDuration().toSeconds(),
            isWardLevelUser = user.isWardLevelUser,
            wardNumber = user.wardNumber
        )
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

        val email = resetTokenCache.getIfPresent(request.token)
            ?: throw AuthException.InvalidPasswordResetTokenException()

        val user = userService.findByEmail(email)
            ?: throw AuthException.UserNotFoundException(email)

        userService.resetPassword(user.id!!, request.newPassword)
        resetTokenCache.invalidate(request.token)
        logger.info("Password reset successful for user: {}", email)
    }

    override fun requestPasswordReset(request: RequestPasswordResetRequest) {
        val user = userService.findByEmail(request.email)
            ?: throw AuthException.UserNotFoundException(request.email)

        val token = UUID.randomUUID().toString()
        resetTokenCache.put(token, user.email!!)
        logger.info("Password reset requested for user: {}", request.email)

        // Here you would typically send an email with the reset token
        // For now, we'll just log it
        logger.info("Reset token for {}: {}", request.email, token)
    }

    private fun createAuthResponse(user: User, tokenPair: TokenPair) = AuthResponse(
        token = tokenPair.accessToken,
        refreshToken = tokenPair.refreshToken,
        userId = user.id!!.toString(),
        email = user.email!!,
        permissions = user.getAuthorities().map { it.authority }.toSet(),
        expiresIn = jwtService.getExpirationDuration().toSeconds(),
        isWardLevelUser = user.isWardLevelUser,
        wardNumber = user.wardNumber
    )
}
