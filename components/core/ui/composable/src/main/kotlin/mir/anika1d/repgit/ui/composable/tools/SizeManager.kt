package mir.anika1d.repgit.ui.composable.tools

import android.util.Size
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp


class SizeManager(
    val screenSize: Size
) {
    constructor(h:Int,w:Int) : this(Size( w,h))
    val center:Offset= Offset(x=screenSize.width.toFloat()/2f,y=screenSize.height.toFloat()/2f)

    fun calculateScreenSize() = StandardSizeScreen.getSize(screenSize)


    fun <T> giveNeed(compactSize:T,mediumSize: T,expandedSize: T): T {
       return when (calculateScreenSize()){
            StandardSizeScreen.COMPACT-> compactSize
            StandardSizeScreen.MEDIUM->mediumSize
            StandardSizeScreen.EXPANDED->expandedSize
            else -> mediumSize
        }
    }
}

enum class StandardSizeScreen(
) {
    EXPANDED,
    MEDIUM,
    COMPACT,
    UNSPECIFIED;

    companion object {
        fun getSize(size: Size): StandardSizeScreen {
            if (size.height.dp >= 900.dp || size.width.dp >= 840.dp) {
                return EXPANDED
            }
            if ((480.dp <= size.height.dp) && (size.height.dp < 900.dp) ||
                (600.dp <= size.width.dp) && (size.width.dp < 840.dp)
            ) {
                return MEDIUM
            }
            if (size.height.dp < 480.dp || size.width.dp < 600.dp) {
                return COMPACT
            }
            return UNSPECIFIED
        }
    }
}