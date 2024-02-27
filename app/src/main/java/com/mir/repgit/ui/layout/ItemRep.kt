package com.mir.repgit.ui.layout


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mir.core.data.model.repository.Owner
import com.mir.core.data.model.repository.RepositoryItem
import com.mir.repgit.R
import com.mir.repgit.tools.SizeManager
import com.mir.repgit.ui.theme.blue
import com.mir.repgit.ui.theme.dirtyWhite
import com.mir.repgit.ui.theme.lineBackgroundColor

@Composable
fun ItemRep(
    modifier: Modifier,
    contentPadding: PaddingValues = PaddingValues(8.dp),
    repository: RepositoryItem,
    onClick: () -> Unit,
) {
    val configuration=LocalConfiguration.current
    val sizeManager by remember {
        mutableStateOf(
            SizeManager(
                configuration.screenHeightDp,
                configuration.screenWidthDp
            ))
    }
    Box(modifier = modifier
        .background(blue.copy(alpha = 0.8f))
        .clickable { onClick.invoke() }
        .drawBehind {
            drawArc(
                color = lineBackgroundColor, alpha = 0.1f,
                style = Stroke(width = 1f),
                useCenter = false,
                startAngle = 100f,
                sweepAngle = 360f,
                topLeft = Offset(center.x, 0f),
                size = Size(size.width, center.y / 2f)
            )
            drawArc(
                color = lineBackgroundColor, alpha = 0.1f,
                style = Stroke(width = 1f),
                useCenter = false,
                startAngle = 100f,
                sweepAngle = 360f,
                topLeft = Offset(0f, center.y),
                size = Size(center.x + center.x / 2, size.height)
            )
        }) {
        Row(Modifier.padding(paddingValues = contentPadding)) {
            AsyncImage(
                model = repository.owner.avatarUrl.ifEmpty { painterResource(id = R.drawable.avatar_default) },
                contentDescription = repository.owner.avatarUrl.ifEmpty { "avatar_owner" },
                placeholder = painterResource(id = R.drawable.avatar_default),
                error = painterResource(id = R.drawable.image_fail),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(
                        sizeManager.giveNeed(40.dp, 60.dp, 80.dp)
                    )
                    .clip(CircleShape)
            )

            Column {
                Text(text = repository.name, color = dirtyWhite)
                Text(text = repository.description ?: "", color = dirtyWhite)
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
        repository = RepositoryItem(
            owner = Owner("", ""),
            description = "Desc",
            name = "Repka",
            issuesCount = "1",
            starCount = "1",
            forkCount = "2",
            watchersCount = "1"
        ),
    ) {

    }
}