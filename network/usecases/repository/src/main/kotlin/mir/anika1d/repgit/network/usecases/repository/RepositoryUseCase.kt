package mir.anika1d.repgit.network.usecases.repository

import kotlinx.coroutines.flow.Flow
import mir.anika1d.repgit.network.core.data.response.ResultState
import mir.anika1d.repgit.network.core.data.model.repository.RepositoryItem
import mir.anika1d.repgit.network.core.data.request.RepositoryRequest
import mir.anika1d.repgit.network.github.repositories.IGithubRepository

class RepositoryUseCase(private val repository: IGithubRepository) {

  private  suspend fun  getRepository(repositoryRequest: RepositoryRequest): Flow<ResultState<RepositoryItem>> {
        return repository.getRepository(repositoryRequest)
    }

    suspend operator fun invoke(repositoryRequest: RepositoryRequest): Flow<ResultState<RepositoryItem>> {
        return getRepository(repositoryRequest)
    }
}