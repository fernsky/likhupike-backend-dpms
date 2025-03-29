package np.likhupikemun.dpis.common.service

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.NoSuchMessageException
import org.springframework.stereotype.Service
import java.util.*

@Service
class I18nMessageService(
    private val messageSource: MessageSource
) {
    fun getMessage(code: String, args: Array<Any>? = null, defaultMessage: String? = null): String {
        return try {
            messageSource.getMessage(code, args, LocaleContextHolder.getLocale()) ?: defaultMessage ?: code
        } catch (e: NoSuchMessageException) {
            defaultMessage ?: code
        }
    }

    fun getErrorMessage(errorCode: String, defaultMessage: String? = null): String {
        return getMessage("auth.error.$errorCode", null, defaultMessage)
    }
}
