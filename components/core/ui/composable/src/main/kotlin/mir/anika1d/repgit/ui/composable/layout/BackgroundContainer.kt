package mir.anika1d.repgit.ui.composable.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import mir.anika1d.core.network.connection.service.ConnectivityState
import mir.anika1d.core.resources.AppResources
import mir.anika1d.repgit.core.ui.theme.LocalPallet
import mir.anika1d.repgit.core.ui.theme.RepGitPallet

@Composable
fun BackgroundContainer(
    modifier: Modifier,
    contentAlignment: Alignment = Alignment.Center,
    propagateMinConstraints: Boolean = false,
    connectEthernet: ConnectivityState = ConnectivityState.Available,
    checkEthernetView: @Composable () -> Unit = {
        ConnectionLayout(
            onAvailable = { },
            onUnavailable = {
                Box(modifier = Modifier
                    .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                    content = {
                        Icon(
                            painter = painterResource(id = AppResources.Drawable.NO_ETHERNET.id),
                            contentDescription = "no_ethernet",
                            tint = RepGitPallet().mint
                        )
                    })
            },
            connectEthernet = connectEthernet
        )
    },
    content: @Composable BoxScope.() -> Unit,
) {
    val localPallet= LocalPallet.current
    Box(
        modifier = modifier
            .background(
                Color(0xFF010A43)
            )
            .drawBehind {
                drawCircle(
                    color =  localPallet.lineBackgroundColor,
                    radius = size.width - center.x,
                    style = Stroke(3f),
                    center = Offset(center.x, center.y - center.y / 4),
                    alpha = 0.1f
                )
                drawCircle(
                    color = localPallet.lineBackgroundColor,
                    radius = size.width - center.x - center.x / 4f,
                    style = Stroke(width = 2f),
                    center = Offset(center.x, center.y - center.y / 3),
                    alpha = 0.1f
                )
                drawCircle(
                    color =localPallet. lineBackgroundColor,
                    radius = size.width / 5f,
                    style = Stroke(width = 1f),
                    center = Offset(center.x, center.y + center.y / 2f),
                    alpha = 0.1f
                )
                drawArc(
                    color = localPallet.lineBackgroundColor, alpha = 0.1f,
                    style = Stroke(width = 1f),
                    useCenter = false,
                    startAngle = 100f,
                    sweepAngle = 140f,
                    topLeft = Offset(center.x - center.x / 8f, center.y + center.y / 2),
                    size = Size(center.x / 4f, center.y / 2f)
                )
                drawArc(
                    color = localPallet.lineBackgroundColor, alpha = 0.1f,
                    style = Stroke(width = 1f),
                    useCenter = false,
                    startAngle = 220f,
                    sweepAngle = 360f,
                    topLeft = Offset(center.x - center.x / 8f, center.y + center.y / 2),
                    size = Size(center.x / 4f, center.y / 2f)
                )
            },
        contentAlignment = contentAlignment,
        propagateMinConstraints = propagateMinConstraints,
        content = {
            checkEthernetView.invoke()
            content.invoke(this)
        },
    )

}

@Preview(showBackground = true)
@Composable
fun PreviewBackgroundContainer() {
    BackgroundContainer(modifier = Modifier.fillMaxSize(),
        connectEthernet = ConnectivityState.Undefined, content = {})
}