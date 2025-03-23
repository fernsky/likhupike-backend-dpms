package np.likhupikemun.dpms.common.service.impl

import np.likhupikemun.dpms.common.service.EmailService
import np.likhupikemun.dpms.common.enum.EmailTemplate
import org.slf4j.LoggerFactory
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import jakarta.mail.internet.MimeMessage
import org.springframework.beans.factory.annotation.Value

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
}
