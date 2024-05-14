package mir.anika1d.repgit.network.usecases.search

import kotlinx.coroutines.flow.Flow
import mir.anika1d.repgit.network.core.data.model.user.PageUser
import mir.anika1d.repgit.network.core.data.request.SearchRequest
import mir.anika1d.repgit.network.core.data.request.UserRequest
import mir.anika1d.repgit.network.core.data.response.ResultState
import mir.anika1d.repgit.network.github.repositories.IGithubRepository

class SearchUserUseCase(private val repository: IGithubRepository) {
    private suspend fun getUserList(searchRequest: SearchRequest): Flow<ResultState<PageUser>> {
        return repository.searchUser(searchRequest)
    }
    suspend operator fun invoke(searchRequest: SearchRequest): Flow<ResultState<PageUser>> {
        return getUserList(searchRequest)
    }

}