package mir.anika1d.repgit.ui.composable.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mir.anika1d.repgit.core.data.model.issues.Issues
import mir.anika1d.repgit.core.ui.theme.LocalPallet
import mir.anika1d.repgit.ui.composable.tools.formatDate

@Composable
fun ItemIssue(modifier: Modifier, issue: Issues) {
    val localPallet= LocalPallet.current
    Column(
        modifier = modifier.drawBehind {
            drawLine(color = localPallet.darkBlue,
                start = Offset(0f,size.height),
                end = Offset(size.width,size.height),
                strokeWidth = 5.dp.toPx(),
                alpha = 1f
            )},
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier= Modifier.fillMaxWidth(),
            text = issue.title, style = TextStyle(
                color = localPallet.mint,
                fontSize = 20.sp
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.fillMaxWidth(0.5f)) {
                Text(
                    text = issue.updatedAt.formatDate(), style = TextStyle(
                        color =localPallet. mint,
                        fontSize = 20.sp
                    )
                )
            }
            Column(Modifier.fillMaxWidth()) {
                Text(
                    text = issue.createdAt.formatDate(), style = TextStyle(
                        color = localPallet.mint,
                        fontSize = 20.sp
                    )
                )
                
            }
        }
        Spacer(modifier = Modifier.size(5.dp))
        }
}

