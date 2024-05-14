package mir.anika1d.repgit.core.data.state

sealed class LoadState {
    data object SHOW: LoadState()
    data object HIDE: LoadState()

}