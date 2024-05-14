package mir.anika1d.repgit.searchscreen.impl.koin

import mir.anika1d.repgit.database.koin.useCaseDatabaseModule
import mir.anika1d.repgit.network.usecases.search.koin.searchUseCaseModule
import mir.anika1d.repgit.searchscreen.api.ACSearchScreenDecomposeComponent
import mir.anika1d.repgit.searchscreen.impl.SearchScreenDecomposeComponent
import org.koin.dsl.module

val searchScreenComponentFactoryModule = module {
    includes(
        searchUseCaseModule,
        useCaseDatabaseModule,
    )
    single {
        ACSearchScreenDecomposeComponent.Factory { componentContext, navigationToDetails, navigationToDownload,
                                                   navigationToAuth, navigationToProfile ->
            SearchScreenDecomposeComponent(
                context = componentContext,
                navigationToDetails = navigationToDetails,
                navigationToDownload = navigationToDownload,
                navigationToAuth = navigationToAuth,
                navigationToProfile = navigationToProfile,
                searchRepositoryAllUseCase = get(),
                searchRepositoryByUserUseCase = get(),
                searchUserUseCase = get(),
                networkConnectivityService = get(),
                insertSearchQueryUseCase = get(),
                deleteSearchQueryUseCase = get(),
                getSearchQueryUseCase = get(),
                managerDataStore = get(),
            )
        }
    }
}

