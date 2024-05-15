package mir.anika1d.repgit.repodetailscreen.impl.screen

import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import mir.anika1d.core.network.connection.service.ConnectivityState
import mir.anika1d.core.resources.AppResources
import mir.anika1d.repgit.core.data.model.issues.Issues
import mir.anika1d.repgit.core.data.model.repository.RepositoryItem
import mir.anika1d.repgit.core.ui.theme.LocalPallet
import mir.anika1d.repgit.repodetailscreen.api.model.DetailsRepoConfig
import mir.anika1d.repgit.repodetailscreen.impl.screen.management.ManagementScreen
import mir.anika1d.repgit.ui.composable.layout.BackgroundContainer
import mir.anika1d.repgit.ui.composable.layout.ItemIssue
import mir.anika1d.repgit.ui.composable.layout.RepositoryDetails
import mir.anika1d.repgit.ui.composable.layout.placeholder
import mir.anika1d.repgit.ui.composable.tools.SizeManager
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Screen(
    issues: List<Issues>?,
    repLog: Boolean,
    repository: RepositoryItem?,
    config: DetailsRepoConfig,
    connectNetwork: ConnectivityState,
    managementScreen: ManagementScreen
) {
    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded
        )
    )
    val localPallet = LocalPallet.current
    val checkDownloadRepo = remember(repository) {
        File(
            Environment.getExternalStorageDirectory().toString() +
                    "/Download/repgit/${repository?.owner?.name}",
            "${repository?.name?.replace(".", "_")}.zip"
        ).exists()
    }
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val coroutineScope = rememberCoroutineScope()
    val sizeManager by remember {
        mutableStateOf(
            SizeManager(
                configuration.screenHeightDp,
                configuration.screenWidthDp
            )
        )
    }
    BackHandler {
        if (sheetState.bottomSheetState.currentValue == SheetValue.Expanded) {
            coroutineScope.launch { sheetState.bottomSheetState.partialExpand() }
        } else {
            managementScreen.clearData()
            managementScreen.onBack()
        }
    }
    LaunchedEffect(connectNetwork) {
        if (connectNetwork == ConnectivityState.Available) {
            managementScreen.getAllInfoRep(
                owner = config.owner,
                repo = config.repo,
                onSuccess = { },
                onError = {})
        }
    }

    val pullToRefreshState = rememberPullToRefreshState()

    val scaleFraction =
        if (pullToRefreshState.isRefreshing) 1f else LinearOutSlowInEasing.transform(
            pullToRefreshState.progress
        ).coerceIn(0f, 1f)

    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            if (connectNetwork == ConnectivityState.Available) {
                if (!pullToRefreshState.isRefreshing) managementScreen.clearData()
                managementScreen.getAllInfoRep(owner = config.owner,
                    repo = config.repo,
                    onSuccess = { if (pullToRefreshState.isRefreshing) pullToRefreshState.endRefresh() },
                    onError = { if (pullToRefreshState.isRefreshing) pullToRefreshState.endRefresh() })
            } else {
                Toast.makeText(
                    context,
                    context.resources.getText(AppResources.Values.Strings.CHECK_CONNECTION.id),
                    Toast.LENGTH_LONG
                ).show()
                if (pullToRefreshState.isRefreshing) pullToRefreshState.endRefresh()
            }
        }
    }
    BackgroundContainer(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(pullToRefreshState.nestedScrollConnection),
        connectEthernet = connectNetwork
    ) {
        PullToRefreshContainer(modifier = Modifier
            .align(Alignment.TopCenter)
            .graphicsLayer(scaleX = scaleFraction, scaleY = scaleFraction),
            contentColor = Color.Transparent,
            containerColor = Color.Transparent,
            state = pullToRefreshState,
            indicator = { pullRefreshState ->
                PullToRefreshDefaults.Indicator(
                    modifier = Modifier.zIndex(1f),
                    state = pullRefreshState,
                    color = localPallet.mint
                )
            })
        Column(Modifier.fillMaxSize()) {
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
                    managementScreen.clearData()
                    managementScreen.onBack()
                }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "arrow_back",
                        tint = localPallet.dirtyWhite
                    )
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .placeholder(enabled = !repLog),
                    text = repository?.name ?: "",
                    textAlign = TextAlign.Center,
                    color = localPallet.dirtyWhite
                )
                AnimatedVisibility(visible = !checkDownloadRepo) {
                    IconButton(modifier = Modifier, onClick = {
                        managementScreen.download()
                    }) {
                        Icon(
                            modifier = Modifier,
                            painter = painterResource(id = AppResources.Drawable.DOWNLOAD.id),
                            contentDescription = "download_icon",
                            tint = localPallet.dirtyWhite
                        )
                    }
                }

                IconButton(modifier = Modifier, onClick = {
                    val i = Intent(Intent.ACTION_VIEW)
                    i.setData(Uri.parse(repository?.url))
                    startActivity(context, i, bundleOf())
                }) {
                    Icon(
                        modifier = Modifier.padding(4.dp),
                        painter = painterResource(id = AppResources.Drawable.GITHUB_ICON.id),
                        contentDescription = "github_icon",
                        tint = localPallet.dirtyWhite
                    )
                }
            }
            BottomSheetScaffold(
                scaffoldState = sheetState,
                sheetPeekHeight = 200.dp,
                sheetContent = {
                    LazyColumn(
                        modifier = Modifier.animateContentSize(),
                        content = {
                            item {
                                Text(
                                    modifier = Modifier.animateContentSize().placeholder(!repLog),
                                    text = if (repository?.issuesCount != "0") "${
                                        stringResource( id = AppResources.Values.Strings.ISSUES.id)
                                    } ${ repository?.issuesCount ?: ""} " else "${
                                        stringResource(
                                            id = AppResources.Values.Strings.ISSUES_NOT_FOUND.id
                                        )
                                    } ",
                                    style = TextStyle(
                                        fontSize = 35.sp
                                    )
                                )
                            }
                            items(issues?.size ?: 0) { index ->
                                Spacer(modifier = Modifier.size(20.dp))
                                ItemIssue(
                                    modifier = Modifier.fillMaxWidth(),
                                    issue = issues!![index]
                                )

                            }
                            item {
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp)
                                )
                            }
                        },
                        contentPadding = PaddingValues(8.dp),
                    )
                },
                sheetContainerColor = localPallet.blue.copy(0.9f),
                sheetContentColor = localPallet.mint,
                contentColor = Color.Transparent,
                containerColor = Color.Transparent,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            paddingValues = PaddingValues(
                                horizontal = 8.dp, vertical = 16.dp
                            )
                        )
                        .verticalScroll(
                            state = rememberScrollState()
                        )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AsyncImage(
                            model = repository?.owner?.avatarUrl,
                            contentDescription = repository?.owner?.avatarUrl.toString(),
                            placeholder = painterResource(id = AppResources.Drawable.DEFAULT_AVATAR.id),
                            error = if (repLog) painterResource(id = AppResources.Drawable.IMAGE_FAIL.id)
                            else null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(
                                    sizeManager.giveNeed(130.dp, 150.dp, 160.dp)
                                )
                                .clip(CircleShape)
                                .placeholder(enabled = !repLog)

                        )
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .placeholder(enabled = !repLog),
                                text = repository?.owner?.name ?: "",
                                color = localPallet.dirtyWhite,
                                style = TextStyle(
                                    fontFamily = FontFamily.Serif, fontSize = 23.sp
                                )
                            )
                            Spacer(modifier = Modifier.size(20.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                RepositoryDetails(
                                    modifier = Modifier.fillMaxWidth(0.5f),
                                    item = repository?.starCount,
                                    painter = painterResource(id = AppResources.Drawable.STAR.id),
                                    contentDescription = "git_star",
                                    placeholder = !repLog
                                )
                                RepositoryDetails(
                                    modifier = Modifier,
                                    item = repository?.forkCount,
                                    painter = painterResource(id = AppResources.Drawable.FORKS.id),
                                    contentDescription = "git_forks",
                                    placeholder = !repLog
                                )
                            }
                            Spacer(modifier = Modifier.size(20.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                RepositoryDetails(
                                    modifier = Modifier.fillMaxWidth(0.5f),
                                    item = repository?.watchersCount,
                                    painter = painterResource(id = AppResources.Drawable.WATCH.id),
                                    contentDescription = "git_watching",
                                    placeholder = !repLog
                                )
                                RepositoryDetails(
                                    modifier = Modifier.clickable {
                                        coroutineScope.launch { sheetState.bottomSheetState.expand() }
                                    },
                                    item = repository?.issuesCount,
                                    painter = painterResource(id = AppResources.Drawable.ISSUES.id),
                                    contentDescription = "git_issues",
                                    placeholder = !repLog
                                )

                            }


                        }
                    }

                    Text(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                            .placeholder(enabled = !repLog),
                        text = repository?.description ?: "",
                        color = localPallet.dirtyWhite,
                    )
                    Spacer(modifier = Modifier.size(100.dp))

                }
            }

        }
    }
}