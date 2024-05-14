package mir.anika1d.repgit.rootscreen.api

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import mir.anika1d.core.decompose.DecomposeOnBackParameter
import mir.anika1d.core.decompose.createDecomposeCoroutineScope

abstract class ACRootDecomposeComponent :
    ComponentContext,
    RootNavigationInterface {
    val componentScope: CoroutineScope = createDecomposeCoroutineScope()
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onBack: DecomposeOnBackParameter,
            callback: ((Intent) -> Unit) -> ActivityResultLauncher<Intent>
        ): ACRootDecomposeComponent
    }

    @Composable
    abstract fun Render(modifier: Modifier)
}
