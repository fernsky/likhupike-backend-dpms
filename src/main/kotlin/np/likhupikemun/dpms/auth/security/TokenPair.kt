package np.likhupikemun.dpis.auth.security

data class TokenPair(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)
