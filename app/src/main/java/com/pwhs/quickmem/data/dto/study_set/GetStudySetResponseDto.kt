package com.pwhs.quickmem.data.dto.study_set

import kotlinx.serialization.SerialName
import com.pwhs.quickmem.data.dto.color.ColorResponseDto
import com.pwhs.quickmem.data.dto.flashcard.StudySetFlashCardResponseDto
import com.pwhs.quickmem.data.dto.subject.SubjectResponseDto
import com.pwhs.quickmem.data.dto.user.UserResponseDto
import kotlinx.serialization.Serializable

@Serializable
data class GetStudySetResponseDto(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String?,
    @SerialName("isPublic")
    val isPublic: Boolean?,
    @SerialName("isAIGenerated")
    val isAIGenerated: Boolean?,
    @SerialName("flashcardCount")
    val flashcardCount: Int,
    @SerialName("flashcards")
    var flashcards: List<StudySetFlashCardResponseDto>? = null,
    @SerialName("subject")
    val subject: SubjectResponseDto? = null,
    @SerialName("color")
    val color: ColorResponseDto? = null,
    @SerialName("owner")
    val owner: UserResponseDto,
    @SerialName("linkShareCode")
    val linkShareCode: String? = null,
    @SerialName("isImported")
    val isImported: Boolean? = null,
    @SerialName("previousDefinitionVoiceCode")
    val previousDefinitionVoiceCode: String? = null,
    @SerialName("previousTermVoiceCode")
    val previousTermVoiceCode: String? = null,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String,
)
