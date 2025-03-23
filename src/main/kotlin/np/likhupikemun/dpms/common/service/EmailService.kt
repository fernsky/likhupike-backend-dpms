package np.likhupikemun.dpms.common.service

interface EmailService {
    fun sendEmail(to: String, subject: String, htmlContent: String)
    fun sendPasswordResetEmail(to: String, resetToken: String)
    fun sendWelcomeEmail(to: String)
    fun sendAccountApprovedEmail(to: String)
    fun sendAccountCreatedEmail(to: String, resetToken: String)
}
