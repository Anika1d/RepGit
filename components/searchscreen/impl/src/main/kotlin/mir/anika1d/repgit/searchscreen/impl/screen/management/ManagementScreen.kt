package mir.anika1d.repgit.searchscreen.impl.screen.management

import mir.anika1d.repgit.core.data.model.search.SearchQuery
import mir.anika1d.repgit.core.data.state.SearchFilter

internal interface ManagementScreen {
    fun nextPage(onSuccess: () -> Unit, onError: (String?) -> Unit)
    fun navigationToDetails(owner: String, repositoryName: String)
    fun navigationToDownload()
    fun navigationToProfile(user: String, avatarUrl: String)
    fun logout()
    fun changeActiveSearch(b: Boolean)
    fun changeSearchValue(s: String)
    fun clearSearchQueries()
    fun insertSearchValue()
    fun search(onSuccess: () -> Unit, onError: (String?) -> Unit)
    fun onItemModeChange(filter: SearchFilter)
    fun onExpandedModeChange(b: Boolean)
    fun deleteSearchValue(searchQuery: SearchQuery)
}