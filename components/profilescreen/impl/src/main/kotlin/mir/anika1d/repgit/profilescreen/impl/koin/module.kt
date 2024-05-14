package mir.anika1d.repgit.profilescreen.impl.koin

import mir.anika1d.repgit.network.usecases.search.koin.searchUseCaseModule
import mir.anika1d.repgit.network.usecases.users.koin.userUseCaseModule
import mir.anika1d.repgit.profilescreen.api.ACProfileScreenDecomposeComponent
import mir.anika1d.repgit.profilescreen.impl.ProfileScreenDecomposeComponent
import org.koin.dsl.module

val profileScreenComponentFactoryModule = module {
    includes(searchUseCaseModule, userUseCaseModule  )
    single {
        ACProfileScreenDecomposeComponent.Factory { componentContext, onBack, config, navigationToDetails,
                                                    navigationToAuth ->
            ProfileScreenDecomposeComponent(
                componentContext = componentContext,
                onBack = onBack,
                config = config,
                navigationToDetails = navigationToDetails,
                navigationToAuth = navigationToAuth,
                useCaseRepository = get(),
                useCaseUser = get(),
                networkConnectivityService = get(),
                managerDataStore = get(),
            )
        }
    }
}