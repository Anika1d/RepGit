package com.mir.repgit.tools.network

import kotlinx.coroutines.flow.Flow

interface IEthernetConnectivityService {
    val connectivityState: Flow<ConnectivityState>
}
