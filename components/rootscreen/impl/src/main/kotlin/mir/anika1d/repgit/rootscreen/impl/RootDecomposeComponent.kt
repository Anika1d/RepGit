package mir.anika1d.repgit.rootscreen.impl

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonSkippableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mir.anika1d.core.decompose.DecomposeOnBackParameter
import mir.anika1d.core.decompose.IDecomposeComponent
import mir.anika1d.core.decompose.popOr
import mir.anika1d.core.resources.AppResources
import mir.anika1d.repgit.authscreen.api.ACAuthScreenDecomposeComponent
import mir.anika1d.repgit.core.data.model.repository.Owner
import mir.anika1d.repgit.core.ui.theme.LocalPallet
import mir.anika1d.repgit.datastore.data.TokensModel
import mir.anika1d.repgit.datastore.impl.ManagerDataStore
import mir.anika1d.repgit.downloadscreen.api.ACDownloadScreenDecomposeComponent
import mir.anika1d.repgit.network.core.data.request.UserRequest
import mir.anika1d.repgit.network.core.data.response.ResultState
import mir.anika1d.repgit.network.usecases.auth.GetTokensUseCase
import mir.anika1d.repgit.network.usecases.users.GetUserUseCase
import mir.anika1d.repgit.profilescreen.api.ACProfileScreenDecomposeComponent
import mir.anika1d.repgit.profilescreen.api.model.ProfileConfig
import mir.anika1d.repgit.repodetailscreen.api.ACRepoDetailsScreenDecomposeComponent
import mir.anika1d.repgit.repodetailscreen.api.model.DetailsRepoConfig
import mir.anika1d.repgit.rootscreen.api.ACRootDecomposeComponent
import mir.anika1d.repgit.rootscreen.model.RootScreenConfig
import mir.anika1d.repgit.searchscreen.api.ACSearchScreenDecomposeComponent
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.TokenRequest


