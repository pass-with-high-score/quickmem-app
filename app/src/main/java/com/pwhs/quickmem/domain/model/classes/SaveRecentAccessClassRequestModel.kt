package com.pwhs.quickmem.domain.model.classes

data class SaveRecentAccessClassRequestModel(
    val userId: String,
    val classId: String,
)
