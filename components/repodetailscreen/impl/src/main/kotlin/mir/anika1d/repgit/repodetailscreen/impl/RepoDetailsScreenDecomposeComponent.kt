package mir.anika1d.repgit.repodetailscreen.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mir.anika1d.core.decompose.DecomposeOnBackParameter
import mir.anika1d.core.network.connection.service.ConnectivityState
import mir.anika1d.core.network.connection.service.INetworkConnectivityService
import mir.anika1d.repgit.core.data.model.issues.Issues
import mir.anika1d.repgit.core.data.model.repository.Owner
import mir.anika1d.repgit.core.data.model.repository.RepositoryItem
import mir.anika1d.repgit.datastore.api.ACManagerDataStore
import mir.anika1d.repgit.downloadlogic.service.api.IDownloadService
import mir.anika1d.repgit.downloadlogic.service.data.request.DownloadRequest
import mir.anika1d.repgit.network.core.data.request.IssuesRequest
import mir.anika1d.repgit.network.core.data.request.RepositoryRequest
import mir.anika1d.repgit.network.core.data.response.ResultState
import mir.anika1d.repgit.network.usecases.issues.IssuesUseCase
import mir.anika1d.repgit.network.usecases.repository.RepositoryUseCase
import mir.anika1d.repgit.repodetailscreen.api.ACRepoDetailsScreenDecomposeComponent
import mir.anika1d.repgit.repodetailscreen.api.model.DetailsRepoConfig
import mir.anika1d.repgit.repodetailscreen.impl.screen.Screen
import mir.anika1d.repgit.repodetailscreen.impl.screen.management.ManagementScreen


internal class RepoDetailsScreenDecomposeComponent(
    componentContext: ComponentContext,
    private val onBack: DecomposeOnBackParameter,
    private val config: DetailsRepoConfig,
    private val repositoryUseCase: RepositoryUseCase,
    private val issuesUseCase: IssuesUseCase,
    private val downloadService: IDownloadService,
    private val managerDataStore: ACManagerDataStore,
    private val navigationToAuth: () -> Unit,
    networkConnectivityService: INetworkConnectivityService
) : ACRepoDetailsScreenDecomposeComponent(componentContext) {

    private val networkStatus: StateFlow<ConnectivityState> =
        networkConnectivityService.connectivityState.stateIn(
            initialValue = ConnectivityState.Undefined,
            scope = componentScope,
            started = SharingStarted.WhileSubscribed(5000)
        )


    private val _repository: MutableStateFlow<RepositoryItem?> = MutableStateFlow(null)


    private val _issues: MutableStateFlow<List<Issues>?> = MutableStateFlow(null)


    private val _dataRepositoryReceived: MutableStateFlow<Boolean> = MutableStateFlow(false)


    private val _dataIssuesReceived: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private fun logout() {
        componentScope.launch {
            managerDataStore.clear()
            navigationToAuth()
        }
    }

    fun download() {
        componentScope.launch {
            val token = managerDataStore.getJwtToken()
            if (token != null)
                _repository.value?.let {
                    downloadService.downloadFile(
                        DownloadRequest(
                            name = it.name,
                            url = it.downloadUrl,
                            owner = it.owner.name,
                            jwtToken = token
                        )
                    )
                }
            else {
                logout()
            }
        }

    }

    private fun getRepository(
        nameRep: String, nameOwner: String,
        onSuccess: () -> Unit, onError: (String?) -> Unit
    ) {
        componentScope.launch {
            repositoryUseCase(
                RepositoryRequest(
                    nameOwner = nameOwner,
                    nameRepository = nameRep
                )
            ).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        result.data?.let {
                            _repository.emit(
                                RepositoryItem(
                                    owner = Owner(
                                        name = it.owner.name,
                                        avatarUrl = it.owner.avatarUrl,
                                    ), issuesCount = it.issuesCount,
                                    watchersCount = it.watchersCount,
                                    forkCount = it.forkCount,
                                    starCount = it.starCount,
                                    name = it.name,
                                    description = it.description,
                                    url = it.url,
                                    downloadUrl = it.downloadUrl,
                                    defaultBranch = it.defaultBranch,
                                    language = it.language,
                                    updatedAt = it.updatedAt
                                )
                            )
                            onSuccess.invoke()
                        }
                    }

                    is ResultState.NetworkError -> {
                        if (result.statusCode2Int == 401) logout()
                        onError.invoke(result.error.message)
                    }

                    is ResultState.Error -> {
                        onError.invoke(result.error.message)
                    }
                }
                _dataRepositoryReceived.emit(true)
            }

        }
    }

    private fun getIssues(
        nameRep: String,
        nameOwner: String,
        onSuccess: () -> Unit,
        onError: (String?) -> Unit
    ) {
        componentScope.launch {
            issuesUseCase(
                IssuesRequest(
                    nameRepository = nameRep,
                    nameOwner = nameOwner
                )
            ).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _issues.emit(result.data?.map {
                            Issues(
                                body = it.body,
                                comments = it.comments,
                                state = it.state,
                                title = it.title,
                                updatedAt = it.updatedAt,
                                createdAt = it.createdAt,
                                url = it.url,
                                id = it.id
                            )
                        })
                        onSuccess.invoke()
                    }

                    is ResultState.NetworkError -> {
                        if (result.statusCode2Int == 401) logout()
                        onError.invoke(result.error.message)
                    }

                    is ResultState.Error -> {
                        onError.invoke(result.error.message)
                    }
                }
                _dataIssuesReceived.emit(true)
            }
        }
    }

    fun clearData() {
        _repository.value = null
        _dataRepositoryReceived.value = false
        _dataIssuesReceived.value = false

    }

    fun getAllInfoRep(
        owner: String, repo: String,
        onError: (String?) -> Unit,
        onSuccess: () -> Unit
    ) {
        var repError = ""
        var issError = ""
        getRepository(nameOwner = owner,
            nameRep = repo,
            onSuccess = {},
            onError = { repError = it.toString() })
        getIssues(nameOwner = owner,
            nameRep = repo,
            onSuccess = { },
            onError = { issError = it.toString() })
        if (_dataIssuesReceived.value && _dataRepositoryReceived.value) {
            onSuccess.invoke()
        } else {
            if (repError == issError) onError.invoke(issError)
            else onError.invoke("$repError $issError")

        }
    }


    @Composable
    override fun Render() {
        Screen(
            managementScreen = object : ManagementScreen {
                override fun clearData() {
                    this@RepoDetailsScreenDecomposeComponent.clearData()
                }

                override fun download() {
                    this@RepoDetailsScreenDecomposeComponent.download()
                }

                override fun onBack() {
                    this@RepoDetailsScreenDecomposeComponent.onBack()
                }

                override fun getAllInfoRep(
                    owner: String,
                    repo: String,
                    onSuccess: () -> Unit,
                    onError: (String?) -> Unit
                ) {
                    this@RepoDetailsScreenDecomposeComponent.getAllInfoRep(
                        owner = owner, repo = repo,
                        onSuccess = onSuccess, onError = onError
                    )
                }

            },
            issues = _issues.collectAsState().value,
            connectNetwork = networkStatus.collectAsState().value,
            repository = _repository.collectAsState().value,
            config = config,
            repLog = (_dataIssuesReceived.collectAsState().value && _dataRepositoryReceived.collectAsState().value)
        )
    }
}