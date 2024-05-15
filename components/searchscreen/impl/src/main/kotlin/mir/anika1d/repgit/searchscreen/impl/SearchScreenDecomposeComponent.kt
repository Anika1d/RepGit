package mir.anika1d.repgit.searchscreen.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mir.anika1d.core.network.connection.service.ConnectivityState
import mir.anika1d.core.network.connection.service.INetworkConnectivityService
import mir.anika1d.repgit.core.data.model.page.Page
import mir.anika1d.repgit.core.data.model.repository.Owner
import mir.anika1d.repgit.core.data.model.repository.PageRepository
import mir.anika1d.repgit.core.data.model.repository.RepositoryItem
import mir.anika1d.repgit.core.data.model.search.SearchQuery
import mir.anika1d.repgit.core.data.model.user.PageUser
import mir.anika1d.repgit.core.data.model.user.User
import mir.anika1d.repgit.core.data.state.LoadState
import mir.anika1d.repgit.core.data.state.NextPackRepositoryState
import mir.anika1d.repgit.core.data.state.SearchFilter
import mir.anika1d.repgit.database.data.request.SearchQueryRequest
import mir.anika1d.repgit.database.usecase.DeleteSearchQueryUseCase
import mir.anika1d.repgit.database.usecase.GetSearchQueryUseCase
import mir.anika1d.repgit.database.usecase.InsertSearchQueryUseCase
import mir.anika1d.repgit.datastore.api.ACManagerDataStore
import mir.anika1d.repgit.network.core.data.request.SearchRequest
import mir.anika1d.repgit.network.core.data.response.ResultState
import mir.anika1d.repgit.network.usecases.search.SearchRepositoryAllUseCase
import mir.anika1d.repgit.network.usecases.search.SearchRepositoryByUserUseCase
import mir.anika1d.repgit.network.usecases.search.SearchUserUseCase
import mir.anika1d.repgit.searchscreen.api.ACSearchScreenDecomposeComponent
import mir.anika1d.repgit.searchscreen.impl.screen.Screen
import mir.anika1d.repgit.searchscreen.impl.screen.management.ManagementScreen
import mir.anika1d.repgit.searchscreen.impl.timer.Timer
import mir.anika1d.repgit.searchscreen.impl.timer.TimerState

