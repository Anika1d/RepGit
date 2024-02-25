package com.mir.repgit.viewmodel

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

    suspend fun nextPage() {
        if (_needAddResult.value == NextPackRepositoryState.POSSIBLE) {
            _needAddResult.value = NextPackRepositoryState.LOAD
            lastSearchPage.value?.nextPage?.let {
                searchUseCase.search(SearchRequest.fromUrl(it)).collect { result ->
                    when (result) {
                        is ResultState.Success -> {
                            val page = result.data
                            val list = _repositories.value?.toMutableList() ?: mutableListOf()
                            list.addAll(page?.items?.map { it } ?: listOf())
                            _repositories.postValue(list)
                            lastSearchPage.value = page
                        }

                        is ResultState.NetworkError -> {}
                        is ResultState.Error -> {
                        }
                    }
                }
            }
            _needAddResult.value =
                if (lastSearchPage.value?.nextPage != null) NextPackRepositoryState.POSSIBLE else NextPackRepositoryState.IMPOSSIBLE
        }
    }

    suspend fun search() {
        if (!searchValue.value.isNullOrEmpty()) {
            _reloadSearch.value = LoadState.SHOW
            searchUseCase.search(SearchRequest(query = searchValue.value!!)).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        val list = _repositories.value?.toMutableList() ?: mutableListOf()
                        val page = result.data
                        list.addAll(page?.items?.map { it } ?: listOf())
                        _repositories.postValue(list)
                        lastSearchPage.value = page
                        _needAddResult.value =
                            if (page?.nextPage != null) NextPackRepositoryState.POSSIBLE else NextPackRepositoryState.IMPOSSIBLE
                    }

                    is ResultState.NetworkError -> {}
                    is ResultState.Error -> {
                    }
                }
            }
            _reloadSearch.value = LoadState.HIDE
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