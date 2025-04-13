package np.sthaniya.dpis.ui.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Description
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect
import org.thymeleaf.spring6.SpringTemplateEngine
import org.thymeleaf.spring6.view.ThymeleafViewResolver
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect
import org.thymeleaf.templatemode.TemplateMode
import org.springframework.web.servlet.ViewResolver
import org.thymeleaf.templateresolver.ITemplateResolver
import org.thymeleaf.spring6.ISpringTemplateEngine
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.config.annotation.InterceptorRegistry

/**
 * Thymeleaf configuration for UI templates.
 */
@Configuration
class ThymeleafConfig : WebMvcConfigurer {
    
    @Bean
    @Description("Thymeleaf template resolver for UI templates")
    fun templateResolver(): ITemplateResolver = ClassLoaderTemplateResolver().apply {
        prefix = "templates/"
        suffix = ".html"
        templateMode = TemplateMode.HTML
        characterEncoding = "UTF-8"
        setCacheable(false) // Disable caching for development
    }
    
    @Bean
    @Description("Thymeleaf template engine with Spring Security and layout dialects")
    fun templateEngine(): ISpringTemplateEngine = SpringTemplateEngine().apply {
        setTemplateResolver(templateResolver())
        enableSpringELCompiler = true
        addDialect(LayoutDialect())
        addDialect(SpringSecurityDialect())
        addDialect(Java8TimeDialect())
    }
    
    @Bean
    @Description("Thymeleaf view resolver")
    fun viewResolver(): ViewResolver = ThymeleafViewResolver().apply {
        templateEngine = templateEngine()
        characterEncoding = "UTF-8"
        contentType = "text/html;charset=UTF-8"
        viewNames = arrayOf("*")
    }
    
    /**
     * Add a request interceptor to add the canonical URL to the model
     * since Thymeleaf 3.1+ doesn't automatically expose request objects.
     */
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(object : HandlerInterceptor {
            override fun postHandle(
                request: HttpServletRequest,
                response: HttpServletResponse,
                handler: Any,
                modelAndView: ModelAndView?
            ) {
                if (modelAndView != null) {
                    // Add canonical URL information to the model
                    modelAndView.model["canonicalUrl"] = request.requestURL.toString()
                    modelAndView.model["requestUri"] = request.requestURI
                    modelAndView.model["contextPath"] = request.contextPath
                }
            }
        })
    }
}
