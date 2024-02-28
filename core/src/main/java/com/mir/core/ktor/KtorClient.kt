package com.mir.core.ktor

import com.mir.core.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
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
import kotlinx.serialization.json.Json

object KtorClient {
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
        install(HttpTimeout){
            requestTimeoutMillis = 1000*60
            connectTimeoutMillis =1000*10
            socketTimeoutMillis = 3000
        }
        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.ALL
        }
        defaultRequest {
            url {
                takeFrom(BuildConfig.BASE_URL)
                host = BuildConfig.HOST_NAME
                protocol = URLProtocol.HTTPS
                headers {
                    append(HttpHeaders.Accept, "application/vnd.github+json")
                    append(HttpHeaders.Authorization, "Bearer ${BuildConfig.GITHUB_AUTH_TOKEN}")
                }
            }
        }
    }
}