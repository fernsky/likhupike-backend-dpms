package np.sthaniya.dpis.auth.controller

import np.sthaniya.dpis.auth.controller.base.BaseRestDocsTest
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.request.ParameterDescriptor
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName

/**
 * Documentation templates for Auth API
 * 
 * This class provides reusable snippets and field descriptors for
 * documenting the Authentication API endpoints consistently.
 */
class AuthApiDocTemplates {
    companion object {
        // Common request field descriptors
        val loginRequestFields = listOf(
            fieldWithPath("email").description("User's email address"),
            fieldWithPath("password").description("User's password")
        )
        
        val registerRequestFields = listOf(
            fieldWithPath("email").description("User's email address"),
            fieldWithPath("password").description("User's password"),
            fieldWithPath("confirmPassword").description("Password confirmation"),
            fieldWithPath("isWardLevelUser").description("Flag indicating if user is ward-level"),
            fieldWithPath("wardNumber").optional().description("Ward number (required if isWardLevelUser is true)")
        )
        
        val passwordResetRequestFields = listOf(
            fieldWithPath("email").description("User's email address")
        )
        
        val passwordResetFields = listOf(
            fieldWithPath("email").description("User's email address"),
            fieldWithPath("otp").description("One-time password received via email"),
            fieldWithPath("newPassword").description("New password"),
            fieldWithPath("confirmPassword").description("New password confirmation")
        )
        
        val changePasswordFields = listOf(
            fieldWithPath("currentPassword").description("Current password"),
            fieldWithPath("newPassword").description("New password"),
            fieldWithPath("confirmPassword").description("New password confirmation")
        )
        
        // Common header parameters
        val authHeaderParam = parameterWithName("Authorization")
            .description("Bearer authentication token")
        
        val refreshTokenParam = parameterWithName("X-Refresh-Token")
            .description("Refresh token for obtaining a new access token")
            
        // Common error codes with descriptions
        val errorCodeDescriptions = mapOf(
            "AUTH_001" to "User not found",
            "AUTH_002" to "User already exists",
            "AUTH_003" to "User is already deleted",
            "AUTH_004" to "User is already approved",
            "AUTH_005" to "Invalid user status",
            "AUTH_006" to "Permission not found",
            "AUTH_007" to "Required permissions missing",
            "AUTH_008" to "Authentication required",
            "AUTH_009" to "Insufficient permissions",
            "AUTH_010" to "Invalid credentials",
            "AUTH_011" to "User not approved",
            "AUTH_012" to "Invalid or expired token",
            "AUTH_013" to "Invalid password provided",
            "AUTH_014" to "JWT token validation failed",
            "AUTH_015" to "Password reset token is invalid or expired",
            "AUTH_016" to "Passwords don't match",
            "AUTH_017" to "Too many failed attempts",
            "AUTH_018" to "Page does not exist",
            "VALIDATION_ERROR" to "Input validation failed"
        )
    }
}
