package mir.anika1d.repgit.network.usecases.issues

import kotlinx.coroutines.flow.Flow
import mir.anika1d.repgit.network.core.data.response.ResultState
import mir.anika1d.repgit.network.core.data.model.issues.Issues
import mir.anika1d.repgit.network.core.data.request.IssuesRequest
import mir.anika1d.repgit.network.github.repositories.IGithubRepository


class IssuesUseCase(private val repository: IGithubRepository)  {

   private suspend fun getIssues(issuesRequest: IssuesRequest): Flow<ResultState<List<Issues>>> {
        return repository.getIssues(issuesRequest)
    }

    suspend operator fun invoke(issuesRequest: IssuesRequest): Flow<ResultState<List<Issues>>> {
        return getIssues(issuesRequest)
    }
}