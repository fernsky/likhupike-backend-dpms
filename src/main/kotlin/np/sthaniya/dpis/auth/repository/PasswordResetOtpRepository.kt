package np.sthaniya.dpis.auth.repository

import np.sthaniya.dpis.auth.domain.entity.PasswordResetOtp
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID
import java.time.LocalDateTime
/**
 * Repository interface for managing password reset OTP (One-Time Password) operations.
 * 
 * This repository handles:
 * - OTP validation and verification
 * - Expiration time management
 * - Email-based OTP lookups
 *
 * Security Features:
 * - Single-use OTP enforcement
 * - Time-based expiration
 * - Email verification
 *
 * Implementation Details:
 * - Uses UUID as primary key
 * - Supports expiration time checks
 * - Tracks OTP usage status
 */
@Repository
interface PasswordResetOtpRepository : JpaRepository<PasswordResetOtp, UUID> {
    
    /**
     * Finds a valid (unused and non-expired) OTP for a given email and OTP combination.
     *
     * This method validates:
     * 1. Email matches
     * 2. OTP matches
     * 3. OTP is not used
     * 4. OTP has not expired
     *
     * @param email The email address associated with the OTP
     * @param otp The OTP code to validate
     * @param currentTime The current time for expiration check
     * @return Valid PasswordResetOtp if found, null otherwise
     */
    fun findByEmailAndOtpAndIsUsedFalseAndExpiresAtAfter(
        email: String,
        otp: String,
        currentTime: LocalDateTime
    ): PasswordResetOtp?

    /**
     * Finds the most recent OTP request for a given email.
     *
     * Used for:
     * - Rate limiting OTP requests
     * - Checking existing valid OTPs
     * - Managing OTP request history
     *
     * @param email The email address to look up
     * @return Most recent PasswordResetOtp for the email, null if none exists
     */
    fun findLatestByEmail(email: String): PasswordResetOtp?
}
