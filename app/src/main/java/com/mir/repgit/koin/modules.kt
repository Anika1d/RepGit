package com.mir.repgit.koin

import com.mir.repgit.tools.network.ImplEthernetConnectivityService
import com.mir.repgit.viewmodel.MainViewModel
import com.mir.repgit.viewmodel.RepositoryViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal val networkConnectivityModule = module {
    single { ImplEthernetConnectivityService(context = androidContext()) }
}
val viewModelModel = module {
    includes(networkConnectivityModule)
    single {
        MainViewModel(
            searchUseCase = get(),
            insertSearchQueryUseCase = get(),
            deleteSearchQueryUseCase = get(),
            getSearchQueryUseCase = get(),
            implEthernetConnectivityService = get()
        )
    }
    single {
        RepositoryViewModel(
            issuesUseCase = get(),
            repositoryUseCase = get(),
            implEthernetConnectivityService = get()
        )
    }
}