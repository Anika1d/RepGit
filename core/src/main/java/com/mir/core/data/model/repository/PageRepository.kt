package com.mir.core.data.model.repository



data class PageRepository(
    val incompleteResults: Boolean?,
    val items: List<RepositoryItem>,
    val totalCount: Int?,
    val nextPage:String?,
) {
}