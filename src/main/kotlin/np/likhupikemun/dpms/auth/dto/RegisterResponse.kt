package np.likhupikemun.dpms.auth.dto

data class RegisterResponse(
    val email: String,
    val message: String = "Your registration is pending approval. You will be able to login once an administrator approves your account."
)
