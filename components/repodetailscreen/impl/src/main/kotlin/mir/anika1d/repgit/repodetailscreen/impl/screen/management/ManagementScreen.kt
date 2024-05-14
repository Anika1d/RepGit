package mir.anika1d.repgit.repodetailscreen.impl.screen.management


internal interface ManagementScreen {
    fun onBack()
    fun clearData()
    fun getAllInfoRep(owner: String, repo: String, onSuccess: () -> Unit, onError: (String?) -> Unit)
    fun download()
}