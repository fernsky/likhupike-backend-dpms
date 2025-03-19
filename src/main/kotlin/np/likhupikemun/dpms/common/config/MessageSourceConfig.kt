package np.likhupikemun.dpms.common.config

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
import org.springframework.web.servlet.LocaleResolver
import java.util.*

@Configuration
class MessageSourceConfig : WebMvcConfigurer {
    
    @Bean
    fun messageSource(): MessageSource {
        val messageSource = ResourceBundleMessageSource()
        messageSource.setBasenames("messages")
        messageSource.setDefaultEncoding("UTF-8")
        messageSource.setUseCodeAsDefaultMessage(true)
        return messageSource
    }

    @Bean
    fun localeResolver(): LocaleResolver {
        val resolver = AcceptHeaderLocaleResolver()
        // Create supported locales list
        val supportedLocales = listOf(Locale.ENGLISH, Locale("np"))
        // Set default locale
        resolver.setDefaultLocale(Locale.ENGLISH)
        // Set supported locales
        resolver.setSupportedLocales(supportedLocales)
        return resolver
    }
}