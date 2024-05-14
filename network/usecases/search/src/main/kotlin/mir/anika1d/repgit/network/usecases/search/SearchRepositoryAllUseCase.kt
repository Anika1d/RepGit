package mir.anika1d.repgit.network.usecases.search

import kotlinx.coroutines.flow.Flow
import mir.anika1d.repgit.network.core.data.response.ResultState
import mir.anika1d.repgit.network.core.data.model.repository.PageRepository
import mir.anika1d.repgit.network.core.data.request.SearchRequest
import mir.anika1d.repgit.network.github.repositories.IGithubRepository


class SearchRepositoryAllUseCase(private val repository: IGithubRepository) {

    private suspend fun search(searchRequest: SearchRequest): Flow<ResultState<PageRepository>> {
        return repository.searchRepository(searchRequest)

    }

    suspend operator fun invoke(searchRequest: SearchRequest): Flow<ResultState<PageRepository>> {
        return search(searchRequest)
    }
}