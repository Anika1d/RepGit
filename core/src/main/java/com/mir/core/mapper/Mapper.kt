package com.mir.core.mapper

import com.mir.core.data.model.error.GithubError
import com.mir.core.data.model.issues.Issues
import com.mir.core.data.model.repository.Owner
import com.mir.core.data.model.repository.PageRepository
import com.mir.core.data.model.repository.RepositoryItem
import com.mir.core.data.response.error.ErrorResponse
import com.mir.core.data.response.issues.IssuesItemResponse
import com.mir.core.data.response.pagerep.PageResponse
import com.mir.core.data.response.repository.RepositoryResponse

object Mapper {
    fun mapIssuesResponseToAppModel(
        issuesResponse: List<IssuesItemResponse>
    ): List<Issues> = issuesResponse.map { i ->
        Issues(
            id = i.id ?: 0,
            comments = i.comments ?: 0,
            body = i.body ?: "",
            title = i.title ?: "",
            state = i.state ?: "",
            updatedAt = i.updated_at ?: "",
            createdAt = i.created_at ?: "",
            url = i.url ?: "",
            reactions = i.reactions
        )
    }

    fun mapRepositoryResponseToAppModel(
        repositoryResponse: RepositoryResponse,
    ): RepositoryItem = RepositoryItem(
        owner = Owner(
            avatarUrl = repositoryResponse.owner?.avatarUrl ?: "",
            name = repositoryResponse.owner?.login ?: ""
        ),
        description = repositoryResponse.description ?: "",
        name = repositoryResponse.name ?: "",
        starCount = repositoryResponse.starCount,
        forkCount = repositoryResponse.forksCount,
        issuesCount = repositoryResponse.openIssuesCount,
        watchersCount = repositoryResponse.watchersCount
    )

    fun mapPageRepositoryResponseToAppModel(
        pageResponse: PageResponse,
        nextPage: String?
    ): PageRepository = PageRepository(
        incompleteResults = pageResponse.incompleteResults!!,
        items = pageResponse.items.map {
            RepositoryItem(
                owner = Owner(
                    avatarUrl = it.owner?.avatarUrl ?: "",
                    name = it.owner?.login ?: ""
                ),
                description = it.description ?: "",
                name = it.name ?: "",
                starCount = it.stargazersCount.toString(),
                forkCount = it.forksCount.toString(),
                issuesCount = it.openIssuesCount.toString(),
                watchersCount = it.watchersCount.toString()

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