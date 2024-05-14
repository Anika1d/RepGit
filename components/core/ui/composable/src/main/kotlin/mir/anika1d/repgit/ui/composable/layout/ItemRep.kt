package mir.anika1d.repgit.ui.composable.layout


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import mir.anika1d.core.resources.AppResources
import mir.anika1d.repgit.core.data.model.repository.Owner
import mir.anika1d.repgit.core.data.model.repository.RepositoryItem
import mir.anika1d.repgit.core.ui.theme.LocalPallet
import mir.anika1d.repgit.ui.composable.tools.SizeManager
import mir.anika1d.repgit.ui.composable.tools.formatDate

@Composable
fun ItemRep(
    modifier: Modifier,
    repository: RepositoryItem,
    contentPadding: PaddingValues = PaddingValues(8.dp),
    showOwnerAvatar: Boolean = true,
    onClick: () -> Unit,

    ) {
    val localPallet = LocalPallet.current
    val configuration = LocalConfiguration.current
    val sizeManager by remember {
        mutableStateOf(
            SizeManager(
                configuration.screenHeightDp,
                configuration.screenWidthDp
            )
        )
    }
    Box(modifier = modifier
        .background(localPallet.blue.copy(alpha = 0.8f))
        .clickable { onClick.invoke() }
        .drawBehind {
            drawArc(
                color = localPallet.lineBackgroundColor, alpha = 0.1f,
                style = Stroke(width = 1f),
                useCenter = false,
                startAngle = 100f,
                sweepAngle = 360f,
                topLeft = Offset(center.x, 0f),
                size = Size(size.width, center.y / 2f)
            )
            drawArc(
                color = localPallet.lineBackgroundColor, alpha = 0.1f,
                style = Stroke(width = 1f),
                useCenter = false,
                startAngle = 100f,
                sweepAngle = 360f,
                topLeft = Offset(0f, center.y),
                size = Size(center.x + center.x / 2, size.height)
            )
        }) {
        Column(Modifier.padding(paddingValues = contentPadding)) {
            Row(Modifier.fillMaxWidth()) {
                AnimatedVisibility(visible = showOwnerAvatar) {
                    AsyncImage(
                        model = repository.owner.avatarUrl.ifEmpty { painterResource(id = AppResources.Drawable.DEFAULT_AVATAR.id) },
                        contentDescription = repository.owner.avatarUrl.ifEmpty { "avatar_owner" },
                        placeholder = painterResource(id = AppResources.Drawable.DEFAULT_AVATAR.id),
                        error = painterResource(id = AppResources.Drawable.IMAGE_FAIL.id),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(
                                sizeManager.giveNeed(40.dp, 60.dp, 80.dp)
                            )
                            .clip(CircleShape)
                    )
                }
                Column(Modifier.fillMaxWidth(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = repository.name.substring(0, minOf(repository.name.length, 10)) +
                                    if (repository.name.length > 10) "..." else "",
                            color = localPallet.dirtyWhite
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            text = repository.updatedAt.formatDate(),
                            textAlign = TextAlign.End,
                            color = localPallet.dirtyWhite,
                            fontSize = 10.sp,
                        )
                    }

                    Text(
                        text = repository.description.orEmpty(),
                        color = localPallet.dirtyWhite,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }
            LazyRow(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    AnimatedVisibility(visible = repository.language.isNotEmpty()) {
                        Row(
                            Modifier
                                .background(localPallet.purple, CircleShape)
                                .padding(4.dp)
                                .defaultMinSize(minHeight = 20.dp, minWidth = 30.dp)
                        ) {
                            Text(
                                text = repository.language,
                                fontSize = 14.sp,
                                color = localPallet.dirtyWhite
                            )
                        }
                    }
                }
                item {
                    Row(
                        Modifier
                            .background(localPallet.purple, CircleShape)
                            .defaultMinSize(minHeight = 20.dp, minWidth = 30.dp)
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = repository.starCount,
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            color = localPallet.dirtyWhite
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        Icon(
                            modifier = Modifier.size(26.dp),
                            painter = painterResource(id = AppResources.Drawable.STAR.id),
                            contentDescription = "star",
                            tint = localPallet.dirtyWhite

                        )
                    }
                }
                item {
                    Row(
                        Modifier
                            .background(localPallet.purple, CircleShape)
                            .defaultMinSize(minHeight = 20.dp, minWidth = 30.dp)
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = repository.forkCount,
                            fontSize = 14.sp,
                            color = localPallet.dirtyWhite
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        Icon(
                            modifier = Modifier.size(26.dp),
                            painter = painterResource(id = AppResources.Drawable.FORKS.id),
                            contentDescription = "forks",
                            tint = localPallet.dirtyWhite
                        )
                    }
                }
                item {
                    Row(
                        Modifier
                            .background(localPallet.purple, CircleShape)
                            .defaultMinSize(minHeight = 20.dp, minWidth = 30.dp)
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = repository.defaultBranch,
                            fontSize = 14.sp,
                            color = localPallet.dirtyWhite
                        )
                        Spacer(modifier = Modifier.size(10.dp))

                        Icon(
                            modifier = Modifier.size(26.dp),
                            painter = painterResource(id = AppResources.Drawable.BRANCHES.id),
                            contentDescription = "default_branch",
                            tint = localPallet.dirtyWhite
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun ItemRepShotInfo(
    modifier: Modifier,
    contentPadding: PaddingValues = PaddingValues(8.dp),
    owner: String,
    name: String,
    onClick: () -> Unit,
) {
    val localPallet = LocalPallet.current
    val configuration = LocalConfiguration.current
    val sizeManager by remember {
        mutableStateOf(
            SizeManager(
                configuration.screenHeightDp,
                configuration.screenWidthDp
            )
        )
    }
    Box(modifier = modifier
        .background(localPallet.blue.copy(alpha = 0.8f))
        .clickable { onClick.invoke() }
        .drawBehind {
            drawArc(
                color = localPallet.lineBackgroundColor, alpha = 0.1f,
                style = Stroke(width = 1f),
                useCenter = false,
                startAngle = 100f,
                sweepAngle = 360f,
                topLeft = Offset(center.x, 0f),
                size = Size(size.width, center.y / 2f)
            )
            drawArc(
                color = localPallet.lineBackgroundColor, alpha = 0.1f,
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
                modifier = Modifier
                    .size(
                        sizeManager.giveNeed(40.dp, 60.dp, 80.dp)
                    )
                    .clip(CircleShape),
                painter = painterResource(id = AppResources.Drawable.DEFAULT_AVATAR.id),
                contentDescription = "default_avatar"
            )
            Column {
                Text(text = owner, color = localPallet.dirtyWhite)
                Text(text = name, color = localPallet.dirtyWhite)
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
            watchersCount = "1",
            url = "",
            downloadUrl = "",
            updatedAt = "",
            language = "",
            defaultBranch = ""
        )
    ) {

    }
}