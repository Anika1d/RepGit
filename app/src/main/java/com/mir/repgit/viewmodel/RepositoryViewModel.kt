package com.mir.repgit.viewmodel

import android.util.Log
import com.mir.core.data.model.issues.Issues
import com.mir.core.data.model.repository.RepositoryItem
import com.mir.core.data.request.IssuesRequest
import com.mir.core.data.request.RepositoryRequest
import com.mir.core.data.response.ResultState
import com.mir.core.usecase.IssuesUseCase
import com.mir.core.usecase.RepositoryUseCase
import com.mir.repgit.tools.network.ConnectivityState
import com.mir.repgit.tools.network.ImplEthernetConnectivityService
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RepositoryViewModel(
    private val repositoryUseCase: RepositoryUseCase,
    private val issuesUseCase: IssuesUseCase,
    private val implEthernetConnectivityService: ImplEthernetConnectivityService
) : ViewModel() {
    val networkStatus: StateFlow<ConnectivityState> = implEthernetConnectivityService.connectivityState.stateIn(
        initialValue = ConnectivityState.Undefined,
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )


    private val _repository: MutableLiveData<RepositoryItem?> = MutableLiveData(null)
    val repository: LiveData<RepositoryItem?>
        get() = _repository

    private val _issues: MutableLiveData<List<Issues>?> = MutableLiveData(null)
    val issues: LiveData<List<Issues>?>
        get() = _issues


    private val _dataRepositoryReceived: MutableLiveData<Boolean> = MutableLiveData(false)
    val dataRepositoryReceived: LiveData<Boolean>
        get() = _dataRepositoryReceived

    private val _dataIssuesReceived: MutableLiveData<Boolean> = MutableLiveData(false)
    val dataIssuesReceived: LiveData<Boolean>
        get() = _dataIssuesReceived

  private  fun getRepository(
        nameRep: String, nameOwner: String,
        onSuccess: () -> Unit, onError: (String?) -> Unit
    ) {
        viewModelScope.launch {
            repositoryUseCase.getRepository(
                RepositoryRequest(
                    nameOwner = nameOwner,
                    nameRepository = nameRep
                )
            ).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _repository.value = result.data
                        onSuccess.invoke()
                    }

                    is ResultState.NetworkError -> {
                        onError.invoke(result.error.message)
                    }

                    is ResultState.Error -> {
                        onError.invoke(result.error.message)
                    }
                }
                _dataRepositoryReceived.value = true
            }

        }
    }

   private fun getIssues(
        nameRep: String,
        nameOwner: String,
        onSuccess: () -> Unit,
        onError: (String?) -> Unit
    ) {
        viewModelScope.launch {
            issuesUseCase.getIssues(
                IssuesRequest(
                    nameRepository = nameRep,
                    nameOwner = nameOwner
                )
            ).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _issues.value = result.data
                        Log.i("issuie", "success")
                        onSuccess.invoke()
                    }

                    is ResultState.NetworkError -> {
                        Log.i("issuie", "fail")
                        Log.i("issuie", result.error.message)
                        onError.invoke(result.error.message)
                    }

                    is ResultState.Error -> {
                        Log.i("issuie", "fail")
                        Log.i("issuie", result.error.message.toString())
                        onError.invoke(result.error.message)
                    }
                }
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
}
