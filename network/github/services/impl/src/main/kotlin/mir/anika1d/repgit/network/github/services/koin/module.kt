package mir.anika1d.repgit.network.github.services.koin

import mir.anika1d.repgit.network.core.client.ktor.module.ktorClientModule
import mir.anika1d.repgit.network.github.services.IGithubServices
import mir.anika1d.repgit.network.github.services.ImplGithubServices
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val servicesModule =  module {
    includes(ktorClientModule)
    singleOf(::ImplGithubServices) { bind<IGithubServices>()}
}