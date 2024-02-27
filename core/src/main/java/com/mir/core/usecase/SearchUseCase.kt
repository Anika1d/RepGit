package com.mir.core.usecase

import com.mir.core.data.model.repository.PageRepository
import com.mir.core.data.request.SearchRequest
import com.mir.core.data.response.ResultState
import com.mir.core.repository.ImplGithubRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

class SearchUseCase(private val repository: ImplGithubRepository) : KoinComponent {
    suspend fun search(searchRequest: SearchRequest): Flow<ResultState<PageRepository>> {
        return repository.search(searchRequest)

    }

    suspend operator fun invoke(searchRequest: SearchRequest): Flow<ResultState<PageRepository>> {
        return search(searchRequest)
    }
}