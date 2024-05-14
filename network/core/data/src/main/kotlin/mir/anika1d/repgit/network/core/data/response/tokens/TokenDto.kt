package mir.anika1d.repgit.network.core.data.response.tokens

data class TokenDto(
    val accessToken: String,
    val refreshToken: String,
    val idToken: String
)
