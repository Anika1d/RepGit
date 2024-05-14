package mir.anika1d.repgit.authscreen.impl.koin

import mir.anika1d.repgit.authscreen.api.ACAuthScreenDecomposeComponent
import mir.anika1d.repgit.authscreen.impl.AuthScreenDecomposeComponent
import mir.anika1d.repgit.network.usecases.auth.koin.authUseCaseModule
import org.koin.dsl.module

val authScreenDecomposeComponentModule = module {
    includes(authUseCaseModule)
    single {
        ACAuthScreenDecomposeComponent.Factory { componentContext,
                                                 onBackParameter,
                                                 callback ->
            AuthScreenDecomposeComponent(
                componentContext = componentContext,
                onBack = onBackParameter,
                authService = get(),
                useCaseAuth = get(),
                callbackActivityResultContracts = callback,
            )
        }
    }
}
