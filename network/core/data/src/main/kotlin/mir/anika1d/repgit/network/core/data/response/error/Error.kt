package mir.anika1d.repgit.network.core.data.response.error

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Error(
   @SerialName("code") val code: String,
   @SerialName("field") val `field`: String,
   @SerialName("resource") val resource: String
)
