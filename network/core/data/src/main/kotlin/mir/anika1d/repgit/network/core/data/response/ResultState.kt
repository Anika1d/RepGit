package mir.anika1d.repgit.network.core.data.response

import io.ktor.http.HttpStatusCode
import mir.anika1d.repgit.network.core.data.model.error.GithubError

sealed class ResultState<out T : Any> {
    data class Success<out T : Any>(val data: T?) : ResultState<T>()
    data class Error<out T : Any>(val error: Exception, val statusCode: HttpStatusCode) :
        ResultState<T>() {
        val statusCode2Int = statusCode.value
    }

    data class NetworkError<out T : Any>(
        val error: GithubError,
        val statusCode: HttpStatusCode,
    ) : ResultState<T>() {
        val statusCode2Int = statusCode.value
    }
}