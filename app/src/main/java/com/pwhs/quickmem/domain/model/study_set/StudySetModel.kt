package com.pwhs.quickmem.domain.model.study_set

data class StudySetModel(
    val id: String,
    val title: String,
    val description: String = "",
    val flashcardCount: Int,
    val colorHex: String,
    val subjectName: String,
    val ownerId: String,
    val ownerUsername: String,
    val ownerAvatarUrl: String,
    val isPublic: Boolean,
)