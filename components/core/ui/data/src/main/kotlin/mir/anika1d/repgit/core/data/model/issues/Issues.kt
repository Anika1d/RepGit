package mir.anika1d.repgit.core.data.model.issues


data class Issues(
    val body: String,
    val comments: Long,
    val id: Long,
    val state: String,
    val title: String,
    val updatedAt: String,
    val createdAt:String,
    val url: String,
)