class RootDecomposeComponent(
    private val applicationContext: Context,
    private val componentContext: ComponentContext,
    private val searchScreenFactory: ACSearchScreenDecomposeComponent.Factory,
    private val repoDetailsScreenFactory: ACRepoDetailsScreenDecomposeComponent.Factory,
    private val downloadScreenFactory: ACDownloadScreenDecomposeComponent.Factory,
    private val authScreenFactory: ACAuthScreenDecomposeComponent.Factory,
    private val profileScreenFactory: ACProfileScreenDecomposeComponent.Factory,
    private val onBack: DecomposeOnBackParameter,
    private val managerDataStore: ManagerDataStore,
    private val callbackRegisterActivity: ((Intent) -> Unit) -> ActivityResultLauncher<Intent>,
    private val useCaseGetTokens: GetTokensUseCase,
    private val useCaseUser: GetUserUseCase,
) : ComponentContext by componentContext, ACRootDecomposeComponent() {
    private val navigation = StackNavigation<RootScreenConfig>()
    private var owner: Owner?


    init {
        runBlocking {
            //получение строки из локального хранилища не труднозатратная операция,
            // и если подходить с умом, то runBlocking не приговор.
            owner = managerDataStore.getOwner()
        }
    }

    private val childStack: Value<ChildStack<RootScreenConfig, IDecomposeComponent>> = childStack(
        source = navigation,
        serializer = RootScreenConfig.serializer(),
        initialConfiguration = getInitialConfiguration(),
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun getInitialConfiguration(): RootScreenConfig {
        return if (owner != null)
            RootScreenConfig.ProfileScreen(ProfileConfig(user = owner!!))
        else
            RootScreenConfig.AuthScreen

    }


    private fun handleAuthResponseIntent(intent: Intent) {
        val exception = AuthorizationException.fromIntent(intent)
        val tokenExchangeRequest = AuthorizationResponse.fromIntent(intent)
            ?.createTokenExchangeRequest()
        when {
            exception != null -> {
                Toast.makeText(
                    applicationContext,
                    AppResources.Values.Strings.SIGN_IN_WRONG.id,
                    Toast.LENGTH_LONG
                ).show()
            }

            tokenExchangeRequest != null -> {
                Toast.makeText(
                    applicationContext,
                    AppResources.Values.Strings.PLEASE_WAIT.id,
                    Toast.LENGTH_LONG
                ).show()
                componentScope.launch {
                    val tokens = getsToken(tokenExchangeRequest)
                    managerDataStore.saveRefreshToken(tokens.refreshToken)
                    managerDataStore.saveJwtToken(tokens.accessToken)
                    managerDataStore.saveIdToken(tokens.idToken)
                    useCaseUser(UserRequest("")).collect { result ->
                        when (result) {
                            is ResultState.Success -> {
                                val owner = Owner(
                                    name = result.data!!.name,
                                    avatarUrl = result.data!!.avatarUrl
                                )
                                Log.i("owner.name", owner.name)
                                managerDataStore.saveOwner(owner)
                                push(
                                    RootScreenConfig.ProfileScreen(
                                        ProfileConfig(
                                            user = owner
                                        )
                                    )
                                )
                            }

                            else -> {
                                Toast.makeText(
                                    applicationContext,
                                    AppResources.Values.Strings.SIGN_IN_WRONG.id,
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        }
                    }
                }
            }
        }
    }


    private suspend fun getsToken(
        tokenRequest: TokenRequest,
    ): TokensModel {
        val token = useCaseGetTokens(tokenRequest)
        return TokensModel(
            accessToken = token.accessToken,
            refreshToken = token.refreshToken,
            idToken = token.idToken
        )
    }

    private fun createChild(
        config: RootScreenConfig,
        context: ComponentContext,
    ): IDecomposeComponent {
        return when (config) {
            is RootScreenConfig.SearchScreen -> searchScreenFactory(
                componentContext = context,
                navigationToDetails = { owner: String, repo: String ->
                    this.push(
                        RootScreenConfig.DetailsRepoScreen(
                            DetailsRepoConfig(
                                owner = owner,
                                repo = repo
                            )
                        )
                    )
                },
                navigationToProfile = { user, avatarUrl ->
                    this.push(
                        RootScreenConfig.ProfileScreen(
                            ProfileConfig(
                                Owner(
                                    name = user,
                                    avatarUrl = avatarUrl
                                )
                            )
                        )
                    )
                },
                navigationToDownload = {
                    this.push(RootScreenConfig.DownloadScreen)
                },
                navigationToAuth = {
                    this.push(RootScreenConfig.AuthScreen)
                }
            )

            is RootScreenConfig.DetailsRepoScreen -> repoDetailsScreenFactory(
                componentContext = context,
                onBack = ::internalOnBack,
                config = config.detailsRepoConfig,
                navigationToAuth = {
                    this.push(RootScreenConfig.AuthScreen)
                }

            )

            is RootScreenConfig.DownloadScreen -> downloadScreenFactory(
                componentContext = context,
                onBack = ::internalOnBack,
                navigationToDetails = { owner: String, repo: String ->
                    this.push(
                        RootScreenConfig.DetailsRepoScreen(
                            DetailsRepoConfig(
                                owner = owner,
                                repo = repo
                            )
                        )
                    )
                }
            )

            is RootScreenConfig.ProfileScreen -> profileScreenFactory(
                componentContext = context,
                onBack = ::internalOnBack,
                config = config.profileConfig,
                navigationToDetails = { owner: String, repo: String ->
                    this.push(
                        RootScreenConfig.DetailsRepoScreen(
                            DetailsRepoConfig(
                                owner = owner,
                                repo = repo
                            )
                        )
                    )
                },
                navigationToAuth = {
                    navigation.bringToFront(RootScreenConfig.AuthScreen)
                }

            )

            RootScreenConfig.AuthScreen -> {

                authScreenFactory.invoke(
                    componentContext = context,
                    onBack = onBack::invoke,
                    callbackActivityResultContracts = callbackRegisterActivity::invoke{
                        handleAuthResponseIntent(
                            it
                        )
                    }
                )
            }
        }
    }

    private fun internalOnBack() {
        navigation.popOr(onBack::invoke)
    }


    @Composable
    @NonSkippableComposable
    @Suppress("UNUSED_EXPRESSION")
    override fun Render(modifier: Modifier) {
        val stack by childStack.subscribeAsState()
        val localPallet = LocalPallet.current
        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current
        Scaffold(bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.1F),
                containerColor = localPallet.darkBlue.copy(0.9f),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            val owner = managerDataStore.getOwner()
                            if (owner != null) {
                                push(RootScreenConfig.ProfileScreen(ProfileConfig(owner)))
                            } else {
                                Toast.makeText(
                                    context,
                                    context.getText(AppResources.Values.Strings.PLEASE_AUTH.id),
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        }
                    }) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            painter = painterResource(id = AppResources.Drawable.PROFILE.id),
                            contentDescription = "profile_bottom_bar",
                            tint = localPallet.mint
                        )
                    }
                    IconButton(onClick = {
                        coroutineScope.launch {
                            val owner = managerDataStore.getOwner()
                            if (owner != null) {
                                push(RootScreenConfig.SearchScreen)
                            } else {
                                Toast.makeText(
                                    context,
                                    context.getText(AppResources.Values.Strings.PLEASE_AUTH.id),
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        }
                    }) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            painter = painterResource(id = AppResources.Drawable.SEARCH.id),
                            contentDescription = "search_bottom_bar",
                            tint = localPallet.mint
                        )
                    }
                }
            }
        }) { padding ->
            padding
            Children(
                modifier = modifier,
                stack = stack,
            ) {

                it.instance.Render()
            }
        }
    }

    @OptIn(ExperimentalDecomposeApi::class)
    override fun push(config: RootScreenConfig) {
        navigation.pushToFront(config)
    }

}