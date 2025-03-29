package np.sthaniya.dpis.common.service

import np.sthaniya.dpis.common.exception.ErrorCode
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.NoSuchMessageException
import org.springframework.stereotype.Service
import java.util.*

/**
 * Service for handling internationalized messages in the application.
 * 
 * This service provides methods to retrieve localized messages using Spring's [MessageSource].
 * Messages are retrieved based on the current locale from [LocaleContextHolder].
 *
 * @property messageSource The Spring message source containing the localized messages
 */
@Service
class I18nMessageService(
    private val messageSource: MessageSource
) {
    /**
     * Retrieves a localized message for the given code.
     *
     * @param code The message code to look up
     * @param args Optional array of arguments to be used for message formatting
     * @param defaultMessage Optional fallback message if the code is not found
     * @return The localized message, or the default message/code if not found
     */
    fun getMessage(code: String, args: Array<Any>? = null, defaultMessage: String? = null): String {
        return try {
            messageSource.getMessage(code, args, LocaleContextHolder.getLocale()) ?: defaultMessage ?: code
        } catch (e: NoSuchMessageException) {
            defaultMessage ?: code
        }
    }

    /**
     * Retrieves a localized error message for the given error code object.
     * 
     * @param errorCode The error code object
     * @param args Optional array of arguments to be used for message formatting
     * @return The localized error message, or the default message if not found
     */
    fun getErrorMessage(errorCode: ErrorCode, args: Array<Any>? = null): String {
        return getMessage(errorCode.i18nKey, args, errorCode.defaultMessage)
    }

    /**
     * Retrieves a localized error message for the given error code string.
     * 
     * Error messages are looked up using the prefix "auth.error." followed by the error code.
     *
     * @param errorCode The specific error code as a string
     * @param defaultMessage Optional fallback message if the error code is not found
     * @return The localized error message, or the default message/error code if not found
     */
    fun getErrorMessage(errorCode: String, defaultMessage: String? = null): String {
        return getMessage("auth.error.$errorCode", null, defaultMessage)
    }
}
