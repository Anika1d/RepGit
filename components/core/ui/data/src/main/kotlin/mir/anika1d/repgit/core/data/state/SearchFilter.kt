package mir.anika1d.repgit.core.data.state

enum class SearchFilter(val value: String) {
    REPOS_ALL("repall"),
    USERS("user"),
    REPOS_OWNER("owner");

    companion object {
        fun of(s: String): SearchFilter {
            return when (s.lowercase()) {
                "repall" -> REPOS_ALL
                "user" -> USERS
                "owner" -> REPOS_OWNER
                else -> throw Exception("Undefine search filter")
            }
        }
    }
}