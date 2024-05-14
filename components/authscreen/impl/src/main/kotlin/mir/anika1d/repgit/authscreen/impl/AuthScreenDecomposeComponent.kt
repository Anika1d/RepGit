package mir.anika1d.repgit.authscreen.impl

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.activity.result.ActivityResultLauncher
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import mir.anika1d.core.decompose.DecomposeOnBackParameter
import mir.anika1d.core.resources.AppResources
import mir.anika1d.repgit.authscreen.api.ACAuthScreenDecomposeComponent
import mir.anika1d.repgit.core.ui.theme.LocalPallet
import mir.anika1d.repgit.network.usecases.auth.AuthUseCase
import mir.anika1d.repgit.ui.composable.layout.BackgroundContainer
import net.openid.appauth.AuthorizationService

class AuthScreenDecomposeComponent(
    componentContext: ComponentContext,
    private val onBack: DecomposeOnBackParameter,
    private val authService: AuthorizationService,
    private val useCaseAuth: AuthUseCase,
    private val callbackActivityResultContracts: ActivityResultLauncher<Intent>
) : ACAuthScreenDecomposeComponent(componentContext) {
    private val openAuthPageEventChannel = Channel<Intent>(Channel.BUFFERED)
    private val openAuthPageFlow: Flow<Intent>
        get() = openAuthPageEventChannel.receiveAsFlow()

    private fun openLoginPage() {
        val customTabsIntent = CustomTabsIntent.Builder().build()
        val authRequest = useCaseAuth()
        val openAuthPageIntent = authService.getAuthorizationRequestIntent(
            authRequest,
            customTabsIntent
        )
        callbackActivityResultContracts.launch(openAuthPageIntent)
    }


    @Composable
    override fun Render() {
        val pallet = LocalPallet.current
        BackHandler {
            onBack()
        }
        LaunchedEffect(key1 = openAuthPageFlow) {
            openAuthPageFlow.collect {
                callbackActivityResultContracts.launch(it)
            }
        }
        BackgroundContainer(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                colors = ButtonDefaults.buttonColors().copy(containerColor = pallet.mint),
                onClick = { openLoginPage() },
            ) {
                Text(
                    stringResource(id = AppResources.Values.Strings.SIGN_IN.id),
                    color = pallet.dirtyWhite
                )
            }
        }
    }
}