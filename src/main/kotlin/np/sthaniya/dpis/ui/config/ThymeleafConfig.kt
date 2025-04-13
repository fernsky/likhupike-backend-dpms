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
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.HandlerMapping
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.servlet.support.RequestContext
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.context.request.ServletRequestAttributes

/**
 * Thymeleaf configuration for UI templates.
 */
@Configuration
class ThymeleafConfig {
    
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
        
        // We can't use request objects directly in Thymeleaf 3.1+
        // Instead, we'll add the active tab attribute in the controller methods
        // and inject it into each view
    }
}
