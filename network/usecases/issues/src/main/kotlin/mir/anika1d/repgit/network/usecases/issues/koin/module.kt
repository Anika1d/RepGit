package mir.anika1d.repgit.network.usecases.issues.koin

import mir.anika1d.repgit.network.github.repositories.koin.repositoryModule
import mir.anika1d.repgit.network.usecases.issues.IssuesUseCase
import org.koin.dsl.module

val issuesUseCaseModule = module {
    includes(repositoryModule)
    factory { IssuesUseCase(repository = get()) }

}