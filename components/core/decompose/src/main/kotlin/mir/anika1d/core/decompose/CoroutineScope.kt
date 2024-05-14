package mir.anika1d.core.decompose

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
val createDecomposeCoroutineScope: () -> CoroutineScope = {
    CoroutineScope(Dispatchers.Main)
}

