package np.sthaniya.dpis.auth.repository

import np.sthaniya.dpis.auth.domain.entity.PasswordResetOtp
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID
import java.time.LocalDateTime

@Repository
interface PasswordResetOtpRepository : JpaRepository<PasswordResetOtp, UUID> {
    fun findByEmailAndOtpAndIsUsedFalseAndExpiresAtAfter(
        email: String,
        otp: String,
        currentTime: LocalDateTime
    ): PasswordResetOtp?

    fun findLatestByEmail(email: String): PasswordResetOtp?
}
