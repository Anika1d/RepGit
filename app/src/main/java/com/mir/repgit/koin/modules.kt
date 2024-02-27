package com.mir.repgit.koin

import com.mir.repgit.viewmodel.MainViewModel
import com.mir.repgit.viewmodel.RepositoryViewModel
import org.koin.dsl.module

val viewModelModel = module {
    single {
        MainViewModel(
            searchUseCase = get(),
            insertSearchQueryUseCase = get(),
            deleteSearchQueryUseCase = get(),
            getSearchQueryUseCase = get()
        )
    }
    single {
        RepositoryViewModel(
            issuesUseCase = get(),
            repositoryUseCase = get()
        )
    }
}