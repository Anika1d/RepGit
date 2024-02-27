package com.mir.database.koin

import androidx.room.Room
import com.mir.database.repository.ImplSearchQueryRepository
import com.mir.database.room.AppDatabase
import com.mir.database.usecase.DeleteSearchQueryUseCase
import com.mir.database.usecase.GetSearchQueryUseCase
import com.mir.database.usecase.InsertSearchQueryUseCase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule =
    module {
        single {
            Room.databaseBuilder(
                androidApplication(),
                AppDatabase::class.java,
                "search_query_database"
            ).build()
        }
    }
val daosModule = module {
    includes(databaseModule)
    single { get<AppDatabase>().searchQueryDao() }
}
val repositoryModule = module {
    includes(daosModule)
    single { ImplSearchQueryRepository(dao = get()) }
}
val useCaseDatabaseModule = module {
    includes(repositoryModule)
    single { DeleteSearchQueryUseCase(repository = get()) }
    single { GetSearchQueryUseCase(repository = get()) }
    single { InsertSearchQueryUseCase(repository = get()) }
}