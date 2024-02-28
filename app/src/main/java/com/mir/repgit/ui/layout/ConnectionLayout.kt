package com.mir.repgit.ui.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import com.mir.repgit.tools.network.ConnectivityState

@Composable
fun ConnectionLayout(
    onAvailable: @Composable () -> Unit,
    onUnavailable: @Composable () -> Unit,
    connectEthernet: ConnectivityState
) {
    AnimatedVisibility(connectEthernet == ConnectivityState.Available)
    { onAvailable.invoke() }
    AnimatedVisibility(connectEthernet != ConnectivityState.Available)
    { onUnavailable.invoke() }
}