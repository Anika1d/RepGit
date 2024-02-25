package com.mir.repgit.koin

import com.mir.repgit.viewmodel.MainViewModel
import org.koin.dsl.module

val viewModelModel= module {
    single { MainViewModel(searchUseCase = get()) }
}