package mir.anika1d.repgit.downloadscreen.impl.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mir.anika1d.core.network.connection.service.ConnectivityState
import mir.anika1d.core.resources.AppResources
import mir.anika1d.repgit.core.ui.theme.LocalPallet
import mir.anika1d.repgit.downloadscreen.impl.screen.management.ManagementScreen
import mir.anika1d.repgit.ui.composable.layout.BackgroundContainer
import mir.anika1d.repgit.ui.composable.layout.ItemRepShotInfo

@Composable
internal fun Screen(
    repositories: List<Pair<String, String>>,
    managementScreen: ManagementScreen
) {
    val localPallet = LocalPallet.current
    LaunchedEffect(key1 = Unit) {
        managementScreen.getLocalRepositories()
    }
    BackgroundContainer(
        modifier = Modifier.fillMaxSize(),
        checkEthernetView = {}, connectEthernet = ConnectivityState.Available
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawLine(
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            color = localPallet.mint,
                            alpha = 1f,
                            strokeWidth = 2.dp.toPx()
                        )
                    },
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(modifier = Modifier, onClick = {
                    managementScreen.onBack()
                }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "arrow_back",
                        tint = localPallet.dirtyWhite
                    )
                }
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = AppResources.Values.Strings.DOWNLOAD_REPOSITORIES.id),
                    textAlign = TextAlign.Center,
                    color = localPallet.dirtyWhite
                )


            }


            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
            ) {
                items(repositories.size) {
                    ItemRepShotInfo(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentPadding = PaddingValues(8.dp),
                        owner = repositories[it].first,
                        name = repositories[it].second
                    ) {
                       managementScreen.navigationToDetails(
                            repositories[it].first,
                            repositories[it].second
                        )
                    }
                }
            }
        }
    }
}