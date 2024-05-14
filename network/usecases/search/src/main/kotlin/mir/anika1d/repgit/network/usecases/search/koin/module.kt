package mir.anika1d.repgit.network.usecases.search.koin

import mir.anika1d.repgit.network.github.repositories.koin.repositoryModule
import mir.anika1d.repgit.network.usecases.search.SearchRepositoryAllUseCase
import mir.anika1d.repgit.network.usecases.search.SearchRepositoryByUserUseCase
import mir.anika1d.repgit.network.usecases.search.SearchUserUseCase
import org.koin.dsl.module

val searchUseCaseModule = module {
    includes(repositoryModule)
    factory { SearchRepositoryAllUseCase(repository = get()) }
    factory { SearchRepositoryByUserUseCase(repository = get()) }
    factory { SearchUserUseCase(repository = get()) }
}