package mir.anika1d.repgit.ui.composable.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import mir.anika1d.core.network.connection.service.ConnectivityState

@Composable
fun ConnectionLayout(
    onAvailable: @Composable () -> Unit,
    onUnavailable: @Composable () -> Unit,
    connectEthernet: ConnectivityState
) {
    AnimatedVisibility(connectEthernet == ConnectivityState.Available)
    { onAvailable() }
    AnimatedVisibility(connectEthernet == ConnectivityState.Unavailable)
    { onUnavailable() }
}