package mir.anika1d.repgit.network.core.data.model.repository


data class RepositoryItem(
    val owner: Owner,
    val description: String?,
    val name: String,
    val starCount: String,
    val watchersCount: String,
    val forkCount: String,
    val issuesCount: String,
    val url: String,
    val downloadUrl: String,
    val language: String,
    val defaultBranch:String,
    val  updatedAt:String,
)