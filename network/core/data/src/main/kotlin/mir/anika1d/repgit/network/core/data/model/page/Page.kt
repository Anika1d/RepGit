package mir.anika1d.repgit.network.core.data.model.page

interface Page<T> {
    val incompleteResults: Boolean?
    val items: List<T>
    val totalCount: Int?
    val nextPage: String?
}