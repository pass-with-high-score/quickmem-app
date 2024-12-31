package com.pwhs.quickmem.data.mapper.auth

import com.pwhs.quickmem.data.dto.auth.AuthSocialGoogleRequestDto
import com.pwhs.quickmem.domain.model.auth.AuthSocialGoogleRequestModel

fun AuthSocialGoogleRequestDto.toModel() = AuthSocialGoogleRequestModel(
    id = id,
    email = email,
    provider = provider,
    displayName = displayName,
    photoUrl = photoUrl,
    idToken = idToken,
)

fun AuthSocialGoogleRequestModel.toDto() = AuthSocialGoogleRequestDto(
    id = id,
    email = email,
    provider = provider,
    displayName = displayName,
    photoUrl = photoUrl,
    idToken = idToken,
)