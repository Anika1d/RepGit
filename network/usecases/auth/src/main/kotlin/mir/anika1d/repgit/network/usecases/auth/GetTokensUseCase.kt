package mir.anika1d.repgit.network.usecases.auth

import mir.anika1d.repgit.network.core.data.response.tokens.TokenDto
import mir.anika1d.repgit.network.github.repositories.IGithubRepository
import net.openid.appauth.TokenRequest


class GetTokensUseCase(private val repository: IGithubRepository)  {

    private  suspend fun getTokens(tokenRequest: TokenRequest): TokenDto {
        return repository.getTokens(tokenRequest)
    }

    suspend operator fun invoke(tokenRequest: TokenRequest): TokenDto {
        return getTokens(tokenRequest)
    }

}