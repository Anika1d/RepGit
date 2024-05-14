package mir.anika1d.repgit.authscreen.api

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.arkivanov.decompose.ComponentContext
import mir.anika1d.core.decompose.DecomposeComponent
import mir.anika1d.core.decompose.DecomposeOnBackParameter

abstract class ACAuthScreenDecomposeComponent(componentContext: ComponentContext) :
    DecomposeComponent(componentContext) {
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onBack: DecomposeOnBackParameter,
            callbackActivityResultContracts: ActivityResultLauncher<Intent>,
        ): ACAuthScreenDecomposeComponent
    }
}