package com.mir.repgit.viewmodel

import android.util.Log
import com.mir.core.data.model.repository.PageRepository
import com.mir.core.data.model.repository.RepositoryItem
import com.mir.core.data.request.SearchRequest
import com.mir.core.data.response.ResultState
import com.mir.core.usecase.SearchUseCase
import com.mir.database.data.model.SearchQuery
import com.mir.database.data.request.SearchQueryRequest
import com.mir.database.usecase.DeleteSearchQueryUseCase
import com.mir.database.usecase.GetSearchQueryUseCase
import com.mir.database.usecase.InsertSearchQueryUseCase
import com.mir.repgit.tools.LoadState
import com.mir.repgit.tools.NextPackRepositoryState
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.launch

class MainViewModel(
    private val searchUseCase: SearchUseCase,
    private val deleteSearchQueryUseCase: DeleteSearchQueryUseCase,
    private val insertSearchQueryUseCase: InsertSearchQueryUseCase,
    private val getSearchQueryUseCase: GetSearchQueryUseCase,
) : ViewModel() {

    private val _searchQueries: MutableLiveData<List<SearchQuery>> = MutableLiveData(emptyList())
    val searchQueries: LiveData<List<SearchQuery>>
        get() = _searchQueries

    private val _repositories: MutableLiveData<List<RepositoryItem>> = MutableLiveData(emptyList())
    val repositories: LiveData<List<RepositoryItem>>
        get() = _repositories

    private val lastSearchPage: MutableLiveData<PageRepository?> = MutableLiveData(null)
    val countResult: LiveData<Int>
        get() = lastSearchPage.map { it?.totalCount ?: 0 }

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

    fun nextPage(onSuccess: () -> Unit, onError: (String?) -> Unit) {
        viewModelScope.launch {
            if (_needAddResult.value == NextPackRepositoryState.POSSIBLE) {
                _needAddResult.value = NextPackRepositoryState.LOAD
                lastSearchPage.value?.nextPage?.let {
                    search(searchR = SearchRequest.fromUrl(it),
                        onSuccess = { onSuccess.toString() },
                        onError = { mes ->
                            _needAddResult.value = (NextPackRepositoryState.POSSIBLE)
                            onError.invoke(mes)
                        })
                }

            }
        }
    }

    fun getSearchValue(s: String) {
        viewModelScope.launch {
            getSearchQueryUseCase.invoke(SearchQueryRequest(name = s, limit = 5)).collect {
                _searchQueries.value = it
            }
        }
    }

    fun deleteSearchValue(searchQuery: SearchQuery) {
        viewModelScope.launch {
            deleteSearchQueryUseCase.deleteSearchQuery(SearchQueryRequest(searchQuery.name))
            getSearchValue(_searchValue.value)
        }
    }

    private fun insertSearchValue() {
        viewModelScope.launch {
            insertSearchQueryUseCase.insertSearchQuery(SearchQueryRequest(searchValue.value))
        }
    }


    fun search(
        searchR: SearchRequest? = null, onSuccess: () -> Unit, onError: (String?) -> Unit
    ) {
        insertSearchValue()
        viewModelScope.launch {
            val sR: SearchRequest? = searchR
                ?: if (searchValue.value.isEmpty()) null else SearchRequest(query = searchValue.value)
            if (sR != null) {
                _reloadSearch.value = LoadState.SHOW
                Log.i("vmv", "request send")
                searchUseCase.search(sR).collect { result ->
                    when (result) {
                        is ResultState.Success -> {
                            val list = _repositories.value.toMutableList()
                            val page = result.data
                            list.addAll(page?.items?.map { it } ?: listOf())
                            _repositories.value = list
                            lastSearchPage.value = page
                            _needAddResult.value =
                                if (page?.nextPage != null) NextPackRepositoryState.POSSIBLE else NextPackRepositoryState.IMPOSSIBLE
                            onSuccess.invoke()
                        }

                        is ResultState.NetworkError -> {
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
                _reloadSearch.value = LoadState.HIDE
                Log.i("vmv", "request end")
            }
        }
    }

    fun changeActiveSearch(b: Boolean) {
        _activeSearch.value = b
    }

    fun changeSearchValue(s: String) {
        viewModelScope.launch {
            _searchValue.value = s
            _repositories.value = listOf()
            lastSearchPage.value = null
            getSearchValue(_searchValue.value)

        }
    }

    fun clearSearchQueries() {
        _searchQueries.value = listOf()
    }

}
