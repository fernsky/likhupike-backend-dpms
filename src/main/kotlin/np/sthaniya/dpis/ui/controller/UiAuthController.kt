package np.sthaniya.dpis.ui.controller

import np.sthaniya.dpis.auth.dto.LoginRequest
import np.sthaniya.dpis.auth.dto.RegisterRequest
import np.sthaniya.dpis.auth.dto.RequestPasswordResetRequest
import np.sthaniya.dpis.auth.dto.ResetPasswordRequest
import np.sthaniya.dpis.auth.service.AuthService
import np.sthaniya.dpis.common.service.I18nMessageService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import jakarta.servlet.http.HttpServletRequest

/**
 * Controller handling UI routes for authentication pages.
 * 
 * This controller:
 * 1. Renders Thymeleaf templates for auth-related pages
 * 2. Maps UI forms to API DTOs
 * 3. Provides i18n support for all UI elements
 */
@Controller
@RequestMapping("/ui")
class UiAuthController(
    private val authService: AuthService,
    private val i18nMessageService: I18nMessageService
) {

    /**
     * Renders the login page.
     */
    @GetMapping("/login")
    fun loginPage(
        @RequestParam(required = false) error: Boolean?,
        @RequestParam(required = false) logout: Boolean?,
        model: Model
    ): String {
        error?.let { model.addAttribute("errorMessage", i18nMessageService.getMessage("auth.error.AUTH_010")) }
        logout?.let { model.addAttribute("logoutMessage", i18nMessageService.getMessage("auth.logout.success")) }
        model.addAttribute("loginRequest", LoginRequest("", ""))
        return "auth/login"
    }

    /**
     * Renders the registration page.
     */
    @GetMapping("/register")
    fun registerPage(model: Model): String {
        model.addAttribute("registerRequest", RegisterRequest("", "", "", ""))
        return "auth/register"
    }

    /**
     * Handles the registration form submission.
     */
    @PostMapping("/register")
    fun register(registerRequest: RegisterRequest, redirectAttributes: RedirectAttributes): String {
        try {
            authService.register(registerRequest)
            redirectAttributes.addFlashAttribute("successMessage", 
                i18nMessageService.getMessage("auth.register.success"))
            return "redirect:/ui/login"
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute("errorMessage", e.message)
            redirectAttributes.addFlashAttribute("registerRequest", registerRequest)
            return "redirect:/ui/register"
        }
    }

    /**
     * Renders the password reset request page (Step 1).
     * This is the first step where user enters their email to receive an OTP.
     */
    @GetMapping("/password-reset")
    fun passwordResetRequestPage(model: Model): String {
        model.addAttribute("passwordResetRequest", RequestPasswordResetRequest(""))
        return "auth/password-reset-request"
    }

    /**
     * Handles the password reset request form submission (Step 1).
     * This sends an OTP to the user's email.
     */
    @PostMapping("/password-reset")
    fun passwordResetRequest(
        passwordResetRequest: RequestPasswordResetRequest,
        redirectAttributes: RedirectAttributes
    ): String {
        try {
            authService.requestPasswordReset(passwordResetRequest)
            redirectAttributes.addFlashAttribute("successMessage", 
                i18nMessageService.getMessage("auth.password.reset.request.success"))
            // Redirect to the OTP confirmation page, passing the email
            return "redirect:/ui/password-reset/confirm?email=" + passwordResetRequest.email
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute("errorMessage", e.message)
            return "redirect:/ui/password-reset"
        }
    }

    /**
     * Renders the password reset confirmation page (Step 2).
     * This is where the user enters the OTP and new password.
     */
    @GetMapping("/password-reset/confirm")
    fun passwordResetConfirmPage(
        @RequestParam email: String?,
        model: Model
    ): String {
        if (email.isNullOrEmpty()) {
            return "redirect:/ui/password-reset"
        }
        
        model.addAttribute("email", email)
        // Initialize with empty values but pre-fill the email
        model.addAttribute("resetPasswordRequest", ResetPasswordRequest(email, "", "", ""))
        return "auth/password-reset-confirm"
    }

    /**
     * Handles the password reset confirmation form submission (Step 2).
     * This validates the OTP and resets the password.
     */
    @PostMapping("/password-reset/confirm")
    fun passwordResetConfirm(
        resetPasswordRequest: ResetPasswordRequest,
        redirectAttributes: RedirectAttributes
    ): String {
        try {
            // Ensure passwords match (this might already be validated in the DTO)
            if (resetPasswordRequest.newPassword != resetPasswordRequest.confirmPassword) {
                throw IllegalArgumentException(i18nMessageService.getMessage("auth.error.AUTH_016"))
            }
            
            authService.resetPassword(resetPasswordRequest)
            redirectAttributes.addFlashAttribute("successMessage", 
                i18nMessageService.getMessage("auth.password.reset.success"))
            return "redirect:/ui/login"
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute("errorMessage", e.message)
            redirectAttributes.addFlashAttribute("email", resetPasswordRequest.email)
            // Keep the email when redirecting back to the form
            return "redirect:/ui/password-reset/confirm?email=${resetPasswordRequest.email}"
        }
    }
    
    /**
     * Renders the home/landing page.
     */
    @GetMapping(value = ["", "/"])
    fun homePage(): String {
        return "auth/home"
    }
    
    /**
     * Renders the access denied page.
     */
    @GetMapping("/access-denied")
    fun accessDeniedPage(): String {
        return "error/access-denied"
    }
}
