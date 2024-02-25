package com.mir.core.repository

import com.mir.core.data.model.repository.PageRepository
import com.mir.core.data.request.SearchRequest
import com.mir.core.data.response.ResultState
import kotlinx.coroutines.flow.Flow

interface IGithubRepository{
    suspend fun search(searchRequest: SearchRequest): Flow<ResultState<PageRepository>>
}