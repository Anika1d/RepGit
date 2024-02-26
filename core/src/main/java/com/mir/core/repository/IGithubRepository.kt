package com.mir.core.repository

import com.mir.core.data.model.issues.Issues
import com.mir.core.data.model.repository.PageRepository
import com.mir.core.data.model.repository.RepositoryItem
import com.mir.core.data.request.IssuesRequest
import com.mir.core.data.request.RepositoryRequest
import com.mir.core.data.request.SearchRequest
import com.mir.core.data.response.ResultState
import kotlinx.coroutines.flow.Flow

interface IGithubRepository{
    suspend fun search(searchRequest: SearchRequest): Flow<ResultState<PageRepository>>
    suspend fun getRepository(repositoryRequest: RepositoryRequest): Flow<ResultState<RepositoryItem>>

    suspend fun getIssues(issuesRequest:IssuesRequest): Flow<ResultState<List<Issues>>>
}