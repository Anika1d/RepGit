package mir.anika1d.repgit.network.usecases.auth.koin

import mir.anika1d.repgit.network.github.repositories.koin.repositoryModule
import mir.anika1d.repgit.network.usecases.auth.AuthUseCase
import mir.anika1d.repgit.network.usecases.auth.GetTokensUseCase
import org.koin.dsl.module

val authUseCaseModule = module {
    includes(repositoryModule)
    single { AuthUseCase(get()) }
}

val getTokenUseCaseModule = module {
    includes(repositoryModule)
    single {GetTokensUseCase(get()) }
}