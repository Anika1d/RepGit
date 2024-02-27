package com.mir.database.usecase

import com.mir.database.data.request.SearchQueryRequest
import com.mir.database.repository.ImplSearchQueryRepository
import org.koin.core.component.KoinComponent

class InsertSearchQueryUseCase (private val repository: ImplSearchQueryRepository) : KoinComponent {
    suspend fun insertSearchQuery(request: SearchQueryRequest) {
        repository.insertSearchQuery(request)
    }

    suspend operator fun invoke(request: SearchQueryRequest) {
       insertSearchQuery(request)
    }
}