package mir.anika1d.repgit.searchscreen.api

import com.arkivanov.decompose.ComponentContext
import mir.anika1d.core.decompose.DecomposeComponent

abstract class ACSearchScreenDecomposeComponent(context: ComponentContext) :
    DecomposeComponent(context) {
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            navigationToDetails: (String, String) -> Unit,
            navigationToDownload: () -> Unit,
            navigationToAuth: () -> Unit,
            navigationToProfile: (String,String) -> Unit
        ): ACSearchScreenDecomposeComponent
    }
}