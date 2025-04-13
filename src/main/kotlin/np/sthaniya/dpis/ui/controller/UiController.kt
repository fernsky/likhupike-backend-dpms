package np.sthaniya.dpis.ui.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Controller handling UI routes for the Digital Profile System showcase.
 * 
 * This controller:
 * 1. Serves the public-facing landing page
 * 2. Provides information about the Digital Profile System
 */
@Controller
class UiController {
    /**
     * Renders the showcase homepage at the root path.
     */
    @GetMapping("/")
    fun homePage(model: Model): String {
        model.addAttribute("activeTab", "home")
        model.addAttribute("pageTitle", "Digital Profile System")
        return "showcase/home"
    }
    
    /**
     * Renders the access denied page.
     */
    @GetMapping("/access-denied")
    fun accessDeniedPage(model: Model): String {
        model.addAttribute("activeTab", "none")
        return "error/access-denied"
    }
}
