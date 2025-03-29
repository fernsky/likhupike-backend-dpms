package np.likhupikemun.dpis.auth.repository

import np.likhupikemun.dpis.auth.domain.entity.PasswordResetOtp
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
