package np.likhupikemun.dpms.auth.domain.entity

import jakarta.persistence.*
import np.likhupikemun.dpms.common.entity.UuidBaseEntity
import java.time.LocalDateTime

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

    fun isValid(): Boolean =
        !isUsed && LocalDateTime.now().isBefore(expiresAt) && attempts < 3
}
