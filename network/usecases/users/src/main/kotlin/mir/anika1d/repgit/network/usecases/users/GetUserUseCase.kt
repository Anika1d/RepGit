package mir.anika1d.repgit.network.usecases.users

import kotlinx.coroutines.flow.Flow
import mir.anika1d.repgit.network.core.data.model.user.User
import mir.anika1d.repgit.network.core.data.request.UserRequest
import mir.anika1d.repgit.network.core.data.response.ResultState
import mir.anika1d.repgit.network.github.repositories.IGithubRepository

class GetUserUseCase(private val repository: IGithubRepository) {

    private suspend fun getUser(userRequest: UserRequest): Flow<ResultState<User>> {
       return repository.getUser(userRequest)
    }
    suspend operator fun invoke(userRequest: UserRequest): Flow<ResultState<User>> {
        return getUser(userRequest)
    }

}