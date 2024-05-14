package mir.anika1d.repgit.database.api

import kotlinx.coroutines.flow.Flow
import mir.anika1d.repgit.database.data.model.SearchQuery
import mir.anika1d.repgit.database.data.request.SearchQueryRequest

interface ISearchQueryRepository{
    suspend fun getSearchQueriesByName(request: SearchQueryRequest): Flow<List<SearchQuery>>
    suspend fun insertSearchQuery(request: SearchQueryRequest)
    suspend fun deleteSearchQuery(request: SearchQueryRequest)
}