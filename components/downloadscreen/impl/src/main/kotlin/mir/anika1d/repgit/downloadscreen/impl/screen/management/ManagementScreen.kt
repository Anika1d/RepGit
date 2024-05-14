package mir.anika1d.repgit.downloadscreen.impl.screen.management


internal interface ManagementScreen {
    fun onBack()
    fun getLocalRepositories()
   fun navigationToDetails(first: String, second: String)
}