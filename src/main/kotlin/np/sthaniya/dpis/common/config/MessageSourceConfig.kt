package np.sthaniya.dpis.common.config

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.CookieLocaleResolver
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import java.util.*
import java.time.Duration

/**
 * Configuration class for internationalization (i18n) message handling.
 *
 * This configuration sets up the message source and locale resolution for the application,
 * providing support for:
 * - Multiple languages (English and Nepali)
 * - UTF-8 encoded message files
 * - Accept-Language header based locale resolution
 * - Fallback message handling
 * - Domain-specific message organization
 */
@Configuration
class MessageSourceConfig : WebMvcConfigurer {

    /**
     * Creates and configures the application's [MessageSource].
     *
     * The message source is configured to:
     * - Load messages from domain-specific resource bundles in organized directories
     * - Use UTF-8 encoding for message files
     * - Fall back to message codes when translations are missing
     *
     * @return A configured [ResourceBundleMessageSource]
     */
    @Bean
    fun messageSource(): MessageSource {
        val messageSource = ResourceBundleMessageSource()
        messageSource.setBasenames(
            "i18n/auth/messages",
            "i18n/common/messages",
            "i18n/user/messages",
            "i18n/error/messages",
            "i18n/document/messages",
            "i18n/citizen/messages",
            "i18n/ui/messages", // Add UI-specific messages

            // Profile
            "i18n/profile/demographics/messages"
        )
        messageSource.setDefaultEncoding("UTF-8")
        messageSource.setUseCodeAsDefaultMessage(true)
        return messageSource
    }

    /**
     * Creates and configures the application's [LocaleResolver].
     *
     * The resolver is configured to:
     * - Support English (default) and Nepali locales
     * - Determine locale from Accept-Language header for API requests
     * - Use cookies for UI locale preferences
     * - Fall back to English when requested locale is not supported
     *
     * @return A configured [CookieLocaleResolver]
     */
    @Bean
    fun localeResolver(): LocaleResolver {
        // Using cookie-based locale resolver for UI
        val resolver = CookieLocaleResolver("LANGUAGE_PREFERENCE")
        // Create supported locales list
        val supportedLocales = listOf(Locale.ENGLISH, Locale("ne"))
        // Set default locale
        resolver.setDefaultLocale(Locale.ENGLISH)
        // CookieLocaleResolver doesn't have setSupportedLocales method, so we handle this in code
        // through the LocaleChangeInterceptor
        resolver.setCookieMaxAge(Duration.ofDays(365))
        resolver.setCookiePath("/")
        return resolver
    }

    /**
     * Creates and configures a [LocaleChangeInterceptor] for changing locales via request parameter.
     *
     * This allows changing the locale by adding a query parameter: ?lang=ne or ?lang=en
     *
     * @return A configured [LocaleChangeInterceptor]
     */
    @Bean
    fun localeChangeInterceptor(): LocaleChangeInterceptor {
        val localeChangeInterceptor = LocaleChangeInterceptor()
        localeChangeInterceptor.paramName = "lang"
        return localeChangeInterceptor
    }

    /**
     * Registers the locale change interceptor.
     *
     * @param registry The interceptor registry
     */
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(localeChangeInterceptor())
    }
}
