package np.sthaniya.dpis.auth.repository

import np.sthaniya.dpis.auth.domain.entity.PasswordResetOtp
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID
import java.time.LocalDateTime
/**
 * Repository for OTP management with focus on password reset functionality.
 *
 * Technical Details:
 * - Primary key: UUID (auto-generated)
 * - Index on email + otp combination
 * - Index on email for latest lookup
 *
 * Database Considerations:
 * - Regular cleanup required for expired OTPs
 * - High write, low read ratio
 * - Potential for table growth
 *
 * Usage Examples:
 * ```kotlin
 * // Validate OTP
 * val otp = repository.findByEmailAndOtpAndIsUsedFalseAndExpiresAtAfter(
 *     email = "user@example.com",
 *     otp = "123456",
 *     currentTime = LocalDateTime.now()
 * )
 *
 * // Rate limiting check
 * val lastOtp = repository.findLatestByEmail("user@example.com")
 * if (lastOtp?.createdAt?.isAfter(LocalDateTime.now().minusMinutes(5))) {
 *     throw TooManyRequestsException()
 * }
 * ```
 *
 * Implementation Notes:
 * - Uses composite queries for validation
 * - No soft delete mechanism
 * - Requires periodic cleanup job
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
