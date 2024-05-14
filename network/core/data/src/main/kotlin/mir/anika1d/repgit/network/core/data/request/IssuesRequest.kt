package mir.anika1d.repgit.network.core.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IssuesRequest(
    @SerialName("name_owner") val nameOwner:String,
    @SerialName("name") val nameRepository:String
)