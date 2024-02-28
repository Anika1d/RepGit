package com.mir.database.repository

import com.mir.database.dao.SearchQueryDao
import com.mir.database.data.model.SearchQuery
import com.mir.database.data.request.SearchQueryRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class ImplSearchQueryRepository(
    private val dao: SearchQueryDao
) : ISearchQueryRepository {
    override suspend fun getSearchQueriesByName(request: SearchQueryRequest): Flow<List<SearchQuery>> {
        return (if (request.limit != null)
            dao.getSearchQueriesByName(request.name, request.limit)
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