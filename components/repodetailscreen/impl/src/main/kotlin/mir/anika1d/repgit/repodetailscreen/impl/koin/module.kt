package mir.anika1d.repgit.repodetailscreen.impl.koin

import mir.anika1d.repgit.datastore.koin.managerDataStoreModule
import mir.anika1d.repgit.network.usecases.issues.koin.issuesUseCaseModule
import mir.anika1d.repgit.network.usecases.repository.koin.repositoryUseCaseModule
import mir.anika1d.repgit.repodetailscreen.api.ACRepoDetailsScreenDecomposeComponent
import mir.anika1d.repgit.repodetailscreen.impl.RepoDetailsScreenDecomposeComponent
import org.koin.dsl.module

val repoDetailsComponentFactoryModule = module {
    includes(
        issuesUseCaseModule,
        repositoryUseCaseModule,
        managerDataStoreModule,
    )
    single {
        ACRepoDetailsScreenDecomposeComponent.Factory { componentContext, onBack, config, navigationToAuth ->
            RepoDetailsScreenDecomposeComponent(
                componentContext = componentContext,
                onBack = onBack,
                config = config,
                issuesUseCase = get(),
                repositoryUseCase = get(),
                managerDataStore = get(),
                downloadService = get(),
                networkConnectivityService = get(),
                navigationToAuth = navigationToAuth
            )
        }
    }
}
