package com.mir.repgit.tools

sealed class LoadState {
    data object SHOW:LoadState()
    data object HIDE:LoadState()

}