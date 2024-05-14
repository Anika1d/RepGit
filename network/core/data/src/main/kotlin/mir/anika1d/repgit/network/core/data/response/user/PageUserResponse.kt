package mir.anika1d.repgit.network.core.data.response.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageUserResponse(
    @SerialName("incomplete_results") val incompleteResults: Boolean,
    @SerialName("items") val items: List<UserResponse>,
    @SerialName("total_count") val totalCount: Int
)