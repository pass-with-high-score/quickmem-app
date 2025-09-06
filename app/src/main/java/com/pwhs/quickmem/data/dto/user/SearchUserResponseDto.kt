package com.pwhs.quickmem.data.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SearchUserResponseDto (
    @SerialName("id")
    val id: String,
    @SerialName("username")
    val username: String,
    @SerialName("avatarUrl")
    val avatarUrl: String,
)