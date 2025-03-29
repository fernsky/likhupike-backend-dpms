package np.sthaniya.dpis.common.annotation

/**
 * Marks an API endpoint as publicly accessible without authentication.
 *
 * This annotation is used to:
 * 1. Filter public routes in OpenAPI documentation grouping
 * 2. Configure security settings for public endpoints
 * 3. Identify routes that don't require authentication in route registry
 *
 * Example usage:
 * ```
 * @Public
 * @GetMapping("/health")
 * fun healthCheck(): HealthResponse
 * ```
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Public
