package mir.anika1d.repgit.network.core.client.ktor.module

import mir.anika1d.repgit.datastore.koin.managerDataStoreModule
import mir.anika1d.repgit.network.core.client.ktor.KtorClient
import org.koin.dsl.module

val ktorClientModule = module {
    includes(managerDataStoreModule)

    single {
        KtorClient( managerDataStore = get(), authService = get(),).provide()
    }
}