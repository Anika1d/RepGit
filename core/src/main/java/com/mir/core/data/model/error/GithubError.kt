package com.mir.core.data.model.error

import com.mir.core.data.response.error.Error
import kotlinx.serialization.Serializable


@Serializable
class GithubError(val errors: List<Error>, val message: String) {
}