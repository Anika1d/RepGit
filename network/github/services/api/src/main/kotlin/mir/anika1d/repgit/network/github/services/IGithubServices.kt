package mir.anika1d.repgit.network.github.services

import android.net.Uri
import io.ktor.client.statement.HttpResponse
import mir.anika1d.repgit.network.core.data.request.IssuesRequest
import mir.anika1d.repgit.network.core.data.request.RepositoryRequest
import mir.anika1d.repgit.network.core.data.request.SearchRequest
import mir.anika1d.repgit.network.core.data.request.UserRequest
import mir.anika1d.repgit.network.core.data.response.tokens.TokenDto
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.TokenRequest


interface IGithubServices : IGithubSearchServices,
    IGithubRepositoryServices, IGithubIssuesServices,IGithubUserServices,
    IGithubAuthServices

interface IGithubSearchServices {
    suspend fun searchRepository(searchRequest: SearchRequest): HttpResponse
    suspend fun searchRepositoryByUser(searchRequest: SearchRequest): HttpResponse
    suspend fun searchUser(searchRequest: SearchRequest): HttpResponse

}

interface IGithubRepositoryServices {
    suspend fun getRepository(repositoryRequest: RepositoryRequest): HttpResponse
}

interface IGithubIssuesServices {
    suspend fun getIssues(issuesRequest: IssuesRequest): HttpResponse
}
interface IGithubUserServices {
    suspend fun getUser(userRequest: UserRequest): HttpResponse
}



interface IGithubAuthServices {
     fun auth(
        redirectUrl: Uri, authUrl: Uri,
        tokenUrl: Uri,
        endSessionUrl: Uri,
        clientId:String,
        scope:String
    ): AuthorizationRequest.Builder

    suspend fun getTokens(tokenRequest: TokenRequest,
                          appSecretKey:String): TokenDto
}