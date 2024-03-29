package com.mir.repgit.screens.repository

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.mir.repgit.R
import com.mir.repgit.tools.SizeManager
import com.mir.repgit.tools.composable.placeholder.placeholder
import com.mir.repgit.tools.network.ConnectivityState
import com.mir.repgit.ui.layout.BackgroundContainer
import com.mir.repgit.ui.layout.ItemIssue
import com.mir.repgit.ui.layout.RepositoryDetails
import com.mir.repgit.ui.theme.blue
import com.mir.repgit.ui.theme.dirtyWhite
import com.mir.repgit.ui.theme.mint
import com.mir.repgit.values.LocalNavController
import com.mir.repgit.viewmodel.RepositoryViewModel
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryScreen(owner: String, repo: String) {
    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(skipHiddenState = false)
    )
    val viewModel = koinInject<RepositoryViewModel>()
    val navController = LocalNavController.current!!
    val repository = remember(viewModel) { viewModel.repository }.observeAsState()
    val issues = remember(viewModel) { viewModel.issues }.observeAsState()
    val repLog = remember(viewModel) { viewModel.dataRepositoryReceived }.observeAsState()
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
    val connectEthernet = remember(viewModel) {
        viewModel.networkStatus
    }.collectAsState()

    BackHandler {
        if (sheetState.bottomSheetState.currentValue==SheetValue.Expanded) {
            coroutineScope.launch { sheetState.bottomSheetState.partialExpand() }
        } else {
            viewModel.clearData()
            navController.navigateUp()
        }
    }

    LaunchedEffect(key1 = connectEthernet.value, block = {
        if (connectEthernet.value == ConnectivityState.Available) {
            viewModel.getAllInfoRep(owner = owner, repo = repo, onSuccess = { }, onError = {})
        }
    })

    val pullToRefreshState = rememberPullToRefreshState()

    val scaleFraction =
        if (pullToRefreshState.isRefreshing) 1f else LinearOutSlowInEasing.transform(
            pullToRefreshState.progress
        ).coerceIn(0f, 1f)

    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            if (connectEthernet.value == ConnectivityState.Available) {
                if (!pullToRefreshState.isRefreshing) viewModel.clearData()
                viewModel.getAllInfoRep(owner = owner,
                    repo = repo,
                    onSuccess = { if (pullToRefreshState.isRefreshing) pullToRefreshState.endRefresh() },
                    onError = { if (pullToRefreshState.isRefreshing) pullToRefreshState.endRefresh() })
            } else {
                Toast.makeText(
                    context, "Check your internet connection", Toast.LENGTH_LONG
                ).show()
                if (pullToRefreshState.isRefreshing) pullToRefreshState.endRefresh()
            }
        }
    }
    BackgroundContainer(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(pullToRefreshState.nestedScrollConnection),
        connectEthernet = connectEthernet.value
    ) {
        PullToRefreshContainer(modifier = Modifier
            .align(Alignment.TopCenter)
            .graphicsLayer(scaleX = scaleFraction, scaleY = scaleFraction),
            contentColor = Color.Transparent,
            containerColor = Color.Transparent,
            state = pullToRefreshState,
            indicator = { pullRefreshState ->
                PullToRefreshDefaults.Indicator(
                    modifier = Modifier.zIndex(1f), state = pullRefreshState, color = mint
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
                            color = mint,
                            alpha = 1f,
                            strokeWidth = 2.dp.toPx()
                        )
                    },
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(modifier = Modifier, onClick = {
                    viewModel.clearData()
                    navController.navigateUp()
                }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "arrow_back",
                        tint = dirtyWhite
                    )
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .placeholder(enabled = !repLog.value),
                    text = repository.value?.name ?: "",
                    textAlign = TextAlign.Center,
                    color = dirtyWhite
                )
            }

            BottomSheetScaffold(
                scaffoldState = sheetState,
                sheetContent = {
                    LazyColumn(
                        modifier = Modifier.animateContentSize(),
                        content = {
                            item {
                                Text(
                                    modifier = Modifier.animateContentSize(),
                                    text = "Issues " + if (repository.value?.issuesCount != "0") "" else "not found",
                                    style = TextStyle(
                                        fontSize = 35.sp
                                    )
                                )
                            }
                            items(issues.value?.size ?: 0) { index ->
                                Spacer(modifier = Modifier.size(20.dp))
                                ItemIssue(
                                    modifier = Modifier.fillMaxWidth(),
                                    issue = issues.value!![index]
                                )

                            }
                        },
                        contentPadding = PaddingValues(8.dp),
                    )
                },
                sheetContainerColor = blue.copy(0.9f),
                sheetContentColor = mint,
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
                            model = repository.value?.owner?.avatarUrl,
                            contentDescription = repository.value?.owner?.avatarUrl.toString(),
                            placeholder = painterResource(id = R.drawable.avatar_default),
                            error = if (repLog.value) painterResource(id = R.drawable.image_fail) else null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(
                                    sizeManager.giveNeed(130.dp, 150.dp, 160.dp)
                                )
                                .clip(CircleShape)
                                .placeholder(enabled = !repLog.value)

                        )
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .placeholder(enabled = !repLog.value),
                                text = repository.value?.owner?.name ?: "",
                                color = dirtyWhite,
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
                                    item = repository.value?.starCount,
                                    painter = painterResource(id = R.drawable.star),
                                    contentDescription = "git_star",
                                    placeholder = !repLog.value
                                )
                                RepositoryDetails(
                                    modifier = Modifier,
                                    item = repository.value?.forkCount,
                                    painter = painterResource(id = R.drawable.forks),
                                    contentDescription = "git_forks",
                                    placeholder = !repLog.value
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
                                    item = repository.value?.watchersCount,
                                    painter = painterResource(id = R.drawable.watch),
                                    contentDescription = "git_watching",
                                    placeholder = !repLog.value
                                )
                                RepositoryDetails(
                                    modifier = Modifier,
                                    item = repository.value?.issuesCount,
                                    painter = painterResource(id = R.drawable.issues),
                                    contentDescription = "git_branches",
                                    placeholder = !repLog.value
                                )

                            }


                        }
                    }

                    Text(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                            .placeholder(enabled = !repLog.value),
                        text = repository.value?.description ?: "",
                        color = dirtyWhite,
                    )
                    Spacer(modifier = Modifier.size(100.dp))

                }
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewRepositoryScreen() {
    RepositoryScreen("Anika1d", "GitRep")
}
