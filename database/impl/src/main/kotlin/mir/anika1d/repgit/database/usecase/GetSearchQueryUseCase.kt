package mir.anika1d.repgit.database.usecase

import kotlinx.coroutines.flow.Flow
import mir.anika1d.repgit.database.api.ISearchQueryRepository
import mir.anika1d.repgit.database.data.model.SearchQuery
import mir.anika1d.repgit.database.data.request.SearchQueryRequest

class GetSearchQueryUseCase(private val repository: ISearchQueryRepository) {

    private suspend fun getSearchQueriesByName(request: SearchQueryRequest): Flow<List<SearchQuery>> {
        return repository.getSearchQueriesByName(request)

    }

    suspend operator fun invoke(request: SearchQueryRequest): Flow<List<SearchQuery>> {
        return getSearchQueriesByName(request)
    }
}