package mir.anika1d.repgit.network.core.data.response.issues

import kotlinx.serialization.Serializable

@Serializable
data class Milestone(
    val url: String,
    val html_url: String,
    val labels_url: String,
    val id: Int,
    val node_id: String,
    val number: Int,
    val state: String,
    val title: String,
    val description: String,
    val creator: User,
    val open_issues: Int,
    val closed_issues: Int,
    val created_at: String,
    val updated_at: String,
    val closed_at: String?,
    val due_on: String
)