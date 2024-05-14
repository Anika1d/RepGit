package mir.anika1d.repgit.buildlogic

import org.gradle.api.Project

object ApkConfig {
    const val APPLICATION_ID = "mir.anika1d.repgit"
    const val MIN_SDK_VERSION = 23
    const val TARGET_SDK_VERSION = 33
    const val COMPILE_SDK_VERSION = 34

    private const val DEBUG_VERSION = "DEBUG_VERSION"

    val Project.VERSION_CODE
        get() = prop("version_code", Integer.MAX_VALUE).toInt()
    val Project.VERSION_NAME
        get() = prop("version_name", DEBUG_VERSION)
    val Project.GITHUB_BASE_URL
        get() = prop("github_base_url", "https://api.github.com/")
    val Project.GITHUB_HOSTNAME
        get() = prop("github_hostname", "api.github.com")

     val Project.GITHUB_APP_AUTH_URL
        get() = prop("github_app_auth_url", "https://github.com/login/oauth/authorize")
    val Project.GITHUB_APP_TOKEN_URL
        get() = prop("github_app_token_url", "https://github.com/login/oauth/access_token")
    val Project.GITHUB_APP_END_SESSION_URL
        get() = prop("github_app_end_session_url", "https://github.com/logout")

    val Project.GITHUB_APP_ID
        get() = prop("github_app_id", "896835")


    val Project.GITHUB_APP_CLIENT_ID
        get() = prop("github_app_client_id", "Iv23liHXhOg86Z6S9za0")


    val Project.GITHUB_APP_SECRET_KEY
        get() = prop(
            "github_app_secret_key",
            "77f0b08fd616ad26fdd80d4d5e9efed8a4051255"
        )

    val Project.GITHUB_APP_CALLBACK_URL
        get() = prop(
            "github_app_callback_url",
            "mir.anika1d.repgit://github.com/callback"
        )

    val Project.GITHUB_APP_LOGOUT_CALLBACK_URL
        get() = prop(
            "github_app_logout_callback_url",
            "mir.anika1d.repgit://github.com/logout_callback"
        )

    val Project.GITHUB_APP_SCOPE_WORKING
        get() = prop("github_app_scope_working", "user,repo")



}

internal fun Project.prop(key: String, default: Any): String {
    return providers.gradleProperty(key).getOrElse(default.toString())
}
