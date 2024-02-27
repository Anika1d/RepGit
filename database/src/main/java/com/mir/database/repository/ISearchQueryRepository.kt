package com.mir.database.repository

import com.mir.database.data.model.SearchQuery
import com.mir.database.data.request.SearchQueryRequest
import kotlinx.coroutines.flow.Flow

interface ISearchQueryRepository {
    suspend fun getSearchQueriesByName(request: SearchQueryRequest): Flow<List<SearchQuery>>
    suspend fun insertSearchQuery(request: SearchQueryRequest)
    suspend fun deleteSearchQuery(request: SearchQueryRequest)
}