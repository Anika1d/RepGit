package mir.anika1d.repgit.network.github.repositories


import kotlinx.coroutines.flow.Flow
import mir.anika1d.repgit.network.core.data.model.issues.Issues
import mir.anika1d.repgit.network.core.data.model.repository.PageRepository
import mir.anika1d.repgit.network.core.data.model.repository.RepositoryItem
import mir.anika1d.repgit.network.core.data.model.user.PageUser
import mir.anika1d.repgit.network.core.data.model.user.User
import mir.anika1d.repgit.network.core.data.request.IssuesRequest
import mir.anika1d.repgit.network.core.data.request.RepositoryRequest
import mir.anika1d.repgit.network.core.data.request.SearchRequest
import mir.anika1d.repgit.network.core.data.request.UserRequest
import mir.anika1d.repgit.network.core.data.response.ResultState
import mir.anika1d.repgit.network.core.data.response.tokens.TokenDto
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.TokenRequest

interface IGithubRepository : IGithubSearchRepository, IGithubRepositoryItemRepository,
    IGithubIssuesRepository, IGithubAuthRepository, IGithubUserRepository

interface IGithubSearchRepository {
    suspend fun searchRepository(searchRequest: SearchRequest): Flow<ResultState<PageRepository>>
    suspend fun searchRepositoryByUser(searchRequest: SearchRequest): Flow<ResultState<PageRepository>>
    suspend fun searchUser(searchRequest: SearchRequest): Flow<ResultState<PageUser>>

}

interface IGithubRepositoryItemRepository {
    suspend fun getRepository(repositoryRequest: RepositoryRequest): Flow<ResultState<RepositoryItem>>
}

interface IGithubIssuesRepository {
    suspend fun getIssues(issuesRequest: IssuesRequest): Flow<ResultState<List<Issues>>>
}

interface IGithubUserRepository {
    suspend fun getUser(userRequest: UserRequest): Flow<ResultState<User>>
}

interface IGithubAuthRepository {
    fun auth(): AuthorizationRequest
    suspend fun getTokens(tokenRequest: TokenRequest): TokenDto
}