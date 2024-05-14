package mir.anika1d.repgit.singleactivity.impl

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.decompose.extensions.compose.stack.animation.LocalStackAnimationProvider
import mir.anika1d.repgit.core.ui.theme.RepGitTheme
import mir.anika1d.repgit.rootscreen.api.ACRootDecomposeComponent
import mir.anika1d.repgit.rootscreen.api.LocalRootNavigation
import mir.anika1d.repgit.singleactivity.impl.utils.RepGitStackAnimationProvider
import org.koin.android.ext.android.inject

class SingleActivity : ComponentActivity() {


    private val rootComponentFactory: ACRootDecomposeComponent.Factory by inject()
    private var rootDecomposeComponent: ACRootDecomposeComponent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val oathInGithub = { function: (Intent) -> Unit ->
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                val dataIntent = it.data ?: return@registerForActivityResult
                function(dataIntent)
            }
        }
        val root = rootComponentFactory(
            componentContext = defaultComponentContext(),
            onBack = this::finish,
            callback = oathInGithub
        ).also { rootDecomposeComponent = it }

        setContent {
            RepGitTheme {
                CompositionLocalProvider(
                    LocalRootNavigation provides root,
                    LocalStackAnimationProvider provides RepGitStackAnimationProvider
                ) {
                    root.Render(
                        modifier = Modifier
                            .fillMaxSize()
                            .imePadding()
                    )
                }

            }
        }
    }
}



