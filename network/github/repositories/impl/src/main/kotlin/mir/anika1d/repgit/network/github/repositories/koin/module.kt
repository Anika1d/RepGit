package mir.anika1d.repgit.network.github.repositories.koin

import mir.anika1d.repgit.network.github.repositories.IGithubRepository
import mir.anika1d.repgit.network.github.repositories.ImplGithubRepository
import mir.anika1d.repgit.network.github.services.koin.servicesModule
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule =  module {
    includes(servicesModule)
    singleOf(::ImplGithubRepository) { bind<IGithubRepository>()}
}