package com.pwhs.quickmem.data.dto.study_set

import com.google.gson.annotations.SerializedName
import com.pwhs.quickmem.data.dto.color.ColorResponseDto
import com.pwhs.quickmem.data.dto.flashcard.StudySetFlashCardResponseDto
import com.pwhs.quickmem.data.dto.subject.SubjectResponseDto
import com.pwhs.quickmem.data.dto.user.UserResponseDto

data class GetStudySetResponseDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("isPublic")
    val isPublic: Boolean,
    @SerializedName("isAIGenerated")
    val isAIGenerated: Boolean?,
    @SerializedName("flashcardCount")
    val flashcardCount: Int,
    @SerializedName("flashcards")
    var flashcards: List<StudySetFlashCardResponseDto>? = null,
    @SerializedName("subject")
    val subject: SubjectResponseDto? = null,
    @SerializedName("color")
    val color: ColorResponseDto? = null,
    @SerializedName("owner")
    val owner: UserResponseDto,
    @SerializedName("linkShareCode")
    val linkShareCode: String? = null,
    @SerializedName("isImported")
    val isImported: Boolean? = null,
    @SerializedName("previousDefinitionVoiceCode")
    val previousDefinitionVoiceCode: String? = null,
    @SerializedName("previousTermVoiceCode")
    val previousTermVoiceCode: String? = null,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
)
