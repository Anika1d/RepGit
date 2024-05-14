package mir.anika1d.repgit.core.data.model.repository

import mir.anika1d.repgit.core.data.model.page.Page


data class PageRepository(
    override val incompleteResults: Boolean?,
    override val items: List<RepositoryItem>,
    override val totalCount: Int?,
    override val nextPage: String?,
) : Page<RepositoryItem>