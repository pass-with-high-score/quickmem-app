package com.pwhs.quickmem.domain.model.study_set

import com.pwhs.quickmem.domain.model.color.ColorModel
import com.pwhs.quickmem.domain.model.flashcard.StudySetFlashCardResponseModel
import com.pwhs.quickmem.domain.model.subject.SubjectModel
import com.pwhs.quickmem.domain.model.users.UserResponseModel

data class GetStudySetResponseModel(
    val id: String,
    val title: String,
    val description: String?,
    val isPublic: Boolean?,
    val isAIGenerated: Boolean?,
    val subject: SubjectModel? = null,
    val color: ColorModel? = null,
    val owner: UserResponseModel,
    val flashcardCount: Int,
    val linkShareCode: String? = null,
    val flashcards: List<StudySetFlashCardResponseModel>,
    val isImported: Boolean? = null,
    val createdAt: String,
    val updatedAt: String,
    val previousDefinitionVoiceCode: String? = null,
    val previousTermVoiceCode: String? = null,
)
