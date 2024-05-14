package mir.anika1d.repgit.network.core.client.ktor

import android.net.Uri
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import mir.anika1d.repgit.datastore.api.ACManagerDataStore
import mir.anika1d.repgit.datastore.data.TokensModel
import mir.anika1d.repgit.datastore.impl.ManagerDataStore
import mir.anika1d.repgit.network.core.client.BuildConfig
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientSecretPost
import net.openid.appauth.GrantTypeValues
import net.openid.appauth.TokenRequest
import kotlin.coroutines.suspendCoroutine

class KtorClient(
    private val managerDataStore: ACManagerDataStore,
    private val authService: AuthorizationService,
) {
    fun provide() = HttpClient(Android) {
        install(ContentNegotiation) {
            json(
                json = Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                    isLenient = true
                    allowSpecialFloatingPointValues = true
                    prettyPrint = false
                    useArrayPolymorphism = false
                },
                contentType = ContentType.Application.Json,
            )
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 1000 * 60
            connectTimeoutMillis = 1000 * 10
            socketTimeoutMillis = 3000
        }
        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.ALL
        }
        install(Auth) {
            bearer {
                refreshTokens {
                    val tokensRequest = TokenRequest.Builder(
                        AuthorizationServiceConfiguration(
                            Uri.parse(BuildConfig.GITHUB_APP_AUTH_URL),
                            Uri.parse(BuildConfig.GITHUB_APP_TOKEN_URL),
                            null,
                            Uri.parse(BuildConfig.GITHUB_APP_END_SESSION_URL)
                        ),
                        BuildConfig.GITHUB_APP_CLIENT_ID
                    )
                        .setGrantType(GrantTypeValues.REFRESH_TOKEN)
                        .setScopes(BuildConfig.GITHUB_APP_SCOPE_WORKING)
                        .setRefreshToken(managerDataStore.getRefreshToken().orEmpty())
                        .build()
                    val tokens = suspendCoroutine { continuation ->
                        authService.performTokenRequest(
                            tokensRequest,
                            ClientSecretPost(BuildConfig.GITHUB_APP_CLIENT_ID)
                        ) { response, _ ->
                            when {
                                response != null -> {
                                    val tokens = TokensModel(
                                        accessToken = response.accessToken.orEmpty(),
                                        refreshToken = response.refreshToken.orEmpty(),
                                        idToken = response.idToken.orEmpty()
                                    )
                                    continuation.resumeWith(Result.success(tokens))
                                }

                                else -> {
                                    CoroutineScope(Dispatchers.IO).launch { managerDataStore.clear() }
                                }
                            }
                        }
                    }
                    BearerTokens(
                        accessToken = tokens.accessToken,
                        refreshToken = tokens.refreshToken
                    )
                }
                loadTokens {
                    BearerTokens(
                        accessToken = managerDataStore.getJwtToken().orEmpty(),
                        refreshToken = managerDataStore.getJwtToken().orEmpty(),
                    )
                }
            }
        }

        defaultRequest {
            url {
                takeFrom(BuildConfig.GITHUB_BASE_URL)
                host = BuildConfig.GITHUB_HOSTNAME
                protocol = URLProtocol.HTTPS
                headers {
                    append(HttpHeaders.Accept, "application/vnd.github+json")
                }
            }
        }
    }
}