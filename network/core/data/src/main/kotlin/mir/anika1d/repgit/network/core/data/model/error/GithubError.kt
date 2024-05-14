package mir.anika1d.repgit.network.core.data.model.error

import mir.anika1d.repgit.network.core.data.response.error.Error
import kotlinx.serialization.Serializable


@Serializable
class GithubError(val errors: List<Error>, val message: String) {
}