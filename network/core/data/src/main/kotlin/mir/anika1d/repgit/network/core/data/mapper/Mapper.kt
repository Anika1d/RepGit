package mir.anika1d.repgit.network.core.data.mapper

import mir.anika1d.repgit.network.core.data.model.error.GithubError
import mir.anika1d.repgit.network.core.data.model.issues.Issues
import mir.anika1d.repgit.network.core.data.model.repository.Owner
import mir.anika1d.repgit.network.core.data.model.repository.PageRepository
import mir.anika1d.repgit.network.core.data.model.repository.RepositoryItem
import mir.anika1d.repgit.network.core.data.model.user.PageUser
import mir.anika1d.repgit.network.core.data.model.user.User
import mir.anika1d.repgit.network.core.data.response.error.ErrorResponse
import mir.anika1d.repgit.network.core.data.response.issues.IssuesItemResponse
import mir.anika1d.repgit.network.core.data.response.pagerep.PageRepositoryResponse
import mir.anika1d.repgit.network.core.data.response.pagerep.RepositoryItemResponse
import mir.anika1d.repgit.network.core.data.response.user.PageUserResponse
import mir.anika1d.repgit.network.core.data.response.user.UserResponse

object Mapper {
    fun mapIssuesResponseToAppModel(
        issuesResponse: List<IssuesItemResponse>
    ): List<Issues> = issuesResponse.map { i ->
        Issues(
            id = i.id ?: 0,
            comments = i.comments ?: 0,
            body = i.body.orEmpty(),
            title = i.title.orEmpty(),
            state = i.state.orEmpty(),
            updatedAt = i.updated_at.orEmpty(),
            createdAt = i.created_at.orEmpty(),
            url = i.url.orEmpty(),
            reactions = i.reactions
        )
    }

    fun mapRepositoryResponseToAppModel(
        repositoryResponse: RepositoryItemResponse,
    ): RepositoryItem = RepositoryItem(
        owner = Owner(
            avatarUrl = repositoryResponse.owner?.avatarUrl.orEmpty(),
            name = repositoryResponse.owner?.login.orEmpty()
        ),
        description = repositoryResponse.description.orEmpty(),
        name = repositoryResponse.name.orEmpty(),
        starCount = repositoryResponse.stargazersCount?.toString().orEmpty(),
        forkCount = repositoryResponse.forksCount?.toString().orEmpty(),
        issuesCount = repositoryResponse.openIssuesCount?.toString().orEmpty(),
        watchersCount = repositoryResponse.watchersCount?.toString().orEmpty(),
        url = repositoryResponse.svnUrl ?: "github.com",
        downloadUrl = "${repositoryResponse.svnUrl}/archive/refs/heads/${repositoryResponse.defaultBranch}.zip",
        language = repositoryResponse.language.orEmpty(),
        defaultBranch = repositoryResponse.defaultBranch ?: "master",
        updatedAt = repositoryResponse.updatedAt.orEmpty()
    )

    fun mapPageRepositoryResponseToAppModel(
        pageResponse: PageRepositoryResponse,
        nextPage: String?
    ): PageRepository = PageRepository(
        incompleteResults = pageResponse.incompleteResults!!,
        items = pageResponse.items.map { mapRepositoryResponseToAppModel(it) },
        totalCount = pageResponse.totalCount,
        nextPage = nextPage
    )

    fun mapErrorResponseToAppModel(errorResponse: ErrorResponse) = GithubError(
        errors = errorResponse.errors ?: listOf(),
        message = errorResponse.message
    )

    fun mapUserResponseToAppModel(userResponse: UserResponse) = User(
        name = userResponse.login.orEmpty(),
        avatarUrl = userResponse.avatarUrl.orEmpty(),
        followersCount = userResponse.followers?.toString() ?: "0",
        repositoryCount = ((userResponse.publicRepos ?: 0) + (userResponse.ownedPrivateRepos
            ?: 0)).toString()
    )

    fun mapRepositoryListToAppModel(
        rep: List<RepositoryItemResponse>,
        nextPage: String?
    ): PageRepository {
        return PageRepository(
            incompleteResults = null,
            items = rep.map { mapRepositoryResponseToAppModel(it) },
            totalCount = rep.size,
            nextPage = nextPage
        )

    }

    fun mapPageUserResponseToAppModel(
        pageResponse: PageUserResponse,
        nextPage: String?
    ): PageUser = PageUser(
        incompleteResults = pageResponse.incompleteResults,
        items = pageResponse.items.map { mapUserResponseToAppModel(it) },
        totalCount = pageResponse.totalCount,
        nextPage = nextPage
    )


}