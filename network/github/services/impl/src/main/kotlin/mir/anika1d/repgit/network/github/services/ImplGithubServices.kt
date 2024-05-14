package mir.anika1d.repgit.network.github.services

import android.net.Uri
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import mir.anika1d.repgit.network.core.data.request.IssuesRequest
import mir.anika1d.repgit.network.core.data.request.RepositoryRequest
import mir.anika1d.repgit.network.core.data.request.SearchRequest
import mir.anika1d.repgit.network.core.data.request.UserRequest
import mir.anika1d.repgit.network.core.data.response.tokens.TokenDto
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientSecretPost
import net.openid.appauth.ResponseTypeValues
import net.openid.appauth.TokenRequest
import kotlin.coroutines.suspendCoroutine

class ImplGithubServices internal constructor(
    private val client: HttpClient,
    private val authService: AuthorizationService
) : IGithubServices {
    override suspend fun searchRepository(searchRequest: SearchRequest): HttpResponse {
        return client.get("search/repositories") {
            this.parameter("q", searchRequest.query)
            this.parameter("per_page", searchRequest.perPage)
            this.parameter("page", searchRequest.page)
            searchRequest.sort?.let {
                this.parameter("sort", searchRequest.sort)
                searchRequest.order?.let {
                    this.parameter("order", searchRequest.order)
                }
            }
        }
    }

    override suspend fun searchRepositoryByUser(searchRequest: SearchRequest): HttpResponse {
        return client.get("/users/${searchRequest.query}/repos") {
            this.parameter("per_page", searchRequest.perPage)
            this.parameter("page", searchRequest.page)
            searchRequest.sort?.let {
                this.parameter("sort", searchRequest.sort)
                searchRequest.order?.let {
                    this.parameter("order", searchRequest.order)
                }
            }
        }
    }

    override suspend fun searchUser(searchRequest: SearchRequest): HttpResponse {
        return client.get("search/users") {
            this.parameter("q", searchRequest.query)
            this.parameter("per_page", searchRequest.perPage)
            this.parameter("page", searchRequest.page)
            searchRequest.sort?.let {
                this.parameter("sort", searchRequest.sort)
                searchRequest.order?.let {
                    this.parameter("order", searchRequest.order)
                }
            }
        }
    }


    override suspend fun getRepository(repositoryRequest: RepositoryRequest): HttpResponse {
        return client.get("/repos/${repositoryRequest.nameOwner}/${repositoryRequest.nameRepository}")
    }

    override suspend fun getIssues(issuesRequest: IssuesRequest): HttpResponse {
        return client.get("/repos/${issuesRequest.nameOwner}/${issuesRequest.nameRepository}/issues")
    }

    override suspend fun getUser(userRequest: UserRequest): HttpResponse {
        return client.get(if (userRequest.name.isEmpty()) "/user" else "/users/${userRequest.name}")
    }


    override fun auth(
        redirectUrl: Uri, authUrl: Uri,
        tokenUrl: Uri,
        endSessionUrl: Uri,
        clientId: String,
        scope: String
    ): AuthorizationRequest.Builder {
        return AuthorizationRequest.Builder(
            AuthorizationServiceConfiguration(
                authUrl,
                tokenUrl,
                null,
                endSessionUrl,
            ),
            clientId,
            ResponseTypeValues.CODE,
            redirectUrl
        )
            .setScope(scope)
    }

    override suspend fun getTokens(
        tokenRequest: TokenRequest,
        appSecretKey: String
    ): TokenDto {
        return suspendCoroutine { continuation ->
            authService.performTokenRequest(
                tokenRequest,
                ClientSecretPost(appSecretKey)
            ) { response, ex ->
                when {
                    response != null -> {
                        val tokens = TokenDto(
                            accessToken = response.accessToken.orEmpty(),
                            refreshToken = response.refreshToken.orEmpty(),
                            idToken = response.idToken.orEmpty()
                        )
                        continuation.resumeWith(Result.success(tokens))
                    }

                    ex != null -> {
                        continuation.resumeWith(Result.failure(ex))
                    }

                    else -> error("unreachable")
                }
            }
        }
    }
}