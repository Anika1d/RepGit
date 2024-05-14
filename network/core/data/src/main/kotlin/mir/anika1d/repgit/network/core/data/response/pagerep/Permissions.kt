package mir.anika1d.repgit.network.core.data.response.pagerep

import kotlinx.serialization.Serializable

@Serializable
data class Permissions(
    val admin: Boolean?,
    val maintain: Boolean?,
    val pull: Boolean?,
    val push: Boolean?,
    val triage: Boolean?
)