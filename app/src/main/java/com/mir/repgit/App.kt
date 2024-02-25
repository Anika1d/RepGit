package com.mir.repgit

import android.app.Application
import org.koin.core.context.startKoin
import com.mir.core.koin.coreModule
import com.mir.core.koin.useCasesModule
import com.mir.repgit.koin.viewModelModel

class App : Application() {
    init {
        startKoin {
            modules(
                coreModule,
                viewModelModel,
                useCasesModule,
            )
        }
    }

}