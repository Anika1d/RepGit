package mir.anika1d.core.decompose

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import kotlinx.coroutines.CoroutineScope

abstract class DecomposeComponent(
    componentContext: ComponentContext
) : IDecomposeComponent,
    ComponentContext by componentContext,
    Lifecycle.Callbacks {
    val ComponentContext.componentScope: CoroutineScope
        get() = createDecomposeCoroutineScope()
}