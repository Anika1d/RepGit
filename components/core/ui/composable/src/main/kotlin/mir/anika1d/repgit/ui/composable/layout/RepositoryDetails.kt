package mir.anika1d.repgit.ui.composable.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mir.anika1d.repgit.core.ui.theme.LocalPallet

@Composable
fun RepositoryDetails(
    modifier: Modifier,
    item: String?,
    painter: Painter,
    contentDescription: String?,
    placeholder:Boolean,
) {
    val localPallet= LocalPallet.current
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Icon(
            painter = painter, contentDescription = contentDescription,
            tint = localPallet.mint,
            modifier = Modifier.size(25.dp)
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(
                modifier= Modifier.fillMaxWidth().placeholder(enabled = placeholder),
                text = item?:"",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 14.sp
                )
            )
    }
}