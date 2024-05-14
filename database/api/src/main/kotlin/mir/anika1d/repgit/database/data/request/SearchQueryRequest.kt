package mir.anika1d.repgit.database.data.request

data class SearchQueryRequest(
    val name: String,
    val limit:Int?=null
)