package com.mir.database.usecase

import com.mir.database.data.request.SearchQueryRequest
import com.mir.database.repository.ISearchQueryRepository
import org.koin.core.component.KoinComponent

class DeleteSearchQueryUseCase(private val repository: ISearchQueryRepository) : KoinComponent {

    suspend fun deleteSearchQuery(request: SearchQueryRequest) {
        repository.deleteSearchQuery(request)
    }

    suspend operator fun invoke(request: SearchQueryRequest) {
        deleteSearchQuery(request)
    }
}