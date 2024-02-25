package com.mir.core.mapper

import com.mir.core.data.model.error.GithubError
import com.mir.core.data.model.repository.Owner
import com.mir.core.data.model.repository.PageRepository
import com.mir.core.data.model.repository.RepositoryItem
import com.mir.core.data.response.error.ErrorResponse
import com.mir.core.data.response.pagerep.PageResponse

object Mapper {
    fun mapRepositoryResponseToAppModel(
        pageResponse: PageResponse,
        nextPage: String?
    ): PageRepository = PageRepository(
        incompleteResults = pageResponse.incompleteResults!!,
        items = pageResponse.items.map {
            RepositoryItem(
                name = it.name ?: "",
                description = it.description ?: "",
                owner = Owner(avatarUrl = it.owner?.avatarUrl ?: "")
            )
        },
        totalCount = pageResponse.totalCount,
        nextPage = nextPage
    )
    fun mapErrorResponseToAppModel(errorResponse: ErrorResponse) = GithubError(
        errors = errorResponse.errors ?: listOf(),
        message = errorResponse.message
    )
}