package com.mir.core.data.response.issues

import kotlinx.serialization.Serializable

@Serializable
data class PullRequest(
    val url: String,
    val html_url: String,
    val diff_url: String,
    val patch_url: String
)