package mir.anika1d.repgit.rootscreen.impl.koin

import mir.anika1d.repgit.authscreen.impl.koin.authScreenDecomposeComponentModule
import mir.anika1d.repgit.datastore.koin.managerDataStoreModule
import mir.anika1d.repgit.downloadscreen.impl.koin.downloadScreenComponentFactoryModule
import mir.anika1d.repgit.network.usecases.auth.koin.getTokenUseCaseModule
import mir.anika1d.repgit.network.usecases.users.koin.userUseCaseModule
import mir.anika1d.repgit.profilescreen.impl.koin.profileScreenComponentFactoryModule
import mir.anika1d.repgit.repodetailscreen.impl.koin.repoDetailsComponentFactoryModule
import mir.anika1d.repgit.rootscreen.api.ACRootDecomposeComponent
import mir.anika1d.repgit.rootscreen.impl.RootDecomposeComponent
import mir.anika1d.repgit.searchscreen.impl.koin.searchScreenComponentFactoryModule
import org.koin.dsl.module


val rootComponentFactoryModule = module {

    includes(
        profileScreenComponentFactoryModule,
        repoDetailsComponentFactoryModule,
        searchScreenComponentFactoryModule,
        authScreenDecomposeComponentModule,
        downloadScreenComponentFactoryModule,
        managerDataStoreModule,
        getTokenUseCaseModule,
        userUseCaseModule
    )
    single<ACRootDecomposeComponent.Factory> {
        ACRootDecomposeComponent.Factory { componentContext, onBack, callback ->
            RootDecomposeComponent(
                applicationContext = get(),
                onBack = onBack,
                searchScreenFactory = get(),
                repoDetailsScreenFactory = get(),
                downloadScreenFactory = get(),
                authScreenFactory = get(),
                profileScreenFactory = get(),
                callbackRegisterActivity = callback,
                componentContext = componentContext,
                managerDataStore = get(),
                useCaseGetTokens = get(),
                useCaseUser = get(),
            )
        }
    }
}