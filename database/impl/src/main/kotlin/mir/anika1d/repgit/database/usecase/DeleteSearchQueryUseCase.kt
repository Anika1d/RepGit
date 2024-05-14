package mir.anika1d.repgit.database.usecase

import mir.anika1d.repgit.database.api.ISearchQueryRepository
import mir.anika1d.repgit.database.data.request.SearchQueryRequest


class DeleteSearchQueryUseCase(private val repository: ISearchQueryRepository) {

    suspend fun deleteSearchQuery(request: SearchQueryRequest) {
        repository.deleteSearchQuery(request)
    }

    suspend operator fun invoke(request: SearchQueryRequest) {
        deleteSearchQuery(request)
    }
}