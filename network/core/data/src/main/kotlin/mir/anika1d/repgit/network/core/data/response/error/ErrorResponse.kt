package mir.anika1d.repgit.network.core.data.response.error

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerialName("documentation_url") val documentationUrl: String,
    @SerialName("errors") val errors: List<Error>? = listOf(),
    @SerialName("message") val message: String
)
/**
 * {   "message":"Validation Failed",
 *     "errors":[{"resource":"Search","field":"q","code":"missing"}],
 *     "documentation_url":"https://docs.github.com/v3/search"}

 * { "message":"Not Found",
 * "documentation_url":"https://docs.github.com/rest/repos/repos#list-repositories-for-a-user"}
 */