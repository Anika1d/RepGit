package com.mir.repgit.screens.main

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mir.repgit.screens.navigation.Route
import com.mir.repgit.tools.LoadState
import com.mir.repgit.tools.NextPackRepositoryState
import com.mir.repgit.ui.layout.BackgroundContainer
import com.mir.repgit.ui.layout.ItemRep
import com.mir.repgit.ui.layout.SearchField
import com.mir.repgit.ui.layout.WelcomeButton
import com.mir.repgit.values.LocalNavController
import com.mir.repgit.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun MainSearchScreen() {
    val viewModel = koinInject<MainViewModel>()
    val navController = LocalNavController.current!!
    val value = viewModel.searchValue.observeAsState().value!!
    val active = viewModel.activeSearch.observeAsState().value!!
    val isFirstSetup = viewModel.firstSetupApp.observeAsState().value!!
    val repositories = viewModel.repositories.observeAsState().value
    val coroutineScope = rememberCoroutineScope()
    BackgroundContainer(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            AnimatedVisibility(
                visible = !isFirstSetup,
                enter = fadeIn() + slideInVertically(tween(800))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Top
                ) {
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
                                contentDescription = "",
                                tint = Color(0xFFFAFAFA)
                            )
                        }
                    }
                    val context = LocalContext.current
                    val searchState = rememberLazyListState()
                    LaunchedEffect(searchState) {
                        snapshotFlow {
                            searchState.firstVisibleItemIndex
                        }.collectLatest { index ->
                            if (!viewModel.repositories.value.isNullOrEmpty()) if (index > viewModel.repositories.value!!.size - 15) {
                                viewModel.nextPage(onSuccess = {}, onError = { message: String? ->
                                    Toast.makeText(
                                        context, message ?: "Unknown Error", Toast.LENGTH_LONG
                                    ).show()
                                })
                            }
                        }
                    }
                    SearchField(modifier = Modifier.fillMaxWidth(0.9f),
                        value = value,
                        onValueChange = {
                            coroutineScope.launch {
                                viewModel.changeSearchValue(it)
                            }
                        },
                        onActiveChange = { viewModel.changeActiveSearch(it) },
                        active = active,
                        onSearch = {
                            coroutineScope.launch {
                                viewModel.search(onError = {}, onSuccess = {})
                            }
                        },
                        searchState = searchState,
                        content = {
                            item {
                                AnimatedVisibility(visible = viewModel.reloadSearch.observeAsState().value == LoadState.SHOW) {
                                    CircularProgressIndicator()
                                }
                            }
                            items(repositories?.size ?: 0) {
                                ItemRep(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp)
                                        .clickable {},
                                    contentPadding = PaddingValues(8.dp),
                                    repository = repositories!![it]
                                ) {
                                    navController.navigate(Route.REPOSITORY_SCREEN.path + "2")
                                }
                            }
                            item {
                                AnimatedVisibility(visible = viewModel.needAddResult.observeAsState().value == NextPackRepositoryState.LOAD) {
                                    CircularProgressIndicator()
                                }
                            }
                        })
                }
            }
            Spacer(modifier = Modifier.size(100.dp))
            WelcomeButton(
                modifier = Modifier.fillMaxSize(0.5f),
                expanded = !isFirstSetup,
                onClick = {
                    viewModel.closeWelcomeWindow(false)
                }

            )

        }
    }
}