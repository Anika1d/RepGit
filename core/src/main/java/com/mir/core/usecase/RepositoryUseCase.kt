package com.mir.core.usecase

import com.mir.core.data.model.repository.RepositoryItem
import com.mir.core.data.request.RepositoryRequest
import com.mir.core.data.response.ResultState
import com.mir.core.repository.ImplGithubRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RepositoryUseCase : KoinComponent {
    private val repository by inject<ImplGithubRepository>()

    suspend fun getRepository(repositoryRequest: RepositoryRequest): Flow<ResultState<RepositoryItem>> {
        return repository.getRepository(repositoryRequest)
    }

    suspend operator fun invoke(repositoryRequest: RepositoryRequest): Flow<ResultState<RepositoryItem>> {
        return getRepository(repositoryRequest)
    }
}