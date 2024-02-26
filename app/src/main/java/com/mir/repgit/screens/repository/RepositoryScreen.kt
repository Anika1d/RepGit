package com.mir.repgit.screens.repository

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
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mir.repgit.R
import com.mir.repgit.tools.SizeManager
import com.mir.repgit.tools.composable.placeholder
import com.mir.repgit.ui.layout.BackgroundContainer
import com.mir.repgit.ui.layout.ItemIssue
import com.mir.repgit.ui.layout.RepositoryDetails
import com.mir.repgit.ui.theme.blue
import com.mir.repgit.ui.theme.dirtyWhite
import com.mir.repgit.ui.theme.mint
import com.mir.repgit.values.LocalNavController
import com.mir.repgit.viewmodel.RepositoryViewModel
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryScreen(owner: String, repo: String) {
    val sheetState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val viewModel = koinInject<RepositoryViewModel>()
    val navController = LocalNavController.current!!
    val repository = viewModel.repository.observeAsState().value
    val issues = viewModel.issues.observeAsState().value
    val repLog = viewModel.dataRepositoryReceived.observeAsState().value!!
    remember(owner) {
        coroutineScope.launch {
            viewModel.clearData()
            viewModel.getRepository(nameOwner = owner, nameRep = repo, onSuccess = {}, onError = {})
            viewModel.getIssues(nameOwner = owner, nameRep = repo, onSuccess = {}, onError = {})
        }
    }

    val pullToRefreshState = rememberPullToRefreshState()

    val scaleFraction =
        if (pullToRefreshState.isRefreshing) 1f else LinearOutSlowInEasing.transform(
            pullToRefreshState.progress
        ).coerceIn(0f, 1f)
    if (pullToRefreshState.isRefreshing) LaunchedEffect(key1 = true) {
        viewModel.getRepository(nameOwner = owner, nameRep = repo, onSuccess = {}, onError = {})
        viewModel.getIssues(nameOwner = owner, nameRep = repo, onSuccess = {}, onError = {})
        pullToRefreshState.endRefresh()
    }
    BackgroundContainer(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
    ) {
        PullToRefreshContainer(modifier = Modifier
            .align(Alignment.TopCenter)
            .graphicsLayer(scaleX = scaleFraction, scaleY = scaleFraction),
            contentColor = Color.Transparent,
            containerColor = Color.Transparent,
            state = pullToRefreshState,
            indicator = { pullRefreshState ->
                PullToRefreshDefaults.Indicator(
                    state = pullRefreshState, color = mint
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
                        .placeholder(enabled = !repLog),
                    text = repository?.name ?: "",
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
                                    text = "Issues", style = TextStyle(
                                        fontSize = 50.sp
                                    )
                                )
                            }
                            items(issues?.size ?: 0) { index ->
                                Spacer(modifier = Modifier.size(20.dp))
                                ItemIssue(
                                    modifier = Modifier.fillMaxWidth(), issue = issues!![index]
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
                            model = repository?.owner?.avatarUrl,
                            contentDescription = repository?.owner?.avatarUrl.toString(),
                            placeholder = painterResource(id = R.drawable.avatar_default),
                            error = if(repLog)painterResource(id = R.drawable.image_fail)else null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(
                                    SizeManager(
                                        LocalConfiguration.current.screenHeightDp,
                                        LocalConfiguration.current.screenWidthDp
                                    ).giveNeed(130.dp, 150.dp, 160.dp)
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
                                    item = repository?.starCount,
                                    painter = painterResource(id = R.drawable.star),
                                    contentDescription = "git_star",
                                    placeholder = !repLog
                                )
                                RepositoryDetails(
                                    modifier = Modifier,
                                    item = repository?.forkCount,
                                    painter = painterResource(id = R.drawable.forks),
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
                                    painter = painterResource(id = R.drawable.watch),
                                    contentDescription = "git_watching",
                                    placeholder = !repLog
                                )
                                RepositoryDetails(
                                    modifier = Modifier,
                                    item = repository?.issuesCount,
                                    painter = painterResource(id = R.drawable.issues),
                                    contentDescription = "git_branches",
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
