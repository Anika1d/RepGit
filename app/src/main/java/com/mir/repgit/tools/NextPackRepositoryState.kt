package com.mir.repgit.tools

sealed class NextPackRepositoryState {
    data object POSSIBLE:NextPackRepositoryState()
    data object IMPOSSIBLE:NextPackRepositoryState()
    data object LOAD:NextPackRepositoryState()

}