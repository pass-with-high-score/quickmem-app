package com.pwhs.quickmem.data.mapper.user

import com.pwhs.quickmem.data.dto.user.UpdateCoinRequestDto
import com.pwhs.quickmem.domain.model.users.UpdateCoinRequestModel

fun UpdateCoinRequestDto.toModel() = UpdateCoinRequestModel(
    coin = coin,
    action = action
)

fun UpdateCoinRequestModel.toDto() = UpdateCoinRequestDto(
    coin = coin,
    action = action
)