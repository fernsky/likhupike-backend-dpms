package np.likhupikemun.dpis.common.exception

interface ErrorCode {
    val code: String
    val defaultMessage: String
    val i18nKey: String
        get() = "auth.error.$code"
}
