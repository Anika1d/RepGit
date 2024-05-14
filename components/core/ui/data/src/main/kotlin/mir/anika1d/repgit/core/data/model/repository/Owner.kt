package mir.anika1d.repgit.core.data.model.repository

import kotlinx.serialization.Serializable
@Serializable
data class Owner (
    val avatarUrl:String,
    val name:String,
)