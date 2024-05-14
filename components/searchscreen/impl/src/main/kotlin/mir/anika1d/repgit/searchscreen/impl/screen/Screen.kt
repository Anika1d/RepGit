package mir.anika1d.repgit.searchscreen.impl.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
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
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.collectLatest
import mir.anika1d.core.network.connection.service.ConnectivityState
import mir.anika1d.core.resources.AppResources
import mir.anika1d.repgit.core.data.model.repository.RepositoryItem
import mir.anika1d.repgit.core.data.model.search.SearchQuery
import mir.anika1d.repgit.core.data.model.user.User
import mir.anika1d.repgit.core.data.state.LoadState
import mir.anika1d.repgit.core.data.state.NextPackRepositoryState
import mir.anika1d.repgit.core.data.state.SearchFilter
import mir.anika1d.repgit.core.ui.theme.LocalPallet
import mir.anika1d.repgit.searchscreen.impl.screen.management.ManagementScreen
import mir.anika1d.repgit.ui.composable.layout.BackgroundContainer
import mir.anika1d.repgit.ui.composable.layout.ItemRep
import mir.anika1d.repgit.ui.composable.layout.ItemUser
import mir.anika1d.repgit.ui.composable.layout.SearchField
import mir.anika1d.repgit.ui.composable.layout.SearchQueryLayout

@Composable
internal fun Screen(
    repositories: List<RepositoryItem>,
    users: List<User>,
    networkStatus: ConnectivityState,
    needAddResult: NextPackRepositoryState,
    searchQueries: List<SearchQuery>,
    countResult: String,
    reloadSearch: LoadState,
    selectedModeItem: SearchFilter,
    expandedMode: Boolean,
    itemsMode: List<SearchFilter>,
    searchText: String,
    active: Boolean,
    managementScreen: ManagementScreen,
) {

    val localPallet = LocalPallet.current

    val context = LocalContext.current
    val searchLazyState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(bottom = 80.dp),
                containerColor = localPallet.blue,
                contentColor = localPallet.mint,
                onClick = { managementScreen.navigationToDownload() }) {
                Icon(
                    modifier = Modifier
                        .size(40.dp)
                        .scale(scaleX = -1f, scaleY = 1f),
                    painter = painterResource(id = AppResources.Drawable.CHEST.id),
                    contentDescription = "floating_chest",
                    tint = localPallet.mint
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { it ->
        print(it)
        BackgroundContainer(
            modifier = Modifier.fillMaxSize(), connectEthernet = networkStatus
        ) {
            Spacer(modifier = Modifier.size(100.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .animateContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
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
                                text = stringResource(id = AppResources.Values.Strings.APP_NAME.id),
                                color = localPallet.dirtyWhite,
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
                            IconButton(onClick = { managementScreen.changeActiveSearch(false) }) {
                                Icon(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .defaultMinSize(114.dp, 114.dp),
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "arrow_back",
                                    tint = localPallet.dirtyWhite
                                )
                            }
                        }
                    }
                    LaunchedEffect(searchLazyState) {
                        snapshotFlow {
                            searchLazyState.firstVisibleItemIndex
                        }.collectLatest { index ->
                            if (networkStatus == ConnectivityState.Available) {
                                if (repositories.isNotEmpty() && index > repositories.size - 15) {
                                    managementScreen.nextPage(onSuccess = {},
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
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = localPallet.darkBlue,
                            focusedContainerColor = localPallet.darkBlue,
                            unfocusedPlaceholderColor = localPallet.mint,
                            focusedPlaceholderColor = localPallet.mint,
                            focusedTextColor = localPallet.dirtyWhite,
                            unfocusedTextColor = localPallet.dirtyWhite,
                            cursorColor = localPallet.dirtyWhite,
                            disabledTextColor = localPallet.dirtyWhite,
                            ),
                        searchText = searchText,
                        onChangeSearchValue = {
                            managementScreen.changeSearchValue(it)
                        },
                        onActiveChange = {
                            managementScreen.clearSearchQueries()
                            managementScreen.changeActiveSearch(it)
                        },
                        focusManager = focusManager,
                        isFocus = active,
                        onSearch = {
                            managementScreen.clearSearchQueries()
                            if (networkStatus == ConnectivityState.Available) {
                                managementScreen.insertSearchValue()
                                managementScreen.search(onError = {
                                    Toast.makeText(
                                        context, it, Toast.LENGTH_LONG
                                    ).show()
                                }, onSuccess = {})
                            } else {
                                Toast.makeText(
                                    context,
                                    context.resources.getText(AppResources.Values.Strings.CHECK_CONNECTION.id),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        },
                        searchState = searchLazyState,
                        onItemModeChange = { managementScreen.onItemModeChange(it) },
                        onExpandedModeChange = { managementScreen.onExpandedModeChange(it) },
                        itemsMode = itemsMode,
                        expandedMode = expandedMode,
                        selectedModeItem = selectedModeItem,
                        content = {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    this@Row.AnimatedVisibility(visible = reloadSearch == LoadState.SHOW) {
                                        managementScreen.clearSearchQueries()
                                        CircularProgressIndicator(
                                            color = localPallet.mint
                                        )
                                    }
                                    this@Row.AnimatedVisibility(
                                        visible = reloadSearch != LoadState.SHOW && repositories.isNotEmpty()
                                    ) {
                                        Text(
                                            text = "$countResult  " +
                                                    "${context.resources.getText(AppResources.Values.Strings.RESULT.id)}",
                                            style = TextStyle(
                                                fontSize = 12.sp,
                                                color = localPallet.mint
                                            )
                                        )
                                    }
                                }
                            }
                            items(searchQueries.size) {
                                SearchQueryLayout(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp),
                                    item = searchQueries[it],
                                    onDelete = {
                                        managementScreen.deleteSearchValue(searchQueries[it])
                                    },
                                    onClick = {
                                        managementScreen.changeSearchValue(searchQueries[it].name)
                                    },
                                )
                            }

                            items(repositories.size) {
                                AnimatedVisibility(visible = selectedModeItem != SearchFilter.USERS) {
                                    ItemRep(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(120.dp),
                                        contentPadding = PaddingValues(8.dp),
                                        repository = repositories[it]
                                    ) {
                                        managementScreen.navigationToDetails(
                                            repositories[it].owner.name,
                                            repositories[it].name
                                        )
                                    }
                                }
                            }
                            items(users.size) {
                                AnimatedVisibility(visible = selectedModeItem == SearchFilter.USERS) {
                                    ItemUser(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(120.dp),
                                        contentPadding = PaddingValues(8.dp),
                                        user = users[it]
                                    ) {
                                        managementScreen.navigationToProfile(
                                            user = users[it].name,
                                            avatarUrl = users[it].avatarUrl
                                        )
                                    }
                                }
                            }
                            item {
                                Box(
                                    modifier = Modifier.size(100.dp),
                                    contentAlignment = Alignment.Center,
                                    content = {
                                        this@Row.AnimatedVisibility(visible = needAddResult == NextPackRepositoryState.LOAD) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.padding(5.dp),
                                                color = localPallet.mint
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