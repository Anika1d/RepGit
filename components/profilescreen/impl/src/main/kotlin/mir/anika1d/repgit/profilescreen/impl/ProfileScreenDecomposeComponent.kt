package mir.anika1d.repgit.profilescreen.impl

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mir.anika1d.core.decompose.DecomposeOnBackParameter
import mir.anika1d.core.network.connection.service.ConnectivityState
import mir.anika1d.core.network.connection.service.INetworkConnectivityService
import mir.anika1d.repgit.core.data.model.repository.Owner
import mir.anika1d.repgit.core.data.model.repository.PageRepository
import mir.anika1d.repgit.core.data.model.repository.RepositoryItem
import mir.anika1d.repgit.core.data.model.user.User
import mir.anika1d.repgit.core.data.state.NextPackRepositoryState
import mir.anika1d.repgit.datastore.api.ACManagerDataStore
import mir.anika1d.repgit.network.core.data.request.SearchRequest
import mir.anika1d.repgit.network.core.data.request.UserRequest
import mir.anika1d.repgit.network.core.data.response.ResultState
import mir.anika1d.repgit.network.usecases.search.SearchRepositoryByUserUseCase
import mir.anika1d.repgit.network.usecases.users.GetUserUseCase
import mir.anika1d.repgit.profilescreen.api.ACProfileScreenDecomposeComponent
import mir.anika1d.repgit.profilescreen.api.model.ProfileConfig
import mir.anika1d.repgit.profilescreen.impl.screen.Screen
import mir.anika1d.repgit.profilescreen.impl.screen.management.ManagementScreen

class ProfileScreenDecomposeComponent(
    componentContext: ComponentContext,
    private val onBack: DecomposeOnBackParameter,
    networkConnectivityService: INetworkConnectivityService,
    private val config: ProfileConfig,
    private val navigationToDetails: (String, String) -> Unit,
    private val navigationToAuth: () -> Unit,
    private val useCaseRepository: SearchRepositoryByUserUseCase,
    private val useCaseUser: GetUserUseCase,
    private val managerDataStore: ACManagerDataStore,
) : ACProfileScreenDecomposeComponent(componentContext) {
    private val flowRepository: MutableStateFlow<List<RepositoryItem>> =
        MutableStateFlow(emptyList())

    private var owner: Owner? = null
    private val flowUser: MutableStateFlow<User?> =
        MutableStateFlow(null)

    private val lastPageRepository: MutableStateFlow<PageRepository?> = MutableStateFlow(null)
    private val needAddResult: MutableStateFlow<NextPackRepositoryState> =
        MutableStateFlow((NextPackRepositoryState.IMPOSSIBLE))
    private val networkStatus: StateFlow<ConnectivityState> =
        networkConnectivityService.connectivityState.stateIn(
            initialValue = ConnectivityState.Undefined,
            scope = componentScope,
            started = WhileSubscribed(5000)
        )

    fun nextPage(onSuccess: () -> Unit, onError: (String?) -> Unit) {
        componentScope.launch {
            if (needAddResult.value == NextPackRepositoryState.POSSIBLE) {
                needAddResult.value = NextPackRepositoryState.LOAD
                lastPageRepository.value?.nextPage?.let {
                    getRepositories(request = SearchRequest.fromUrl(it),
                        onSuccess = { onSuccess.toString() },
                        onError = { mes ->
                            needAddResult.value = (NextPackRepositoryState.POSSIBLE)
                            onError.invoke(mes)
                        })
                }

            }
        }
    }

    init {
        getOwner()
        getRepositories(onSuccess = {}, onError = {})
        getUser(
            onSuccess = { Log.i("ProfileScreenDecomposeComponent", "getUser success") },
            onError = { Log.i("ProfileScreenDecomposeComponent", "getUser failed") })
    }

    private fun getOwner() {
        componentScope.launch {
            owner = managerDataStore.getOwner()
        }
    }

    private fun getUser(
        onSuccess: () -> Unit,
        onError: (String?) -> Unit
    ) {
        componentScope.launch {
            useCaseUser(UserRequest(config.user.name)).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        flowUser.emit(
                            User(
                                name = result.data!!.name,
                                avatarUrl = result.data!!.avatarUrl,
                                followersCount = result.data!!.followersCount,
                                repositoryCount = result.data!!.repositoryCount
                            )
                        )
                        onSuccess.invoke()
                    }

                    is ResultState.NetworkError -> {
                        if (result.statusCode2Int == 401) {
                            logout()
                        }
                        onError.invoke(result.error.message)
                    }

                    is ResultState.Error -> {
                        if (result.statusCode2Int == 400)
                            onError.invoke("Something went wrong, try again later")
                        else
                            onError.invoke(result.error.message)
                    }
                }
            }
        }

    }

    private fun logout() {
        componentScope.launch {
            managerDataStore.clear()
            navigationToAuth()
        }
    }

    fun getRepositories(
        request: SearchRequest = SearchRequest(config.user.name),
        onSuccess: () -> Unit,
        onError: (String?) -> Unit
    ) {
        componentScope.launch {
            useCaseRepository(request).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        val list = flowRepository.value.toMutableList()
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
                        flowRepository.emit(list)
                        Log.i("flow rep", list.toString())
                        lastPageRepository.value = page
                        needAddResult.value = (
                                if (page.nextPage != null) NextPackRepositoryState.POSSIBLE else NextPackRepositoryState.IMPOSSIBLE)
                        onSuccess.invoke()
                    }

                    is ResultState.NetworkError -> {
                        if (result.statusCode2Int == 401) {
                            logout()
                        }
                        onError.invoke(result.error.message)
                    }

                    is ResultState.Error -> {
                        if (result.statusCode2Int == 400)
                            onError.invoke("Something went wrong, try again later")
                        else
                            onError.invoke(result.error.message)
                    }
                }
            }
        }
    }


    @Composable
    override fun Render() {
        Screen(repositories = flowRepository.collectAsState().value,
            isOwner = flowUser.collectAsState().value?.name == owner?.name && flowUser.collectAsState().value?.name != null,
            user = flowUser.collectAsState().value,
            connectEthernet = networkStatus.collectAsState().value,
            managementScreen = object : ManagementScreen {
                override fun getRepositories(onSuccess: () -> Unit, onError: (String?) -> Unit) {
                    this@ProfileScreenDecomposeComponent.getRepositories(
                        onSuccess = onSuccess,
                        onError = onError
                    )
                }

                override fun nextPage(onSuccess: () -> Unit, onError: (String?) -> Unit) {
                    this@ProfileScreenDecomposeComponent.nextPage(
                        onSuccess = onSuccess,
                        onError = onError
                    )
                }

                override fun navigationToDetails(owner: String, repositoryName: String) {
                    this@ProfileScreenDecomposeComponent.navigationToDetails(owner, repositoryName)
                }

                override fun logout() {
                    this@ProfileScreenDecomposeComponent.logout()
                }

                override fun onBack() {
                    this@ProfileScreenDecomposeComponent.onBack()
                }
            })
    }

}

