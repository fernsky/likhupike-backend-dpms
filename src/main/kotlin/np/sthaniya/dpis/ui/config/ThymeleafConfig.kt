package np.sthaniya.dpis.ui.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Description
import org.springframework.web.servlet.ViewResolver
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect
import org.thymeleaf.spring6.SpringTemplateEngine
import org.thymeleaf.spring6.view.ThymeleafViewResolver
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect

/**
 * Thymeleaf configuration for UI templates.
 */
@Configuration
class ThymeleafConfig {
    
    @Bean
    @Description("Thymeleaf template resolver for UI templates")
    fun templateResolver() = ClassLoaderTemplateResolver().apply {
        prefix = "templates/"
        suffix = ".html"
        templateMode = "HTML"
        characterEncoding = "UTF-8"
        setCacheable(false) // Disable caching for development
    }
    
    @Bean
    @Description("Thymeleaf template engine with Spring Security and layout dialects")
    fun templateEngine() = SpringTemplateEngine().apply {
        setTemplateResolver(templateResolver())
        enableSpringELCompiler = true
        addDialect(LayoutDialect())
        addDialect(SpringSecurityDialect())
        addDialect(Java8TimeDialect())
    }
    
    @Bean
    @Description("Thymeleaf view resolver")
    fun viewResolver() = ThymeleafViewResolver().apply {
        templateEngine = templateEngine()
        characterEncoding = "UTF-8"
        contentType = "text/html;charset=UTF-8"
        viewNames = arrayOf("*")
    }
}
