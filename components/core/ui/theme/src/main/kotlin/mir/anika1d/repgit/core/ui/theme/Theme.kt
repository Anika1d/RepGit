package mir.anika1d.repgit.core.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView


@Composable
fun RepGitTheme(
    content: @Composable () -> Unit
) {
    val pallet = RepGitPallet()
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = pallet.darkBlue.toArgb()
        }
    }
    MaterialTheme(
        typography = Typography,
        content = content,
    )
}