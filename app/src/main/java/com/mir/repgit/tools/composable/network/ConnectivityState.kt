package com.mir.repgit.tools.composable.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
@Composable
fun rememberConnectivityState(): State<ConnectivityState> {
    val context = LocalContext.current
    return produceState(initialValue = context.currentConnectivityState) {
        context.observeConnectivityAsFlow().collect { value = it }
    }
}


sealed class ConnectivityState {
    data object Available : ConnectivityState()
    data object Unavailable : ConnectivityState()
}

private val Context.currentConnectivityState: ConnectivityState
    get() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return getCurrentConnectivityState(connectivityManager)
    }

private fun getCurrentConnectivityState(
    connectivityManager: ConnectivityManager
): ConnectivityState {
    var connected: Boolean? = null
    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            connected = true
            super.onAvailable(network)
        }

        override fun onLost(network: Network) {
            connected = false
            super.onLost(network)
        }

        override fun onUnavailable() {
            connected = false
            super.onUnavailable()
        }
    }
    connectivityManager.registerDefaultNetworkCallback(networkCallback)
    return when (connected) {
        true -> ConnectivityState.Available
        false -> ConnectivityState.Unavailable
        null -> {
            val r = connectivityManager.activeNetwork
            if (r != null)
                ConnectivityState.Available
            else
                ConnectivityState.Unavailable
        }
    }

}

@ExperimentalCoroutinesApi
private fun Context.observeConnectivityAsFlow() = callbackFlow {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            trySend(ConnectivityState.Available)
            super.onAvailable(network)
        }

        override fun onLost(network: Network) {
            trySend(ConnectivityState.Unavailable)
            super.onLost(network)
        }
    }
    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()
    connectivityManager.registerNetworkCallback(networkRequest, callback)
    val currentState = getCurrentConnectivityState(connectivityManager)
    trySend(currentState)
    awaitClose {
        connectivityManager.unregisterNetworkCallback(callback)
    }
}


