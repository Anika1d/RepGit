package mir.anika1d.repgit.core.data.model.user

import mir.anika1d.repgit.core.data.model.page.Page

class PageUser (
    override val incompleteResults: Boolean?,
    override val items: List<User>,
    override  val totalCount: Int?,
    override  val nextPage:String?,
): Page<User>