package mir.anika1d.repgit.datastore.koin

import mir.anika1d.repgit.datastore.impl.ManagerDataStore
import mir.anika1d.repgit.datastore.api.ACManagerDataStore
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val managerDataStoreModule = module {
    singleOf(::ManagerDataStore) { bind<ACManagerDataStore>() }
}