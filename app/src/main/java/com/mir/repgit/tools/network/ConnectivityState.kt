package com.mir.repgit.tools.network
sealed class ConnectivityState {
    data object Available : ConnectivityState()
    data object Unavailable : ConnectivityState()
    data object Undefined : ConnectivityState()

}