package np.likhupikemun.dpms.config

import np.likhupikemun.dpms.auth.exception.AuthException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.authentication.BadCredentialsException

class TestAuthenticationManager(
    private val passwordEncoder: PasswordEncoder
) : AuthenticationManager {
    
    override fun authenticate(authentication: Authentication): Authentication {
        val username = authentication.name
        val password = authentication.credentials as String

        if (username == "test@error.com") {
            throw BadCredentialsException("Invalid credentials")
        }

        // For test environment, use direct comparison
        if (password != "Test@123") {
            throw BadCredentialsException("Invalid credentials")
        }

        return UsernamePasswordAuthenticationToken(
            username,
            password,
            listOf(SimpleGrantedAuthority("ROLE_USER"))
        )
    }
}
