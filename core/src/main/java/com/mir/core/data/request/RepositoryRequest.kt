package com.mir.core.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepositoryRequest (
    @SerialName("name_owner") val nameOwner:String,
   @SerialName("name") val nameRepository:String
)