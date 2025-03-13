package np.likhupikemun.dpms.auth.security

data class TokenPair(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)
