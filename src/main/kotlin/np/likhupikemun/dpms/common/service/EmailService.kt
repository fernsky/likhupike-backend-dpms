package np.likhupikemun.dpis.common.service

import java.util.concurrent.CompletableFuture

interface EmailService {
    fun sendEmail(to: String, subject: String, htmlContent: String)
    fun sendPasswordResetEmail(to: String, resetToken: String)
    fun sendWelcomeEmail(to: String)
    fun sendAccountApprovedEmail(to: String)
    fun sendAccountCreatedEmail(to: String, resetToken: String)
    fun sendPasswordResetOtp(to: String, otp: String)
    fun sendPasswordResetConfirmation(to: String)
    fun sendEmailAsync(to: String, subject: String, htmlContent: String): CompletableFuture<Void>
    fun sendAccountCreatedEmailAsync(to: String, resetToken: String): CompletableFuture<Void>
    fun sendAccountApprovedEmailAsync(to: String): CompletableFuture<Void>
    fun sendWelcomeEmailAsync(to: String) : CompletableFuture<Void>
    fun sendPasswordResetEmailAsync(to: String, resetToken: String): CompletableFuture<Void>
    fun sendPasswordResetOtpAsync(to: String, otp: String): CompletableFuture<Void>
    fun sendPasswordResetConfirmationAsync(to: String): CompletableFuture<Void>
}
