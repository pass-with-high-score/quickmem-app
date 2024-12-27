package com.pwhs.quickmem.domain.model.classes

import com.pwhs.quickmem.core.data.enums.InviteStatus

data class InviteToClassResponseModel(
    val message: String,
    val status: Boolean,
    val inviteStatus: String = InviteStatus.SUCCESS.name,
)
