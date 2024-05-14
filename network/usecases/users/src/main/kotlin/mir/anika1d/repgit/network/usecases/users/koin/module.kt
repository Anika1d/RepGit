package mir.anika1d.repgit.network.usecases.users.koin

import mir.anika1d.repgit.network.github.repositories.koin.repositoryModule
import mir.anika1d.repgit.network.usecases.users.GetUserUseCase
import org.koin.dsl.module

val userUseCaseModule= module {
    includes(repositoryModule)
    single { GetUserUseCase(get()) }
}