package np.sthaniya.dpis.common.annotation
import np.sthaniya.dpis.common.enum.RateLimitType

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RateLimited(
    val type: RateLimitType = RateLimitType.API,
    val useClientLimit: Boolean = true
)