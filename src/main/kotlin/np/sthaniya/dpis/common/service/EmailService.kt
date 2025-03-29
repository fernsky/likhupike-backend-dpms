package np.sthaniya.dpis.common.service

import java.util.concurrent.CompletableFuture

/**
 * Service interface for handling email operations in the application.
 * Provides both synchronous and asynchronous methods for sending various types of emails.
 */
interface EmailService {
    /**
     * Sends an email with the specified content.
     *
     * @param to The recipient's email address
     * @param subject The subject line of the email
     * @param htmlContent The HTML content of the email body
     * @throws RuntimeException if the email sending fails
     */
    fun sendEmail(to: String, subject: String, htmlContent: String)

    /**
     * Sends a password reset email containing a reset token.
     *
     * @param to The recipient's email address
     * @param resetToken The token used for password reset verification
     */
    fun sendPasswordResetEmail(to: String, resetToken: String)

    /**
     * Sends a welcome email to a new user.
     *
     * @param to The recipient's email address
     */
    fun sendWelcomeEmail(to: String)

    /**
     * Sends an email notification when a user's account is approved.
     *
     * @param to The recipient's email address
     */
    fun sendAccountApprovedEmail(to: String)

    /**
     * Sends an email notification when a new account is created.
     *
     * @param to The recipient's email address
     * @param resetToken The token used for initial password setup
     */
    fun sendAccountCreatedEmail(to: String, resetToken: String)

    /**
     * Sends a one-time password (OTP) for password reset verification.
     *
     * @param to The recipient's email address
     * @param otp The one-time password
     */
    fun sendPasswordResetOtp(to: String, otp: String)

    /**
     * Sends a confirmation email after a successful password reset.
     *
     * @param to The recipient's email address
     */
    fun sendPasswordResetConfirmation(to: String)

    /**
     * Asynchronously sends an email with the specified content.
     *
     * @param to The recipient's email address
     * @param subject The subject line of the email
     * @param htmlContent The HTML content of the email body
     * @return A [CompletableFuture] representing the asynchronous operation
     */
    fun sendEmailAsync(to: String, subject: String, htmlContent: String): CompletableFuture<Void>

    /**
     * Asynchronously sends an account creation email.
     *
     * @param to The recipient's email address
     * @param resetToken The token used for initial password setup
     * @return A [CompletableFuture] representing the asynchronous operation
     */
    fun sendAccountCreatedEmailAsync(to: String, resetToken: String): CompletableFuture<Void>

    /**
     * Asynchronously sends an account approval notification.
     *
     * @param to The recipient's email address
     * @return A [CompletableFuture] representing the asynchronous operation
     */
    fun sendAccountApprovedEmailAsync(to: String): CompletableFuture<Void>

    /**
     * Asynchronously sends a welcome email.
     *
     * @param to The recipient's email address
     * @return A [CompletableFuture] representing the asynchronous operation
     */
    fun sendWelcomeEmailAsync(to: String): CompletableFuture<Void>

    /**
     * Asynchronously sends a password reset email.
     *
     * @param to The recipient's email address
     * @param resetToken The token used for password reset verification
     * @return A [CompletableFuture] representing the asynchronous operation
     */
    fun sendPasswordResetEmailAsync(to: String, resetToken: String): CompletableFuture<Void>

    /**
     * Asynchronously sends a password reset OTP.
     *
     * @param to The recipient's email address
     * @param otp The one-time password
     * @return A [CompletableFuture] representing the asynchronous operation
     */
    fun sendPasswordResetOtpAsync(to: String, otp: String): CompletableFuture<Void>

    /**
     * Asynchronously sends a password reset confirmation email.
     *
     * @param to The recipient's email address
     * @return A [CompletableFuture] representing the asynchronous operation
     */
    fun sendPasswordResetConfirmationAsync(to: String): CompletableFuture<Void>
}
