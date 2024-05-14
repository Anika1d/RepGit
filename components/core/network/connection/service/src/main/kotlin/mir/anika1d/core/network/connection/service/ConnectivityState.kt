package mir.anika1d.core.network.connection.service

sealed class ConnectivityState {
    data object Available : ConnectivityState()
    data object Unavailable : ConnectivityState()
    data object Undefined : ConnectivityState()
}