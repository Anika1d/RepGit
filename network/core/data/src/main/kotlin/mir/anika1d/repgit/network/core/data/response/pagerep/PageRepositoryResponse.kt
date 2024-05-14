package mir.anika1d.repgit.network.core.data.response.pagerep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageRepositoryResponse(
    @SerialName("incomplete_results") val incompleteResults: Boolean?,
    @SerialName("items") val items: List<RepositoryItemResponse>,
    @SerialName("total_count") val totalCount: Int?
)