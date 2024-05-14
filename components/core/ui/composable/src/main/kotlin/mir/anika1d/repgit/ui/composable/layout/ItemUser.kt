package mir.anika1d.repgit.ui.composable.layout


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import mir.anika1d.core.resources.AppResources
import mir.anika1d.repgit.core.data.model.user.User
import mir.anika1d.repgit.core.ui.theme.LocalPallet
import mir.anika1d.repgit.ui.composable.tools.SizeManager

@Composable
fun ItemUser(
    modifier: Modifier,
    user: User,
    contentPadding: PaddingValues = PaddingValues(8.dp),
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
                AsyncImage(
                    model = user.avatarUrl.ifEmpty { painterResource(id = AppResources.Drawable.DEFAULT_AVATAR.id) },
                    contentDescription = user.avatarUrl.ifEmpty { "avatar_owner" },
                    placeholder = painterResource(id = AppResources.Drawable.DEFAULT_AVATAR.id),
                    error = painterResource(id = AppResources.Drawable.IMAGE_FAIL.id),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(
                            sizeManager.giveNeed(40.dp, 60.dp, 80.dp)
                        )
                        .clip(CircleShape)
                )
                Column(Modifier.fillMaxWidth(1f)) {
                    Text(text = user.name, color = localPallet.dirtyWhite,)
                    Row {
                        Text(
                            text = stringResource(id = AppResources.Values.Strings.FOLLOWERS.id)+" "+ user.followersCount,
                            color = localPallet.dirtyWhite,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Icon(
                            painter = painterResource(id = AppResources.Drawable.FOLLOWERS.id),
                            contentDescription = "followers:${user.name}",
                            tint=localPallet.mint
                        )
                    }

                }


            }

        }
    }
}

