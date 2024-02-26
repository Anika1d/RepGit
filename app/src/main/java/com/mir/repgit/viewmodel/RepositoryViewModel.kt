package com.mir.repgit.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mir.core.data.model.issues.Issues
import com.mir.core.data.model.repository.RepositoryItem
import com.mir.core.data.request.IssuesRequest
import com.mir.core.data.request.RepositoryRequest
import com.mir.core.data.response.ResultState
import com.mir.core.usecase.IssuesUseCase
import com.mir.core.usecase.RepositoryUseCase
import dev.icerock.moko.mvvm.viewmodel.ViewModel

class RepositoryViewModel(
    private val repositoryUseCase: RepositoryUseCase,
    private val issuesUseCase: IssuesUseCase,
) : ViewModel() {
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

    suspend fun getRepository(
        nameRep: String, nameOwner: String,
        onSuccess: () -> Unit, onError: (String?) -> Unit
    ) {
        repositoryUseCase.getRepository(
            RepositoryRequest(
                nameOwner = nameOwner,
                nameRepository = nameRep
            )
        ).collect { result ->
            when (result) {
                is ResultState.Success -> {
                    _repository.postValue(result.data)
                    onSuccess.invoke()
                }

                is ResultState.NetworkError -> {
                    onError.invoke(result.error.message)
                }

                is ResultState.Error -> {
                    onError.invoke(result.error.message)
                }
            }
            _dataRepositoryReceived.value=true
        }

    }

    suspend fun getIssues(
        nameRep: String,
        nameOwner: String,
        onSuccess: () -> Unit,
        onError: (String?) -> Unit
    ) {
        Log.i("issuie","send")
        issuesUseCase.getIssues(
            IssuesRequest(
                nameRepository = nameRep,
                nameOwner = nameOwner
            )
        ).collect { result ->
            when (result) {
                is ResultState.Success -> {
                    _issues.postValue( result.data)
                    Log.i("issuie","success")
                    onSuccess.invoke()
                }
                is ResultState.NetworkError -> {
                    Log.i("issuie","fail")
                    Log.i("issuie",result.error.message)
                    onError.invoke(result.error.message)
                }

                is ResultState.Error -> {
                    Log.i("issuie","fail")
                    Log.i("issuie",result.error.message.toString())
                    onError.invoke(result.error.message)
                }
            }
        }
        Log.i("issuie","end")
    }

    fun clearData() {
        _repository.postValue(null)
        _dataRepositoryReceived.postValue(false)
        _dataIssuesReceived.postValue(false)

    }
}
