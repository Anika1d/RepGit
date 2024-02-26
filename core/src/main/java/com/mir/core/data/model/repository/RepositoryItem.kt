package com.mir.core.data.model.repository


data class RepositoryItem(
    val owner: Owner,
    val description: String?,
    val name: String,
    val starCount: String,
    val watchersCount: String,
    val forkCount: String,
    val issuesCount: String,
)