package mir.anika1d.repgit.network.usecases.repository.koin

import mir.anika1d.repgit.network.github.repositories.koin.repositoryModule
import mir.anika1d.repgit.network.usecases.repository.RepositoryUseCase
import org.koin.dsl.module
val repositoryUseCaseModule = module {
    includes(repositoryModule)
    factory { RepositoryUseCase(repository = get ()) }

}