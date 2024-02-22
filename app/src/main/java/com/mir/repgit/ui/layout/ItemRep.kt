package com.mir.repgit.ui.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mir.repgit.R

@Composable
fun ItemRep(
    modifier: Modifier,
    contentPadding: PaddingValues = PaddingValues(8.dp),
    onClick: () -> Unit,
) {
    Box(modifier = modifier
        .background(Color(0xCC10194E))
        .clickable { onClick.invoke() }
        .drawBehind {
            drawArc(
                color = Color(0xFFC4C4C4), alpha = 0.1f,
                style = Stroke(width = 1f),
                useCenter = false,
                startAngle = 100f,
                sweepAngle = 360f,
                topLeft = Offset(center.x, 0f),
                size = Size(size.width, center.y / 2f)
            )
            drawArc(
                color = Color(0xFFC4C4C4), alpha = 0.1f,
                style = Stroke(width = 1f),
                useCenter = false,
                startAngle = 100f,
                sweepAngle = 360f,
                topLeft = Offset(0f, center.y),
                size = Size(center.x + center.x / 2, size.height)
            )
        }) {
        Row(Modifier.padding(paddingValues = contentPadding)) {
            Image(
                painter = painterResource(id = R.drawable.avatar_default),
                contentDescription = "avatar default"
            )
            Column {
                Text(text = " name", color = Color.White)
                Text(text = " Description", color = Color.White)
            }

        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewItemRep() {
    ItemRep(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        contentPadding = PaddingValues(8.dp),
    ) {

    }
}