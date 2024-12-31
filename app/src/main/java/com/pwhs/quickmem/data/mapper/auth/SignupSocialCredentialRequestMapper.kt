package com.pwhs.quickmem.data.mapper.auth

import com.pwhs.quickmem.data.dto.auth.SignupSocialCredentialRequestDto
import com.pwhs.quickmem.domain.model.auth.SignupSocialCredentialRequestModel

fun SignupSocialCredentialRequestModel.toDto() = SignupSocialCredentialRequestDto(
    username = username,
    email = email,
    idToken = idToken,
    photoUrl = photoUrl,
    role = role,
    birthday = birthday,
    id = id,
    provider = provider,
    displayName = displayName,
)

fun SignupSocialCredentialRequestDto.toModel() = SignupSocialCredentialRequestModel(
    username = username,
    email = email,
    idToken = idToken,
    photoUrl = photoUrl,
    role = role,
    birthday = birthday,
    id = id,
    provider = provider,
    displayName = displayName,
)