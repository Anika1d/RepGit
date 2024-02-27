package com.mir.database.data.request

data class SearchQueryRequest(
    val name: String,
    val limit:Int?=null
)