package mir.anika1d.repgit.database.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import mir.anika1d.repgit.database.api.ISearchQueryRepository
import mir.anika1d.repgit.database.dao.SearchQueryDao
import mir.anika1d.repgit.database.data.model.SearchQuery
import mir.anika1d.repgit.database.data.request.SearchQueryRequest

class ImplSearchQueryRepository(
    private val dao: SearchQueryDao
) : ISearchQueryRepository {
    override suspend fun getSearchQueriesByName(request: SearchQueryRequest): Flow<List<SearchQuery>> {
        return (if (request.limit != null)
            dao.getSearchQueriesByName(request.name, request.limit!!)
        else dao.getSearchQueriesByName(request.name)).map {
            it.map { sr -> SearchQuery(name = sr.name) }
        }.flowOn(Dispatchers.IO).distinctUntilChanged()
    }

    override suspend fun insertSearchQuery(request: SearchQueryRequest) {
        dao.insert(name=request.name)
    }

    override suspend fun deleteSearchQuery(request: SearchQueryRequest) {
        dao.deleteByName(request.name)
    }
}