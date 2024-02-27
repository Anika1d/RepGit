package com.mir.repgit.ui.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.mir.repgit.tools.composable.network.ConnectivityState
import com.mir.repgit.tools.composable.network.rememberConnectivityState
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun ConnectionLayout(
    onAvailable: @Composable () -> Unit,
    onUnavailable: @Composable () -> Unit
) {
    val connectEthernet by rememberConnectivityState()
    AnimatedVisibility(connectEthernet == ConnectivityState.Available)
    { onAvailable.invoke() }
    AnimatedVisibility(connectEthernet != ConnectivityState.Available)
    { onUnavailable.invoke() }
}