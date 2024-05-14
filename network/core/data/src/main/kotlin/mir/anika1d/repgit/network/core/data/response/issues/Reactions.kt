package mir.anika1d.repgit.network.core.data.response.issues

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Reactions(
 @SerialName("+1")   val plusOne: Int,
 @SerialName("-1")   val minusOne: Int,
    val confused: Int,
    val eyes: Int,
    val heart: Int,
    val hooray: Int,
    val laugh: Int,
    val rocket: Int,
    val total_count: Int,
    val url: String
)