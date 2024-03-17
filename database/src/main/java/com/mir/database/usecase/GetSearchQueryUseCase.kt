package com.mir.database.usecase

import com.mir.database.data.model.SearchQuery
import com.mir.database.data.request.SearchQueryRequest
import com.mir.database.repository.ISearchQueryRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

class GetSearchQueryUseCase( private val repository: ISearchQueryRepository):KoinComponent {

    suspend fun getSearchQueriesByName(request: SearchQueryRequest): Flow<List<SearchQuery>>
    {
        return   repository.getSearchQueriesByName(request)

    }

    suspend  operator fun invoke(request: SearchQueryRequest): Flow<List<SearchQuery>>
    {
        return getSearchQueriesByName(request)
    }
}