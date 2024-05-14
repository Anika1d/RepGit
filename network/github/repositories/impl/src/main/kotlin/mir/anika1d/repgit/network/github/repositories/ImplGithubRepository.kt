package mir.anika1d.repgit.network.github.repositories

import android.net.Uri
import android.util.Log
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import mir.anika1d.repgit.network.core.data.mapper.Mapper
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
import mir.anika1d.repgit.network.core.data.response.error.ErrorResponse
import mir.anika1d.repgit.network.core.data.response.issues.IssuesItemResponse
import mir.anika1d.repgit.network.core.data.response.pagerep.PageRepositoryResponse
import mir.anika1d.repgit.network.core.data.response.pagerep.RepositoryItemResponse
import mir.anika1d.repgit.network.core.data.response.tokens.TokenDto
import mir.anika1d.repgit.network.core.data.response.user.PageUserResponse
import mir.anika1d.repgit.network.core.data.response.user.UserResponse
import mir.anika1d.repgit.network.github.repositories.impl.BuildConfig
import mir.anika1d.repgit.network.github.services.IGithubServices
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.TokenRequest
import org.koin.core.component.KoinComponent
import kotlin.coroutines.cancellation.CancellationException

class ImplGithubRepository internal constructor(private val services: IGithubServices) :
    IGithubRepository, KoinComponent {

    override suspend fun searchRepositoryByUser(searchRequest: SearchRequest): Flow<ResultState<PageRepository>> {
        return flow {
            try {
                val s = services.searchRepositoryByUser(searchRequest)
                Log.println(Log.INFO, "infos", s.toString())
                if (s.status == HttpStatusCode.OK) {
                    val nextPage =
                        s.headers["Link"]?.split(",")?.find { it.contains("rel=\"next\"") }
                    val rep = s.body<List<RepositoryItemResponse>>()
                    emit(
                        ResultState.Success(
                            Mapper.mapRepositoryListToAppModel(
                                rep = rep, nextPage = nextPage?.substring(
                                    nextPage.indexOf("<") + 1, nextPage.indexOf(">")
                                )
                            )
                        )
                    )

                } else emit(
                    ResultState.NetworkError(
                        Mapper.mapErrorResponseToAppModel(s.body<ErrorResponse>()), s.status
                    )
                )

            } catch (ce: CancellationException) {
                Log.e("infos", ce.stackTrace.toString())
                emit(ResultState.Error(ce, HttpStatusCode.BadRequest))
            } catch (e: Exception) {
                emit(ResultState.Error(e, HttpStatusCode.BadRequest))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun searchRepository(searchRequest: SearchRequest): Flow<ResultState<PageRepository>> {
        return flow {
            try {
                val s = services.searchRepository(searchRequest)
                Log.println(Log.INFO, "infos", s.toString())
                if (s.status == HttpStatusCode.OK) {
                    val nextPage =
                        s.headers["Link"]?.split(",")?.find { it.contains("rel=\"next\"") }
                    val rep = s.body<PageRepositoryResponse>()
                    emit(
                        ResultState.Success(
                            Mapper.mapPageRepositoryResponseToAppModel(
                                pageResponse = rep, nextPage = nextPage?.substring(
                                    nextPage.indexOf("<") + 1, nextPage.indexOf(">")
                                )

                            )
                        )
                    )
                } else emit(
                    ResultState.NetworkError(
                        Mapper.mapErrorResponseToAppModel(s.body<ErrorResponse>()), s.status
                    )
                )

            } catch (ce: CancellationException) {
                Log.e("infos", ce.stackTrace.toString())
                emit(ResultState.Error(ce, HttpStatusCode.BadRequest))
            } catch (e: Exception) {
                emit(ResultState.Error(e, HttpStatusCode.BadRequest))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getRepository(repositoryRequest: RepositoryRequest): Flow<ResultState<RepositoryItem>> {
        return flow {
            try {
                val s = services.getRepository(repositoryRequest)


                Log.println(Log.INFO, "infos", s.toString())
                if (s.status == HttpStatusCode.OK) {

                    val rep = s.body<RepositoryItemResponse>()
                    emit(
                        ResultState.Success(
                            Mapper.mapRepositoryResponseToAppModel(rep)
                        )
                    )
                } else emit(
                    ResultState.NetworkError(
                        Mapper.mapErrorResponseToAppModel(s.body<ErrorResponse>()), s.status
                    )
                )

            } catch (ce: CancellationException) {
                Log.e("infos", ce.stackTrace.toString())
                emit(ResultState.Error(ce, HttpStatusCode.TooManyRequests))
                throw ce
            } catch (e: Exception) {
                emit(ResultState.Error(e, HttpStatusCode.BadRequest))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getIssues(issuesRequest: IssuesRequest): Flow<ResultState<List<Issues>>> {
        return flow {
            try {
                val s = services.getIssues(issuesRequest)
                Log.println(Log.INFO, "infos", s.toString())
                if (s.status == HttpStatusCode.OK) {
                    val issues = s.body<List<IssuesItemResponse>>()
                    emit(
                        ResultState.Success(Mapper.mapIssuesResponseToAppModel(issues))
                    )
                } else emit(
                    ResultState.NetworkError(
                        Mapper.mapErrorResponseToAppModel(s.body<ErrorResponse>()), s.status
                    )
                )

            } catch (ce: CancellationException) {
                Log.e("infos", ce.stackTrace.toString())
                emit(ResultState.Error(ce, HttpStatusCode.TooManyRequests))
                throw ce
            } catch (e: Exception) {
                emit(ResultState.Error(e, HttpStatusCode.BadRequest))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun auth(): AuthorizationRequest {
        return services.auth(
            redirectUrl = Uri.parse(BuildConfig.GITHUB_APP_CALLBACK_URL),
            authUrl = Uri.parse(BuildConfig.GITHUB_APP_AUTH_URL),
            tokenUrl = Uri.parse(BuildConfig.GITHUB_APP_TOKEN_URL),
            endSessionUrl = Uri.parse(BuildConfig.GITHUB_APP_END_SESSION_URL),
            clientId = BuildConfig.GITHUB_APP_CLIENT_ID,
            scope = BuildConfig.GITHUB_APP_SCOPE_WORKING
        ).build()


    }

    override suspend fun getTokens(tokenRequest: TokenRequest): TokenDto =
        services.getTokens(tokenRequest, BuildConfig.GITHUB_APP_SECRET_KEY)

    override suspend fun getUser(userRequest: UserRequest): Flow<ResultState<User>> {
        return flow {
            try {
                val s = services.getUser(userRequest)
                if (s.status == HttpStatusCode.OK) {
                    val user = s.body<UserResponse>()
                    emit(
                        ResultState.Success(Mapper.mapUserResponseToAppModel(user))
                    )
                } else emit(
                    ResultState.NetworkError(
                        Mapper.mapErrorResponseToAppModel(s.body<ErrorResponse>()), s.status
                    )
                )
            } catch (ce: CancellationException) {
                Log.e("infos", ce.stackTrace.toString())
                emit(ResultState.Error(ce, HttpStatusCode.TooManyRequests))
                throw ce
            } catch (e: Exception) {
                emit(ResultState.Error(e, HttpStatusCode.BadRequest))
            }

        }
    }

    override suspend fun searchUser(searchRequest: SearchRequest): Flow<ResultState<PageUser>> {
        return flow {
            try {
                val s = services.searchUser(searchRequest)
                if (s.status == HttpStatusCode.OK) {
                    val user = s.body<PageUserResponse>()
                    val nextPage =
                        s.headers["Link"]?.split(",")?.find { it.contains("rel=\"next\"") }
                    emit(
                        ResultState.Success(Mapper.mapPageUserResponseToAppModel(user, nextPage))
                    )
                } else emit(
                    ResultState.NetworkError(
                        Mapper.mapErrorResponseToAppModel(s.body<ErrorResponse>()), s.status
                    )
                )
            } catch (ce: CancellationException) {
                Log.e("infos", ce.stackTrace.toString())
                emit(ResultState.Error(ce, HttpStatusCode.TooManyRequests))
                throw ce
            } catch (e: Exception) {
                emit(ResultState.Error(e, HttpStatusCode.BadRequest))
            }

        }
    }

}