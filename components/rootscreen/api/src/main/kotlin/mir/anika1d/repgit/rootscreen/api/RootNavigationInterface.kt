package mir.anika1d.repgit.rootscreen.api

import androidx.compose.runtime.staticCompositionLocalOf
import mir.anika1d.repgit.rootscreen.model.RootScreenConfig

val LocalRootNavigation = staticCompositionLocalOf<RootNavigationInterface> {
    error("CompositionLocal LocalRootComponent not present")
}

interface RootNavigationInterface {
    fun push(config: RootScreenConfig)
}
