package np.sthaniya.dpis.common.service.impl

import np.sthaniya.dpis.common.service.EmailService
import np.sthaniya.dpis.common.enum.EmailTemplate
import org.slf4j.LoggerFactory
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import jakarta.mail.internet.MimeMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import java.util.concurrent.CompletableFuture

/**
 * Implementation of [EmailService] that handles email operations using Spring's [JavaMailSender].
 *
 * This service provides both synchronous and asynchronous email sending capabilities,
 * utilizing predefined email templates for various system notifications.
 *
 * @property mailSender The Spring mail sender for handling email operations
 * @property frontendUrl The base URL of the frontend application
 * @property fromAddress The email address used as the sender
 *
 * @constructor Creates an email service with the specified mail sender and configuration properties
 */
@Service
class EmailServiceImpl(
    private val mailSender: JavaMailSender,
    @Value("\${app.frontend-url}") private val frontendUrl: String,
    @Value("\${spring.mail.from}") private val fromAddress: String
) : EmailService {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun sendEmail(to: String, subject: String, htmlContent: String) {
        try {
            val message: MimeMessage = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, true, "UTF-8")
            
            helper.setFrom(fromAddress)
            helper.setTo(to)
            helper.setSubject(subject)
            helper.setText(htmlContent, true)
            
            mailSender.send(message)
            log.info("Email sent successfully to: $to")
        } catch (e: Exception) {
            log.error("Failed to send email to: $to", e)
            throw RuntimeException("Failed to send email", e)
        }
    }

    override fun sendPasswordResetEmail(to: String, resetToken: String) {
        val resetLink = "$frontendUrl/reset-password?token=$resetToken"
        val template = EmailTemplate.PASSWORD_RESET.template.replace("%resetLink%", resetLink)
        
        sendEmail(
            to = to,
            subject = EmailTemplate.PASSWORD_RESET.subject,
            htmlContent = template
        )
    }

    override fun sendWelcomeEmail(to: String) {
        val template = EmailTemplate.WELCOME.template
            .replace("%email%", to)
        
        sendEmail(
            to = to,
            subject = EmailTemplate.WELCOME.subject,
            htmlContent = template
        )
    }

    override fun sendAccountApprovedEmail(to: String) {
        val loginLink = "$frontendUrl/login"
        val template = EmailTemplate.ACCOUNT_APPROVED.template
            .replace("%loginLink%", loginLink)
            .replace("%email%", to)
        
        sendEmail(
            to = to,
            subject = EmailTemplate.ACCOUNT_APPROVED.subject,
            htmlContent = template
        )
    }

    override fun sendAccountCreatedEmail(to: String, resetToken: String) {
        val resetLink = "$frontendUrl/set-password?token=$resetToken"
        val template = EmailTemplate.ACCOUNT_CREATED.template
            .replace("%resetLink%", resetLink)
            .replace("%email%", to)
        
        sendEmail(
            to = to,
            subject = EmailTemplate.ACCOUNT_CREATED.subject,
            htmlContent = template
        )
    }

    override fun sendPasswordResetOtp(to: String, otp: String) {
        val template = EmailTemplate.PASSWORD_RESET_OTP.template
            .replace("%otp%", otp)
        
        sendEmail(
            to = to,
            subject = EmailTemplate.PASSWORD_RESET_OTP.subject,
            htmlContent = template
        )
    }

    override fun sendPasswordResetConfirmation(to: String) {
        val loginLink = "$frontendUrl/login"
        val template = EmailTemplate.PASSWORD_RESET_SUCCESS.template
            .replace("%loginLink%", loginLink)
        
        sendEmail(
            to = to,
            subject = EmailTemplate.PASSWORD_RESET_SUCCESS.subject,
            htmlContent = template
        )
    }

    @Async
    override fun sendEmailAsync(to: String, subject: String, htmlContent: String): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            sendEmail(to, subject, htmlContent)
        }
    }

    @Async
    override fun sendAccountCreatedEmailAsync(to: String, resetToken: String): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            sendAccountCreatedEmail(to, resetToken)
        }
    }

    @Async
    override fun sendAccountApprovedEmailAsync(to: String): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            sendAccountApprovedEmail(to)
        }
    }

    @Async
    override fun sendWelcomeEmailAsync(to: String): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            sendWelcomeEmail(to)
        }
    }

    @Async
    override fun sendPasswordResetEmailAsync(to: String, resetToken: String): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            sendPasswordResetEmail(to, resetToken)
        }
    }

    @Async
    override fun sendPasswordResetOtpAsync(to: String, otp: String): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            sendPasswordResetOtp(to, otp)
        }
    }

    @Async
    override fun sendPasswordResetConfirmationAsync(to: String): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            sendPasswordResetConfirmation(to)
        }
    }
}
