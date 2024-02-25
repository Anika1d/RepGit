package com.mir.core.data.response

import io.ktor.http.HttpStatusCode

sealed class ResultState<out T : Any> {
    data class Success<out T: Any>(val data: T?) : ResultState<T>()
    data class Error(val error: Exception, val statusCode: HttpStatusCode): ResultState<Nothing>()
    data class NetworkError(
        val error: com.mir.core.data.model.error.GithubError,
        val statusCode: HttpStatusCode
    ) : ResultState<Nothing>()
}