package np.sthaniya.dpis.citizen.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import java.time.LocalDateTime

/**
 * Entity for storing one-time passwords used for citizen password resets.
 *
 * The OTP is stored with its expiration time and usage status to enforce
 * security policies for password reset operations.
 *
 * @property email Email address of the citizen
 * @property otp The one-time password generated for reset
 * @property expiresAt Timestamp when the OTP expires
 * @property isUsed Whether the OTP has already been used
 * @property attempts Number of times someone has attempted to use this OTP
 */
@Entity
@Table(name = "citizen_password_reset_otps")
class CitizenPasswordResetOtp : UuidBaseEntity() {
    
    @Column(nullable = false)
    var email: String? = null
    
    @Column(nullable = false)
    var otp: String? = null
    
    @Column(name = "expires_at", nullable = false)
    var expiresAt: LocalDateTime? = null
    
    @Column(name = "is_used", nullable = false)
    var isUsed: Boolean = false
    
    @Column(nullable = false)
    var attempts: Int = 0
    
    /**
     * Checks if this OTP is valid for use:
     * - Not expired
     * - Not already used
     * - Less than 3 failed attempts
     *
     * @return true if the OTP is valid for use
     */
    fun isValid(): Boolean {
        val now = LocalDateTime.now()
        return expiresAt?.isAfter(now) ?: false && !isUsed && attempts < 3
    }
}
