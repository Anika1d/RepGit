package com.mir.core.data.response.repository

import com.mir.core.data.response.pagerep.Owner
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepositoryResponse(
   @SerialName("owner") val owner: Owner?,
   @SerialName("description") val description: String?,
   @SerialName("name") val name:String?,
   @SerialName("stargazers_count")   val starCount: String,
   @SerialName("watchers_count")  val watchersCount: String,
   @SerialName("forks_count")   val forksCount: String,
   @SerialName("open_issues_count") val openIssuesCount: String,
)