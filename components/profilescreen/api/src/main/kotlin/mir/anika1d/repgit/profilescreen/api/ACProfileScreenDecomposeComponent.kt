package mir.anika1d.repgit.profilescreen.api

import com.arkivanov.decompose.ComponentContext
import mir.anika1d.core.decompose.DecomposeComponent
import mir.anika1d.core.decompose.DecomposeOnBackParameter
import mir.anika1d.repgit.profilescreen.api.model.ProfileConfig

abstract class ACProfileScreenDecomposeComponent (context: ComponentContext) :
    DecomposeComponent(context) {
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onBack: DecomposeOnBackParameter,
            config: ProfileConfig,
            navigationToDetails:(String, String) -> Unit,
            navigationToAuth:() -> Unit,
        ):ACProfileScreenDecomposeComponent
    }
}