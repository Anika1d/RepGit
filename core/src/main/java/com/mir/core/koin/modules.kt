package com.mir.core.koin

import com.mir.core.ktor.KtorClient
import com.mir.core.repository.ImplGithubRepository
import com.mir.core.usecase.IssuesUseCase
import com.mir.core.usecase.RepositoryUseCase
import com.mir.core.usecase.SearchUseCase
import org.koin.dsl.module

val coreModule = module {
    single { KtorClient.provide() }
    single { ImplGithubRepository(client = get()) }
}

val useCasesCoreModule = module {
    includes(coreModule)
    factory { SearchUseCase(repository = get()) }
    factory { IssuesUseCase(repository = get()) }
    factory { RepositoryUseCase(repository = get()) }
}