package mir.anika1d.repgit.repodetailscreen.api

import com.arkivanov.decompose.ComponentContext
import mir.anika1d.core.decompose.DecomposeComponent
import mir.anika1d.core.decompose.DecomposeOnBackParameter
import mir.anika1d.repgit.repodetailscreen.api.model.DetailsRepoConfig

abstract class ACRepoDetailsScreenDecomposeComponent(context: ComponentContext) :
    DecomposeComponent(context) {
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onBack: DecomposeOnBackParameter,
            config: DetailsRepoConfig,
            navigationToAuth: () -> Unit
            ):ACRepoDetailsScreenDecomposeComponent
    }
}