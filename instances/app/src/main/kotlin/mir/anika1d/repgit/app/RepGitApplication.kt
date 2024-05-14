package mir.anika1d.repgit.app

import android.app.Application
import mir.anika1d.core.network.connection.service.INetworkConnectivityService
import mir.anika1d.repgit.app.networkManager.ImplNetworkConnectivityService
import mir.anika1d.repgit.datastore.api.ACManagerDataStore
import mir.anika1d.repgit.datastore.impl.ManagerDataStore
import mir.anika1d.repgit.datastore.koin.managerDataStoreModule
import mir.anika1d.repgit.downloadlogic.service.api.IDownloadService
import mir.anika1d.repgit.downloadlogic.service.impl.DownloadService
import mir.anika1d.repgit.rootscreen.impl.koin.rootComponentFactoryModule
import net.openid.appauth.AuthorizationService
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

class RepGitApplication : Application() {
    init {
        startKoin {
            androidContext(this@RepGitApplication)
            modules(
                module { singleOf(::ImplNetworkConnectivityService) { bind<INetworkConnectivityService>() } },
                module { singleOf(::DownloadService) { bind<IDownloadService>() } },
                module { single { AuthorizationService(get()) } },
                managerDataStoreModule,
                rootComponentFactoryModule,
            )
        }
    }

}

