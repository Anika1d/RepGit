package com.mir.repgit.screens.main

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.mir.repgit.R
import com.mir.repgit.screens.navigation.Route
import com.mir.repgit.tools.LoadState
import com.mir.repgit.tools.NextPackRepositoryState
import com.mir.repgit.tools.network.ConnectivityState
import com.mir.repgit.ui.layout.BackgroundContainer
import com.mir.repgit.ui.layout.ItemRep
import com.mir.repgit.ui.layout.SearchField
import com.mir.repgit.ui.layout.SearchQueryLayout
import com.mir.repgit.ui.layout.WelcomeButton
import com.mir.repgit.ui.theme.dirtyWhite
import com.mir.repgit.ui.theme.mint
import com.mir.repgit.values.LocalNavController
import com.mir.repgit.viewmodel.MainViewModel
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject

@Composable
fun MainSearchScreen() {
    val viewModel = koinInject<MainViewModel>()
    val navController = LocalNavController.current!!
    val value = remember(viewModel) { viewModel.searchValue }.observeAsState()
    val active = remember(viewModel) { viewModel.activeSearch }.observeAsState()
    val isFirstSetup = remember(viewModel) { viewModel.firstSetupApp }.observeAsState()
    val repositories = remember(viewModel) { viewModel.repositories }.observeAsState()
    val searchQueries = remember(viewModel) { viewModel.searchQueries }.observeAsState()
    val connectEthernet = remember(viewModel) { viewModel.networkStatus }.collectAsState()
    val needAddResult = remember(viewModel) { viewModel.needAddResult }.observeAsState()
    val reloadSearch = remember(viewModel) { viewModel.reloadSearch }.observeAsState()
    val countResult = remember(viewModel) { viewModel.countResult }.observeAsState()


    val context = LocalContext.current
    val searchState = rememberLazyListState()
    val focusManager = LocalFocusManager.current

    BackgroundContainer(
        modifier = Modifier.fillMaxSize(), connectEthernet = connectEthernet.value
    ) {
        Spacer(modifier = Modifier.size(100.dp))
        WelcomeButton(modifier = Modifier
            .fillMaxSize(0.5f)
            .zIndex(1f)
            .background(Color.Transparent),
            expanded = !isFirstSetup.value,
            onClick = { viewModel.closeWelcomeWindow(true) })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            AnimatedVisibility(
                visible = isFirstSetup.value, enter = fadeIn(tween(300, 500))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Top
                ) {
                    this@Row.AnimatedVisibility(visible = !active.value) {
                        Box(
                            modifier = Modifier
                                .height(60.dp)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(id = R.string.app_name),
                                color = dirtyWhite,
                                textAlign = TextAlign.Center,
                                style = TextStyle(fontSize = 25.sp)
                            )

                        }
                    }
                    this@Row.AnimatedVisibility(visible = active.value) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(8.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            IconButton(onClick = { viewModel.changeActiveSearch(false) }) {
                                Icon(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .defaultMinSize(114.dp, 114.dp),
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "arrow_back",
                                    tint = dirtyWhite
                                )
                            }
                        }
                    }
                    LaunchedEffect(searchState) {
                        snapshotFlow {
                            searchState.firstVisibleItemIndex
                        }.collectLatest { index ->
                            if (connectEthernet.value == ConnectivityState.Available) {
                                if (repositories.value.isNotEmpty() && index > repositories.value.size - 15) {
                                    viewModel.nextPage(onSuccess = {},
                                        onError = { message: String? ->
                                            Toast.makeText(
                                                context,
                                                message ?: "Unknown Error",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        })
                                }
                            }
                        }
                    }
                    SearchField(modifier = Modifier.fillMaxWidth(0.9f),
                        value = value.value,
                        onValueChange = {
                            viewModel.changeSearchValue(it)
                        },
                        onActiveChange = {
                            viewModel.clearSearchQueries()
                            viewModel.changeActiveSearch(it)
                        },
                        focusManager = focusManager,
                        active = active.value,
                        onSearch = {
                            viewModel.clearSearchQueries()
                            if (connectEthernet.value == ConnectivityState.Available) {
                                viewModel.insertSearchValue()
                                viewModel.search(onError = {
                                    Toast.makeText(
                                        context, it, Toast.LENGTH_LONG
                                    ).show()
                                }, onSuccess = {})
                            } else {
                                Toast.makeText(
                                    context, "Check your internet connection", Toast.LENGTH_LONG
                                ).show()
                            }
                        },
                        searchState = searchState,
                        content = {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    this@Row.AnimatedVisibility(visible = reloadSearch.value == LoadState.SHOW) {
                                        viewModel.clearSearchQueries()
                                        CircularProgressIndicator(
                                            color = mint
                                        )
                                    }
                                    this@Row.AnimatedVisibility(
                                        visible = reloadSearch.value != LoadState.SHOW && repositories.value.isNotEmpty()
                                    ) {
                                        Text(
                                            text = "${countResult.value}  result",
                                            style = TextStyle(fontSize = 12.sp, color = mint)
                                        )
                                    }
                                }
                            }
                            items(searchQueries.value.size) {
                                SearchQueryLayout(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp),
                                    item = searchQueries.value[it],
                                    onDelete = {
                                        viewModel.deleteSearchValue(searchQueries.value[it])
                                    },
                                    onClick = {
                                        viewModel.changeSearchValue(searchQueries.value[it].name)
                                    },
                                )
                            }
                            items(repositories.value.size) {
                                ItemRep(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp),
                                    contentPadding = PaddingValues(8.dp),
                                    repository = repositories.value[it]
                                ) {
                                    navController.navigate(Route.REPOSITORY_SCREEN.path + "${repositories.value[it].owner.name}/${repositories.value[it].name}")
                                }
                            }
                            item {
                                Box(
                                    modifier = Modifier.size(100.dp),
                                    contentAlignment = Alignment.Center,
                                    content = {
                                        this@Row.AnimatedVisibility(visible = needAddResult.value == NextPackRepositoryState.LOAD) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.padding(5.dp), color = mint
                                            )
                                        }
                                    },
                                )
                            }
                        })
                }
            }

        }
    }
}
