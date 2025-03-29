package np.sthaniya.dpis.auth.domain.entity

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import java.time.LocalDateTime

/**
 * Represents a one-time password (OTP) entity for password reset functionality.
 *
 * This entity manages the lifecycle of password reset tokens, including:
 * - OTP generation and storage
 * - Expiration tracking
 * - Usage status
 * - Attempt limiting for security
 *
 * Features:
 * - 6-digit OTP
 * - 15-minute expiration window
 * - Maximum 3 verification attempts
 * - Single-use validation
 * - Inherits UUID-based identification from [UuidBaseEntity]
 *
 * Usage:
 * ```kotlin
 * val otp = PasswordResetOtp().apply {
 *     email = "user@example.com"
 *     otp = "123456"
 * }
 * if (otp.isValid()) {
 *     // Process password reset
 * }
 * ```
 *
 * @property email The email address associated with the password reset request
 * @property otp The 6-digit one-time password
 * @property expiresAt Timestamp when the OTP expires (15 minutes from creation)
 * @property isUsed Flag indicating if the OTP has been used
 * @property attempts Number of verification attempts made with this OTP
 */
@Entity
@Table(name = "password_reset_otps")
class PasswordResetOtp : UuidBaseEntity() {
    
    @Column(nullable = false)
    var email: String? = null

    @Column(nullable = false, length = 6)
    var otp: String? = null

    @Column(nullable = false)
    var expiresAt: LocalDateTime = LocalDateTime.now().plusMinutes(15)

    @Column(nullable = false)
    var isUsed: Boolean = false

    @Column(nullable = false)
    var attempts: Int = 0

    /**
     * Checks if the OTP is valid for use.
     *
     * An OTP is considered valid if all the following conditions are met:
     * 1. It has not been used previously
     * 2. Current time is before expiration time
     * 3. Number of verification attempts is less than 3
     *
     * @return true if the OTP is valid and can be used, false otherwise
     */
    fun isValid(): Boolean =
        !isUsed && LocalDateTime.now().isBefore(expiresAt) && attempts < 3
}
