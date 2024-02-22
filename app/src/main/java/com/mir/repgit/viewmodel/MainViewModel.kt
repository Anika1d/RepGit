package com.mir.repgit.viewmodel

import androidx.lifecycle.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.delay

class MainViewModel:ViewModel() {
    val firstSetupApp= MutableLiveData(true)
    val searchValue= MutableLiveData("")
    val activeSearch=MutableLiveData(false)

    val reloadSearch=MutableLiveData(false)
    fun closeWelcomeWindow(b: Boolean) {
        firstSetupApp.value=b
    }

    fun changeActiveSearch(b: Boolean) {
        activeSearch.value=b
    }

   suspend fun changeSearchValue(s: String) {
       searchValue.value=s
       reloadSearch.value=true
       /////network
       delay(1000)
       reloadSearch.value=false
    }


}