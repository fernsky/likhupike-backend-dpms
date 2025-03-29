package np.sthaniya.dpis.common.config

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
import org.springframework.web.servlet.LocaleResolver
import java.util.*

/**
 * Configuration class for internationalization (i18n) message handling.
 *
 * This configuration sets up the message source and locale resolution for the application,
 * providing support for:
 * - Multiple languages (English and Nepali)
 * - UTF-8 encoded message files
 * - Accept-Language header based locale resolution
 * - Fallback message handling
 */
@Configuration
class MessageSourceConfig : WebMvcConfigurer {
    
    /**
     * Creates and configures the application's [MessageSource].
     *
     * The message source is configured to:
     * - Load messages from "messages" resource bundles
     * - Use UTF-8 encoding for message files
     * - Fall back to message codes when translations are missing
     *
     * @return A configured [ResourceBundleMessageSource]
     */
    @Bean
    fun messageSource(): MessageSource {
        val messageSource = ResourceBundleMessageSource()
        messageSource.setBasenames("messages")
        messageSource.setDefaultEncoding("UTF-8")
        messageSource.setUseCodeAsDefaultMessage(true)
        return messageSource
    }

    /**
     * Creates and configures the application's [LocaleResolver].
     *
     * The resolver is configured to:
     * - Support English (default) and Nepali locales
     * - Determine locale from Accept-Language header
     * - Fall back to English when requested locale is not supported
     *
     * @return A configured [AcceptHeaderLocaleResolver]
     */
    @Bean
    fun localeResolver(): LocaleResolver {
        val resolver = AcceptHeaderLocaleResolver()
        // Create supported locales list
        val supportedLocales = listOf(Locale.ENGLISH, Locale("ne"))
        // Set default locale
        resolver.setDefaultLocale(Locale.ENGLISH)
        // Set supported locales
        resolver.setSupportedLocales(supportedLocales)
        return resolver
    }
}
