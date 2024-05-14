package mir.anika1d.repgit.network.core.data.response.issues

import kotlinx.serialization.Serializable

@Serializable
data class IssuesItemResponse(
    val id: Long?=null,
    val url: String?=null,
    val reactions: Reactions?=null,
    val state: String?=null,
    val title: String?=null,
    val body: String?=null,
    val comments: Long?=null,
    val created_at: String?=null,
    val updated_at: String?=null,
    )