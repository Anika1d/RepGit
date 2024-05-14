package mir.anika1d.repgit.database.usecase

import mir.anika1d.repgit.database.api.ISearchQueryRepository
import mir.anika1d.repgit.database.data.request.SearchQueryRequest


class InsertSearchQueryUseCase(private val repository: ISearchQueryRepository) {
    suspend fun insertSearchQuery(request: SearchQueryRequest) {
        repository.insertSearchQuery(request)
    }

    suspend operator fun invoke(request: SearchQueryRequest) {
        insertSearchQuery(request)
    }
}