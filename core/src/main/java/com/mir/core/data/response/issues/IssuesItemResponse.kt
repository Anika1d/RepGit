package com.mir.core.data.response.issues

import kotlinx.serialization.Serializable

@Serializable
data class IssuesItemResponse(
//    val draft: Boolean?,
//    val performed_via_github_app: String?=null,
    val id: Long?=null,
//    val node_id: String?,
    val url: String?=null,
     val reactions: Reactions?=null,
//    val repository_url: String?,
//    val labels_url: String?,
//    val comments_url: String?,
//    val events_url: String?,
//    val html_url: String?,
//    val number: Int?,
    val state: String?=null,
    val title: String?=null,
    val body: String?=null,
//    val user: User?,
//    val labels: List<Label>?,
//    val assignee: User?,
//    val assignees: List<User>?,
//    val milestone: Milestone?,
//    val locked: Boolean?,
//    val active_lock_reason: String?,
    val comments: Long?=null,
//    val pull_request: PullRequest?,
//    val closed_at: String?,
    val created_at: String?=null,
    val updated_at: String?=null,
//    val closed_by: User?,
//    val author_association: String?,
//    val state_reason: String?=null
    )