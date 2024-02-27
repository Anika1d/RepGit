package com.mir.repgit.screens.main

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.mir.repgit.tools.composable.network.ConnectivityState
import com.mir.repgit.tools.composable.network.rememberConnectivityState
import com.mir.repgit.ui.layout.BackgroundContainer
import com.mir.repgit.ui.layout.ItemRep
import com.mir.repgit.ui.layout.SearchField
import com.mir.repgit.ui.layout.WelcomeButton
import com.mir.repgit.ui.theme.dirtyWhite
import com.mir.repgit.ui.theme.mint
import com.mir.repgit.values.LocalNavController
import com.mir.repgit.viewmodel.MainViewModel
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun MainSearchScreen() {
    val viewModel = koinInject<MainViewModel>()
    val navController = LocalNavController.current!!
    val value = viewModel.searchValue.observeAsState().value
    val active = viewModel.activeSearch.observeAsState().value
    val isFirstSetup = viewModel.firstSetupApp.observeAsState().value
    val repositories = viewModel.repositories.observeAsState().value
    val connectEthernet by rememberConnectivityState()
    val context = LocalContext.current
    val searchState = rememberLazyListState()
    BackgroundContainer(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.size(100.dp))
        WelcomeButton(modifier = Modifier
            .fillMaxSize(0.5f)
            .zIndex(1f)
            .background(Color.Transparent),
            expanded = !isFirstSetup,
            onClick = { viewModel.closeWelcomeWindow(true) })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            AnimatedVisibility(
                visible = isFirstSetup, enter = fadeIn(tween(300, 500))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Top
                ) {
                    this@Row.AnimatedVisibility(visible = !active) {
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
                    this@Row.AnimatedVisibility(visible = active) {
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
                            if (connectEthernet == ConnectivityState.Available) {
                                if (viewModel.repositories.value.isNotEmpty() && index > viewModel.repositories.value.size - 15) {
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
                        value = value,
                        onValueChange = {
                            viewModel.changeSearchValue(it)
                        },
                        onActiveChange = { viewModel.changeActiveSearch(it) },
                        active = active,
                        onSearch = {
                            if (connectEthernet == ConnectivityState.Available) {
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
                                    this@Row.AnimatedVisibility(visible = viewModel.reloadSearch.observeAsState().value == LoadState.SHOW) {
                                        CircularProgressIndicator(
                                            color = mint
                                        )
                                    }
                                    this@Row.AnimatedVisibility(visible = viewModel.reloadSearch.observeAsState().value != LoadState.SHOW && repositories.isNotEmpty()) {
                                        Text(
                                            text = "${viewModel.countResult.observeAsState().value}  result",
                                            style = TextStyle(fontSize = 12.sp, color = mint)
                                        )
                                    }
                                }
                            }
                            items(repositories.size) {
                                ItemRep(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp)
                                        .clickable {},
                                    contentPadding = PaddingValues(8.dp),
                                    repository = repositories[it]
                                ) {
                                    navController.navigate(Route.REPOSITORY_SCREEN.path + "${repositories[it].owner.name}/${repositories[it].name}")
                                }
                            }
                            item {
                                Box(
                                    modifier = Modifier.size(100.dp),
                                    contentAlignment = Alignment.Center,
                                    content = {
                                        this@Row.AnimatedVisibility(visible = viewModel.needAddResult.observeAsState().value == NextPackRepositoryState.LOAD) {
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
