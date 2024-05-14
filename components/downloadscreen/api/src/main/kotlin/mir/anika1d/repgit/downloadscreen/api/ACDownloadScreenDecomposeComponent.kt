package mir.anika1d.repgit.downloadscreen.api

import com.arkivanov.decompose.ComponentContext
import mir.anika1d.core.decompose.DecomposeComponent
import mir.anika1d.core.decompose.DecomposeOnBackParameter


abstract class ACDownloadScreenDecomposeComponent(context: ComponentContext) :
    DecomposeComponent(context) {
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onBack: DecomposeOnBackParameter,
            navigationToDetails: (String, String) -> Unit,
        ): ACDownloadScreenDecomposeComponent
    }
}