package com.mir.repgit.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mir.core.data.model.repository.PageRepository
import com.mir.core.data.model.repository.RepositoryItem
import com.mir.core.data.request.SearchRequest
import com.mir.core.data.response.ResultState
import com.mir.core.usecase.SearchUseCase
import com.mir.repgit.tools.LoadState
import com.mir.repgit.tools.NextPackRepositoryState
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.delay

class MainViewModel(
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    private val _repositories: MutableLiveData<List<RepositoryItem>> = MutableLiveData(emptyList())
    val repositories: LiveData<List<RepositoryItem>>
        get() = _repositories

    private val lastSearchPage: MutableLiveData<PageRepository?> = MutableLiveData(null)


    private val _needAddResult: MutableLiveData<NextPackRepositoryState> =
        MutableLiveData(NextPackRepositoryState.IMPOSSIBLE)
    val needAddResult: LiveData<NextPackRepositoryState>
        get() = _needAddResult


    private val _firstSetupApp = MutableLiveData(false)
    val firstSetupApp: LiveData<Boolean>
        get() = _firstSetupApp
    private val _searchValue = MutableLiveData("")
    val searchValue: LiveData<String>
        get() = _searchValue
    private val _activeSearch = MutableLiveData(false)
    val activeSearch: LiveData<Boolean>
        get() = _activeSearch

    private val _reloadSearch: MutableLiveData<LoadState> = MutableLiveData(LoadState.HIDE)
    val reloadSearch: LiveData<LoadState>
        get() = _reloadSearch

    fun closeWelcomeWindow(b: Boolean) {
        _firstSetupApp.value = b
    }

    suspend fun nextPage(onSuccess: () -> Unit, onError: (String?) -> Unit) {
        delay(300)
        if (_needAddResult.value == NextPackRepositoryState.POSSIBLE) {
            _needAddResult.value = NextPackRepositoryState.LOAD
            lastSearchPage.value?.nextPage?.let {
                search(searchR = SearchRequest.fromUrl(it),
                    onSuccess = { onSuccess.toString() },
                    onError = { mes -> onError.invoke(mes) })
            }
            _needAddResult.value =
                if (lastSearchPage.value?.nextPage != null) NextPackRepositoryState.POSSIBLE else NextPackRepositoryState.IMPOSSIBLE
        }
    }

    suspend fun search(
        searchR: SearchRequest? = null, onSuccess: () -> Unit, onError: (String?) -> Unit
    ) {
        val sR: SearchRequest? = searchR
            ?: if (searchValue.value.isNullOrEmpty()) null else SearchRequest(query = searchValue.value!!)
        if (sR != null) {
            _reloadSearch.value = LoadState.SHOW
            Log.i("vmv", "request send")
            searchUseCase.search(sR).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        val list = _repositories.value?.toMutableList() ?: mutableListOf()
                        val page = result.data
                        list.addAll(page?.items?.map { it } ?: listOf())
                        _repositories.postValue(list)
                        lastSearchPage.value = page
                        _needAddResult.value =
                            if (page?.nextPage != null) NextPackRepositoryState.POSSIBLE else NextPackRepositoryState.IMPOSSIBLE
                        onSuccess.invoke()
                    }

                    is ResultState.NetworkError -> {
                        onError.invoke(result.error.message)
                    }
                    is ResultState.Error -> {
                        onError.invoke(result.error.message)
                    }
                }
            }
            _reloadSearch.value = LoadState.HIDE
            Log.i("vmv", "request end")

        }
    }

    fun changeActiveSearch(b: Boolean) {
        _activeSearch.value = b
    }

    fun changeSearchValue(s: String) {
        _searchValue.value = s
        _repositories.value = listOf()
    }

}
