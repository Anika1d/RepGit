package mir.anika1d.repgit.profilescreen.impl.screen.management

internal interface ManagementScreen {
    fun nextPage(onSuccess: () -> Unit, onError: (String?) -> Unit)
    fun getRepositories(onSuccess: () -> Unit, onError: (String?) -> Unit)
    fun navigationToDetails(owner: String, repositoryName: String)
    fun logout()
    fun onBack()
}