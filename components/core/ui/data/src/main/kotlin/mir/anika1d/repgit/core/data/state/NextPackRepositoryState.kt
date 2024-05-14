package mir.anika1d.repgit.core.data.state

sealed class NextPackRepositoryState {
    data object POSSIBLE: NextPackRepositoryState()
    data object IMPOSSIBLE: NextPackRepositoryState()
    data object LOAD: NextPackRepositoryState()

}