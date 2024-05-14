package mir.anika1d.repgit.network.core.data.response.issues

import kotlinx.serialization.Serializable

@Serializable
data class Label(
    val id: Int,
    val node_id: String,
    val url: String,
    val name: String,
    val description: String,
    val color: String,
    val default: Boolean
)