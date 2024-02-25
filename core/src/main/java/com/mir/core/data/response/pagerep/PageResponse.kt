package com.mir.core.data.response.pagerep

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageResponse(
    @SerialName("incomplete_results") val incompleteResults: Boolean?,
    @SerialName("items") val items: List<Item>,
    @SerialName("total_count") val totalCount: Int?
)