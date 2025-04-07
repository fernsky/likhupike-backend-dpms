package np.sthaniya.dpis.citizen.repository

import np.sthaniya.dpis.citizen.domain.entity.CitizenPasswordResetOtp
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID

/**
 * Repository for managing citizen password reset OTPs.
 */
@Repository
interface CitizenPasswordResetOtpRepository : JpaRepository<CitizenPasswordResetOtp, UUID> {

    /**
     * Finds a valid OTP entry for the given email and OTP code.
     *
     * @param email The citizen's email address
     * @param otp The one-time password code
     * @param currentTime The current time to check against expiration
     * @return The OTP entity if found and not expired, otherwise null
     */
    fun findByEmailAndOtpAndIsUsedFalseAndExpiresAtAfter(
        email: String,
        otp: String,
        currentTime: LocalDateTime
    ): CitizenPasswordResetOtp?
    
    /**
     * Finds the most recent OTP issued for a specific email.
     *
     * @param email The citizen's email address
     * @return The most recent OTP entity if found, otherwise null
     */
    fun findFirstByEmailOrderByCreatedAtDesc(email: String): CitizenPasswordResetOtp?
}