internal class SearchScreenDecomposeComponent(
    context: ComponentContext,
    private val navigationToDetails: (String, String) -> Unit,
    private val navigationToDownload: () -> Unit,
    private val navigationToProfile: (String, String) -> Unit,
    private val navigationToAuth: () -> Unit,
    private val searchRepositoryAllUseCase: SearchRepositoryAllUseCase,
    private val searchUserUseCase: SearchUserUseCase,
    private val searchRepositoryByUserUseCase: SearchRepositoryByUserUseCase,
    private val deleteSearchQueryUseCase: DeleteSearchQueryUseCase,
    private val insertSearchQueryUseCase: InsertSearchQueryUseCase,
    private val getSearchQueryUseCase: GetSearchQueryUseCase,
    private val managerDataStore: ACManagerDataStore,
    networkConnectivityService: INetworkConnectivityService,
) :
    ACSearchScreenDecomposeComponent(context) {
    private val timer = Timer(timeEvent = 3000)


    private val networkStatus: StateFlow<ConnectivityState> =
        networkConnectivityService.connectivityState.stateIn(
            initialValue = ConnectivityState.Undefined,
            scope = componentScope,
            started = WhileSubscribed(5000)
        )

    private val _searchQueries: MutableStateFlow<List<SearchQuery>> = MutableStateFlow(emptyList())

    private val _repositories: MutableStateFlow<List<RepositoryItem>> =
        MutableStateFlow(emptyList())
    private val _users: MutableStateFlow<List<User>> =
        MutableStateFlow(emptyList())

    private val lastSearchPage: MutableStateFlow<Page<*>?> = MutableStateFlow(null)


    private val _needAddResult: MutableStateFlow<NextPackRepositoryState> =
        MutableStateFlow(NextPackRepositoryState.IMPOSSIBLE)

    private fun logout() {
        componentScope.launch {
            managerDataStore.clear()
            navigationToAuth()
        }
    }

    private val _searchValue = MutableStateFlow("")

    private val _activeSearch = MutableStateFlow(false)

    private val _reloadSearch: MutableStateFlow<LoadState> = MutableStateFlow(LoadState.HIDE)


    private val _expandedMode = MutableStateFlow(false)


    private val _selectedModeItem = MutableStateFlow(SearchFilter.REPOS_ALL)

    fun onExpandedModeChange(expanded: Boolean) {
        componentScope.launch {
            _expandedMode.emit(expanded)
        }
    }

    fun onItemModeChange(item: SearchFilter) {
        componentScope.launch {
            _repositories.emit(listOf())
            _selectedModeItem.emit(item)
        }
    }


    fun nextPage(onSuccess: () -> Unit, onError: (String?) -> Unit) {
        componentScope.launch {
            if (_needAddResult.value == NextPackRepositoryState.POSSIBLE) {
                _needAddResult.emit(NextPackRepositoryState.LOAD)
                lastSearchPage.value?.nextPage?.let {
                    search(searchR = SearchRequest.fromUrl(it),
                        onSuccess = { onSuccess.toString() },
                        onError = { mes ->
                            componentScope.launch {
                                _needAddResult.emit((NextPackRepositoryState.POSSIBLE))
                            }
                            onError.invoke(mes)
                        })
                }

            }
        }
    }

    private suspend fun getSearchValue(s: String) {
        componentScope.launch {
            getSearchQueryUseCase.invoke(SearchQueryRequest(name = s, limit = 5)).cancellable()
                .collect { sq ->
                    _searchQueries.emit(sq.map { SearchQuery(it.name) })
                    this.cancel()
                }
        }
    }

    fun deleteSearchValue(searchQuery: SearchQuery) {
        componentScope.launch {
            deleteSearchQueryUseCase.deleteSearchQuery(SearchQueryRequest(searchQuery.name))
            getSearchValue(_searchValue.value)
        }
    }

    fun insertSearchValue() {
        componentScope.launch {
            insertSearchQueryUseCase.insertSearchQuery(SearchQueryRequest(_searchValue.value))
        }
    }


    fun search(
        searchR: SearchRequest? = null, onSuccess: () -> Unit, onError: (String?) -> Unit
    ) {
        componentScope.launch {
            if (searchR == null) timer.destroy()
            else timer.stop()
            val sR: SearchRequest? = searchR
                ?: if (_searchValue.value.isEmpty()) null else SearchRequest(query = _searchValue.value)
            if (sR != null) {
                _reloadSearch.emit(LoadState.SHOW)
                clearSearchQueries()
                when (_selectedModeItem.value) {
                    SearchFilter.REPOS_ALL -> searchRepositoryAllUseCase(sR).collect { result ->
                        when (result) {
                            is ResultState.Success -> {
                                _users.emit(listOf())
                                val list = _repositories.value.toMutableList()
                                val page = PageRepository(
                                    incompleteResults = result.data?.incompleteResults,
                                    items = result.data?.items?.map {
                                        RepositoryItem(
                                            owner = Owner(
                                                name = it.owner.name,
                                                avatarUrl = it.owner.avatarUrl,
                                            ),
                                            name = it.name,
                                            description = it.description,
                                            starCount = it.starCount,
                                            watchersCount = it.watchersCount,
                                            forkCount = it.forkCount,
                                            issuesCount = it.issuesCount,
                                            url = it.url,
                                            downloadUrl = it.downloadUrl,
                                            defaultBranch = it.defaultBranch,
                                            language = it.language,
                                            updatedAt = it.updatedAt
                                        )
                                    } ?: listOf(),
                                    nextPage = result.data?.nextPage,
                                    totalCount = result.data?.totalCount,
                                )
                                list.addAll(page.items)
                                _repositories.emit(list)
                                lastSearchPage.emit(page)
                                _needAddResult.emit(
                                    (if (page.nextPage != null) NextPackRepositoryState.POSSIBLE
                                    else NextPackRepositoryState.IMPOSSIBLE)
                                )
                                onSuccess.invoke()
                            }

                            is ResultState.NetworkError -> {
                                if (result.statusCode2Int == 401)
                                    logout()
                                onError.invoke(result.error.message)
                            }

                            is ResultState.Error -> {
                                when (result.statusCode2Int) {
                                    400 -> onError("Something went wrong, try again later")
                                    401 ->  logout()
                                    else -> onError.invoke(result.error.message)
                                }
                            }
                        }
                        _reloadSearch.emit(LoadState.HIDE)
                    }

                    SearchFilter.REPOS_OWNER -> searchRepositoryByUserUseCase(sR).collect { result ->
                        when (result) {
                            is ResultState.Success -> {
                                _users.emit(listOf())
                                val list = _repositories.value.toMutableList()
                                val page = PageRepository(
                                    incompleteResults = result.data?.incompleteResults,
                                    items = result.data?.items?.map {
                                        RepositoryItem(
                                            owner = Owner(
                                                name = it.owner.name,
                                                avatarUrl = it.owner.avatarUrl,
                                            ),
                                            name = it.name,
                                            description = it.description,
                                            starCount = it.starCount,
                                            watchersCount = it.watchersCount,
                                            forkCount = it.forkCount,
                                            issuesCount = it.issuesCount,
                                            url = it.url,
                                            downloadUrl = it.downloadUrl,
                                            defaultBranch = it.defaultBranch,
                                            language = it.language,
                                            updatedAt = it.updatedAt
                                        )
                                    } ?: listOf(),
                                    nextPage = result.data?.nextPage,
                                    totalCount = result.data?.totalCount,
                                )
                                list.addAll(page.items)
                                _repositories.emit(list)
                                lastSearchPage.emit(page)
                                _needAddResult.emit(
                                    (if (page.nextPage != null) NextPackRepositoryState.POSSIBLE
                                    else NextPackRepositoryState.IMPOSSIBLE)
                                )
                                onSuccess.invoke()
                            }

                            is ResultState.NetworkError -> {
                                if (result.statusCode2Int == 401)
                                    logout()
                                onError.invoke(result.error.message)
                            }

                            is ResultState.Error -> {
                                when (result.statusCode2Int) {
                                    400 -> onError("Something went wrong, try again later")
                                    401 ->  logout()
                                    else -> onError.invoke(result.error.message)
                                }
                            }
                        }
                        _reloadSearch.emit(LoadState.HIDE)
                    }

                    SearchFilter.USERS -> {
                        searchUserUseCase(sR).collect { result ->
                            when (result) {
                                is ResultState.Success -> {
                                    _repositories.emit(listOf())
                                    val list = _users.value.toMutableList()
                                    val page = PageUser(
                                        incompleteResults = result.data?.incompleteResults,
                                        items = result.data?.items?.map {
                                            User(
                                                name = it.name,
                                                avatarUrl = it.avatarUrl,
                                                followersCount = it.followersCount,
                                                repositoryCount = it.repositoryCount
                                            )
                                        } ?: listOf(),
                                        nextPage = result.data?.nextPage,
                                        totalCount = result.data?.totalCount,
                                    )
                                    list.addAll(page.items)
                                    _users.emit(list)
                                    lastSearchPage.emit(page)
                                    _needAddResult.emit(
                                        (if (page.nextPage != null) NextPackRepositoryState.POSSIBLE
                                        else NextPackRepositoryState.IMPOSSIBLE)
                                    )
                                    onSuccess.invoke()
                                }

                                is ResultState.NetworkError -> {
                                    if (result.statusCode2Int == 401)
                                        logout()
                                    onError.invoke(result.error.message)
                                }

                                is ResultState.Error -> {
                                    when (result.statusCode2Int) {
                                        400 -> onError("Something went wrong, try again later")
                                        401 ->  logout()
                                        else -> onError.invoke(result.error.message)
                                    }
                                }
                            }
                            _reloadSearch.emit(LoadState.HIDE)
                        }

                    }
                }
            }
        }
    }

    fun changeActiveSearch(b: Boolean) {
        _activeSearch.value = b
    }


    fun changeSearchValue(s: String) {
        componentScope.launch {
            if (timer.stateTimer.value == TimerState.End) {
                timer.start {
                    lazy {
                        search(SearchRequest(_searchValue.value), {}, {})
                    }
                }
            } else {
                timer.setNewTime()
            }
            _repositories.value = listOf()
            _searchValue.value = s
            lastSearchPage.value = null
            getSearchValue(_searchValue.value)

        }
    }

    fun clearSearchQueries() {
        _searchQueries.value = listOf()
    }


    @Composable
    override fun Render() {
        Screen(
            repositories = _repositories.collectAsState().value,
            users = _users.collectAsState().value,
            networkStatus = networkStatus.collectAsState().value,
            needAddResult = _needAddResult.collectAsState().value,
            searchQueries = _searchQueries.collectAsState().value,
            countResult = lastSearchPage.collectAsState().value?.totalCount?.toString() ?: "0",
            reloadSearch = _reloadSearch.collectAsState().value,
            selectedModeItem = _selectedModeItem.collectAsState().value,
            expandedMode = _expandedMode.collectAsState().value,
            itemsMode = SearchFilter.entries,
            searchText = _searchValue.collectAsState().value,
            active = _activeSearch.collectAsState().value,
            managementScreen = object : ManagementScreen {
                override fun nextPage(onSuccess: () -> Unit, onError: (String?) -> Unit) {
                    this@SearchScreenDecomposeComponent.nextPage(onSuccess, onError)
                }

                override fun navigationToDetails(owner: String, repositoryName: String) {
                    this@SearchScreenDecomposeComponent.navigationToDetails(
                        owner,
                        repositoryName
                    )
                }

                override fun logout() {
                    this@SearchScreenDecomposeComponent.logout()

                }

                override fun navigationToDownload() {
                    this@SearchScreenDecomposeComponent.navigationToDownload()
                }

                override fun navigationToProfile(user: String, avatarUrl: String) {
                    this@SearchScreenDecomposeComponent.navigationToProfile(user, avatarUrl)
                }

                override fun changeActiveSearch(b: Boolean) {
                    this@SearchScreenDecomposeComponent.changeActiveSearch(b)
                }

                override fun changeSearchValue(s: String) {
                    this@SearchScreenDecomposeComponent.changeSearchValue(s)
                }

                override fun clearSearchQueries() {
                    this@SearchScreenDecomposeComponent.clearSearchQueries()
                }

                override fun insertSearchValue() {
                    this@SearchScreenDecomposeComponent.insertSearchValue()
                }

                override fun search(onSuccess: () -> Unit, onError: (String?) -> Unit) {
                    this@SearchScreenDecomposeComponent.search(
                        onSuccess = onSuccess,
                        onError = onError
                    )
                }

                override fun onItemModeChange(filter: SearchFilter) {
                    this@SearchScreenDecomposeComponent.onItemModeChange(filter)
                }

                override fun onExpandedModeChange(b: Boolean) {
                    this@SearchScreenDecomposeComponent.onExpandedModeChange(b)
                }

                override fun deleteSearchValue(searchQuery: SearchQuery) {
                    this@SearchScreenDecomposeComponent.deleteSearchValue(searchQuery)
                }
            }
        )

    }

    override fun onDestroy() {
        timer.destroy()
        super.onDestroy()
    }
}
