package com.mir.core.usecase

import com.mir.core.data.model.issues.Issues
import com.mir.core.data.request.IssuesRequest
import com.mir.core.data.response.ResultState
import com.mir.core.repository.ImplGithubRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.inject

class IssuesUseCase:KoinComponent {
    private val repository by inject<ImplGithubRepository>()
    suspend fun getIssues(issuesRequest: IssuesRequest): Flow<ResultState<List<Issues>>> {
        return repository.getIssues(issuesRequest)
    }
    suspend  operator fun invoke(issuesRequest: IssuesRequest):Flow<ResultState<List<Issues>>> {
        return getIssues(issuesRequest)
    }
}