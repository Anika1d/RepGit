package com.mir.repgit

import android.app.Application
import org.koin.core.context.startKoin
import com.mir.core.koin.useCasesCoreModule
import com.mir.database.koin.useCaseDatabaseModule
import com.mir.repgit.koin.viewModelModel
import org.koin.android.ext.koin.androidContext

class App : Application() {
    init {
        startKoin {
            androidContext(this@App)
            modules(
                viewModelModel,
                useCasesCoreModule,
                useCaseDatabaseModule,
            )
        }
    }

}