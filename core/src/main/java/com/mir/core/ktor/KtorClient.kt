package com.mir.core.ktor

import com.mir.core.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.DefaultJson
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.takeFrom

object KtorClient {
    fun provide() = HttpClient(CIO) {
        this.install(ContentNegotiation) {
            json(
                json = DefaultJson,
                contentType = ContentType.Application.Json,
                )
        }
        this.install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.ALL
        }
        defaultRequest {
            url{
                takeFrom(BuildConfig.BASE_URL)
                host=BuildConfig.HOST_NAME
                protocol= URLProtocol.HTTPS
                headers {
                    append(HttpHeaders.Accept,"application/vnd.github+json")
                    append(HttpHeaders.Authorization, "Bearer ${BuildConfig.GITHUB_AUTH_TOKEN}")
                }
            }
        }
    }
}