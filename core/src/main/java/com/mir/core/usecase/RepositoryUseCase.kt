package com.mir.core.usecase

import com.mir.core.data.model.repository.RepositoryItem
import com.mir.core.data.request.RepositoryRequest
import com.mir.core.data.response.ResultState
import com.mir.core.repository.IGithubRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

class RepositoryUseCase(private val repository: IGithubRepository) : KoinComponent {

    suspend fun getRepository(repositoryRequest: RepositoryRequest): Flow<ResultState<RepositoryItem>> {
        return repository.getRepository(repositoryRequest)
    }

    suspend operator fun invoke(repositoryRequest: RepositoryRequest): Flow<ResultState<RepositoryItem>> {
        return getRepository(repositoryRequest)
    }
}