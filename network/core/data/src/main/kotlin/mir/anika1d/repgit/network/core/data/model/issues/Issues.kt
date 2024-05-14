package mir.anika1d.repgit.network.core.data.model.issues

import mir.anika1d.repgit.network.core.data.response.issues.Reactions

data class Issues(
    val body: String,
    val comments: Long,
    val id: Long,
    val state: String,
    val title: String,
    val updatedAt: String,
    val createdAt:String,
    val url: String,
    val reactions: Reactions?
){

}