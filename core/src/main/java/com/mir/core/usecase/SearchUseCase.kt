package com.mir.core.usecase

import com.mir.core.data.model.repository.PageRepository
import com.mir.core.data.request.SearchRequest
import com.mir.core.data.response.ResultState
import com.mir.core.repository.ImplGithubRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SearchUseCase: KoinComponent {
    private val repository by inject<ImplGithubRepository>()

    suspend fun search(searchRequest: SearchRequest): Flow<ResultState<PageRepository>> {
       return repository.search(searchRequest)

    }
    suspend operator fun invoke(searchRequest: SearchRequest): Flow<ResultState<PageRepository>> {
       return search(searchRequest)
    }
}