package np.sthaniya.dpis.auth.resolver

/**
 * Parameter annotation for automatically resolving the current authenticated user's ID.
 *
 * This annotation is used to inject the UUID of the currently authenticated user
 * into controller method parameters. It works in conjunction with [CurrentUserIdResolver]
 * to extract the user ID from the security context.
 *
 * Usage:
 * ```kotlin
 * @PostMapping("/example")
 * fun exampleEndpoint(@CurrentUserId userId: UUID) {
 *     // userId contains the authenticated user's ID
 * }
 * ```
 *
 * Features:
 * - Type-safe user ID injection
 * - Automatic security context resolution
 * - Null-safety handling
 *
 * Security Considerations:
 * - Only works for authenticated requests
 * - Returns null for unauthenticated requests
 * - Requires proper security context setup
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class CurrentUserId
