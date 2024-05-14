package mir.anika1d.repgit.profilescreen.impl.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.collectLatest
import mir.anika1d.core.network.connection.service.ConnectivityState
import mir.anika1d.core.resources.AppResources
import mir.anika1d.repgit.core.data.model.repository.RepositoryItem
import mir.anika1d.repgit.core.data.model.user.User
import mir.anika1d.repgit.core.ui.theme.LocalPallet
import mir.anika1d.repgit.profilescreen.impl.screen.management.ManagementScreen
import mir.anika1d.repgit.ui.composable.layout.BackgroundContainer
import mir.anika1d.repgit.ui.composable.layout.ItemRep
import mir.anika1d.repgit.ui.composable.layout.placeholder
import mir.anika1d.repgit.ui.composable.tools.SizeManager


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Screen(
    repositories: List<RepositoryItem>,
    user: User?,
    isOwner: Boolean,
    managementScreen: ManagementScreen,
    connectEthernet: ConnectivityState,
) {
    val configuration = LocalConfiguration.current
    val pallet = LocalPallet.current
    val sizeManager by remember(configuration) {
        mutableStateOf(
            SizeManager(
                configuration.screenHeightDp, configuration.screenWidthDp
            )
        )
    }
    val placeHolder by remember(user) {
        mutableStateOf(user == null)
    }
    val context = LocalContext.current
    val lazyListRepositoryState = rememberLazyListState()
    LaunchedEffect(lazyListRepositoryState) {
        snapshotFlow {
            lazyListRepositoryState.firstVisibleItemIndex
        }.collectLatest { index ->
            if (connectEthernet == ConnectivityState.Available) {
                if (repositories.isNotEmpty() && index > repositories.size - 15) {
                    managementScreen.nextPage(onSuccess = {}, onError = { message: String? ->
                        Toast.makeText(
                            context, message ?: "Unknown Error", Toast.LENGTH_LONG
                        ).show()
                    })
                }
            }
        }
    }
    Scaffold(
        containerColor = pallet.darkBlue.copy(0.9f),
        contentColor = Color.Transparent,
        topBar = {
            TopAppBar(title = { },
                modifier = Modifier.background(Color.Transparent),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                ),
                navigationIcon = {
                    AnimatedVisibility(visible = isOwner) {
                        IconButton(onClick = { managementScreen.logout() }) {
                            Icon(
                                modifier = Modifier
                                    .size(40.dp)
                                    .rotate(180f),
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "logout",
                                tint = pallet.dirtyWhite
                            )

                        }
                    }
                    AnimatedVisibility(visible = !isOwner) {
                        IconButton(onClick = { managementScreen.onBack() }) {
                            Icon(
                                modifier = Modifier.size(40.dp),
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "back",
                                tint = pallet.dirtyWhite
                            )

                        }

                    }
                })
        }) {
        BackgroundContainer(
            modifier = Modifier
                .fillMaxSize()
                .padding(it), connectEthernet = connectEthernet
        ) {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                AsyncImage(model = user?.avatarUrl,
                    contentDescription = user?.avatarUrl.toString(),
                    placeholder = painterResource(id = AppResources.Drawable.DEFAULT_AVATAR.id),
                    error = if (!placeHolder) painterResource(id = AppResources.Drawable.IMAGE_FAIL.id) else null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .drawBehind {
                            drawCircle(
                                color = Color(0xFF192259),
                                radius = (sizeManager.center.x / 2).dp.toPx() + sizeManager
                                    .giveNeed(
                                        30.dp, 50.dp, 60.dp
                                    )
                                    .toPx()
                            )
                            drawCircle(
                                color = Color(0xFF10194E),
                                radius = (sizeManager.center.x / 2).dp.toPx()
                            )
                            drawCircle(
                                color = Color.Black,
                                alpha = 0.1f,
                                radius = (sizeManager.center.x / 2).dp.toPx() + sizeManager
                                    .giveNeed(
                                        30.dp, 50.dp, 60.dp
                                    )
                                    .toPx()
                            )
                        }
                        .clip(CircleShape)
                        .size(
                            (sizeManager.center.x / 2).dp
                        )
                        .zIndex(1f)
                        .placeholder(placeHolder)


                )
                Text(
                    text = user?.name.orEmpty(),
                    modifier = Modifier
                        .placeholder(placeHolder)
                        .zIndex(2f),
                    style = TextStyle(fontSize = 25.sp, color = pallet.dirtyWhite)
                )
                Spacer(modifier = Modifier.size(10.dp))
                Row(
                    modifier = Modifier.zIndex(2f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        modifier = Modifier.placeholder(placeHolder, shape = CircleShape),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(id = AppResources.Drawable.REPOSITORY.id),
                            contentDescription = "repository",
                            tint = pallet.mint
                        )
                        Text(
                            modifier = Modifier.placeholder(placeHolder),
                            text = user?.repositoryCount.orEmpty(),
                            style = TextStyle(fontSize = 14.sp, color = pallet.dirtyWhite)
                        )
                    }
                    Spacer(modifier = Modifier.size(50.dp))
                    Column(
                        modifier = Modifier.placeholder(placeHolder, shape = CircleShape),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(id = AppResources.Drawable.FOLLOWERS.id),
                            contentDescription = "followers",
                            tint = pallet.mint
                        )
                        Text(
                            text = user?.followersCount.orEmpty(),
                            style = TextStyle(fontSize = 14.sp, color = pallet.dirtyWhite)
                        )
                    }
                }
                LazyColumn(
                    modifier = Modifier.clip(
                        shape = RoundedCornerShape(
                            topStart = 50f,
                            topEnd = 50f,
                            bottomEnd = 0f,
                            bottomStart = 0f
                        ),
                    )
                ) {
                    items(repositories.size) { i ->
                        ItemRep(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(8.dp),
                            repository = repositories[i],
                            showOwnerAvatar = false
                        ) {
                            managementScreen.navigationToDetails(
                                owner = user!!.name, repositoryName = repositories[i].name
                            )
                        }
                    }
                    item { Spacer(modifier = Modifier.size(100.dp)) }
                }
            }
        }
    }
}