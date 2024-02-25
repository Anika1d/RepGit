package com.mir.core.repository

import android.util.Log
import com.mir.core.data.model.repository.PageRepository
import com.mir.core.data.request.SearchRequest
import com.mir.core.data.response.ResultState
import com.mir.core.data.response.error.ErrorResponse
import com.mir.core.data.response.pagerep.PageResponse
import com.mir.core.mapper.Mapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.cancellation.CancellationException

class ImplGithubRepository : IGithubRepository, KoinComponent {
    private val client: HttpClient by inject<HttpClient>()
    override suspend fun search(searchRequest: SearchRequest): Flow<ResultState<PageRepository>> {
        return flow {
            try {
                val s = client.get("search/repositories") {
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
                Log.println(Log.INFO, "infos", s.toString())
                if (s.status == HttpStatusCode.OK) {
                    val nextPage =
                        s.headers["Link"]?.split(",")?.find { it.contains("rel=\"next\"") }
                    val rep = s.body<PageResponse>()
                    emit(
                        ResultState.Success(
                            Mapper.mapRepositoryResponseToAppModel(
                                pageResponse = rep,
                                nextPage = nextPage?.substring(
                                    nextPage.indexOf("<") + 1,
                                    nextPage.indexOf(">")
                                )

                            )
                        )
                    )
                } else
                    emit(
                        ResultState.NetworkError(
                            Mapper.mapErrorResponseToAppModel(s.body<ErrorResponse>()),
                            s.status
                        )
                    )

            }
            catch (ce: CancellationException) {
                Log.e("infos",ce.stackTrace.toString())
                emit(ResultState.Error(ce, HttpStatusCode.TooManyRequests))
                throw ce
            }
            catch (e: Exception) {
                emit(ResultState.Error(e, HttpStatusCode.BadRequest))
            }
        }.flowOn(Dispatchers.IO)
    }
}