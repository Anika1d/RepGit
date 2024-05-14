package mir.anika1d.repgit.network.core.data.response.pagerep

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class License(
    val key: String?,
    val name: String?,
    @SerialName("html_url") val htmlUrl: String?=null,
    @SerialName("node_id")  val nodeId: String?,
    @SerialName("spdx_id")  val spdxId: String?,
    val url: String?
)