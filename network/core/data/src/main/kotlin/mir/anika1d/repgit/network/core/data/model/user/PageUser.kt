package mir.anika1d.repgit.network.core.data.model.user

import mir.anika1d.repgit.network.core.data.model.page.Page

data class PageUser (
    override val nextPage: String?,
    override val incompleteResults: Boolean?,
    override val items: List<User>,
    override val totalCount: Int?
): Page<User>