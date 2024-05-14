package mir.anika1d.repgit.downloadscreen.impl.koin

import mir.anika1d.repgit.downloadscreen.api.ACDownloadScreenDecomposeComponent
import mir.anika1d.repgit.downloadscreen.impl.DownloadScreenDecomposeComponent
import org.koin.dsl.module


val downloadScreenComponentFactoryModule = module {
    single {
        ACDownloadScreenDecomposeComponent.Factory { componentContext,
                                                     onBackParameter,
                                                     navigationToDetails ->
            DownloadScreenDecomposeComponent(
                componentContext, onBackParameter, navigationToDetails
            )
        }
    }
}
