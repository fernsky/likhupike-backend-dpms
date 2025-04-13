package np.sthaniya.dpis.ui.controller

import np.sthaniya.dpis.common.service.I18nMessageService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.security.core.Authentication
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.authentication.AuthenticationManager

/**
 * Controller handling traditional form login processes separately from the main auth controller.
 * 
 * This controller:
 * 1. Processes form logins through Spring Security's form login infrastructure
 * 2. Integrates with the existing security model
 */
@Controller
@RequestMapping("/ui")
class UiLoginController(
    private val i18nMessageService: I18nMessageService,
    private val userDetailsService: UserDetailsService,
    private val authenticationManager: AuthenticationManager
) {
    /**
     * Handles direct form login submission (test method if Spring Security form login isn't working).
     * This is a fallback method and shouldn't normally be needed when using formLogin() properly.
     */
    @PostMapping("/login-manual")
    fun processLoginManually(
        @RequestParam username: String,
        @RequestParam password: String,
        model: Model
    ): String {
        try {
            val authRequest = UsernamePasswordAuthenticationToken(username, password)
            val authentication = authenticationManager.authenticate(authRequest)
            
            if (authentication.isAuthenticated) {
                SecurityContextHolder.getContext().authentication = authentication
                return "redirect:/ui/dashboard" 
            } else {
                model.addAttribute("errorMessage", i18nMessageService.getMessage("auth.error.AUTH_010"))
                return "auth/login"
            }
        } catch (e: Exception) {
            model.addAttribute("errorMessage", i18nMessageService.getMessage("auth.error.AUTH_010"))
            return "auth/login"
        }
    }
}
