package mir.anika1d.repgit.network.usecases.auth

import net.openid.appauth.AuthorizationRequest
import mir.anika1d.repgit.network.github.repositories.IGithubAuthRepository
import mir.anika1d.repgit.network.github.repositories.IGithubRepository


class AuthUseCase(private val repository: IGithubRepository)  {

   private  fun auth(): AuthorizationRequest {
        return repository.auth()
    }

    operator fun invoke():AuthorizationRequest {
        return auth()
    }

}