package mir.anika1d.repgit.database.koin

import androidx.room.Room
import mir.anika1d.repgit.database.api.ISearchQueryRepository
import mir.anika1d.repgit.database.impl.ImplSearchQueryRepository
import mir.anika1d.repgit.database.room.AppDatabase
import mir.anika1d.repgit.database.usecase.DeleteSearchQueryUseCase
import mir.anika1d.repgit.database.usecase.GetSearchQueryUseCase
import mir.anika1d.repgit.database.usecase.InsertSearchQueryUseCase
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


internal val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(), AppDatabase::class.java, "search_query_database"
        ).build()
    }
}


internal val daosModule = module {
    includes(databaseModule)
    single { get<AppDatabase>().searchQueryDao() }
}
internal val repositoryDModule = module {
    includes(daosModule)
    singleOf(::ImplSearchQueryRepository) { bind<ISearchQueryRepository>() }
}
val useCaseDatabaseModule = module {
    includes(repositoryDModule)
    single { DeleteSearchQueryUseCase(repository = get()) }
    single { GetSearchQueryUseCase(repository = get()) }
    single { InsertSearchQueryUseCase(repository = get()) }
}