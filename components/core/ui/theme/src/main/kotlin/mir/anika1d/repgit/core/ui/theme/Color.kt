package mir.anika1d.repgit.core.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalPallet = compositionLocalOf{ RepGitPallet() }

@Stable
data class RepGitPallet(
    val lineBackgroundColor: Color = Color(0xFFC4C4C4),
    val darkBlue: Color = Color(0xFF010A43),
    val mint: Color = Color(0xFF1DC7AC),
    val blue: Color = Color(0xFF10194E),
    val dirtyWhite: Color = Color(0xFFFAFAFA),
    val purple: Color = Color(0xFF7B22A5)
) {
    val defaultColors = listOf(
        darkBlue,
        mint,
        blue,
    )

}