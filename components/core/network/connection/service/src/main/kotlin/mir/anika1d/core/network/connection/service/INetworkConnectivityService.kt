package mir.anika1d.core.network.connection.service

import kotlinx.coroutines.flow.Flow


interface INetworkConnectivityService {
    val connectivityState: Flow<ConnectivityState>
}
