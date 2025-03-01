package com.pwhs.quickmem.presentation.app.study_set.detail

import androidx.compose.ui.graphics.Color
import com.pwhs.quickmem.domain.model.color.ColorModel
import com.pwhs.quickmem.domain.model.flashcard.StudySetFlashCardResponseModel
import com.pwhs.quickmem.domain.model.study_time.GetStudyTimeByStudySetResponseModel
import com.pwhs.quickmem.domain.model.subject.SubjectModel
import com.pwhs.quickmem.domain.model.users.UserResponseModel

data class StudySetDetailUiState(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val color: Color = Color.White,
    val colorModel: ColorModel = ColorModel(),
    val subject: SubjectModel = SubjectModel(),
    val flashCardCount: Int = 0,
    val isAIGenerated: Boolean = false,
    val flashCards: List<StudySetFlashCardResponseModel> = emptyList(),
    val idOfFlashCardSelected: String = "",
    val isPublic: Boolean = false,
    val user: UserResponseModel = UserResponseModel(),
    val studyTime: GetStudyTimeByStudySetResponseModel? = null,
    val linkShareCode: String = "",
    val createdAt: String = "",
    val updatedAt: String = "",
    val isLoading: Boolean = false,
    val shouldLoad: Boolean = false,
    val isOwner: Boolean = false,
    val isSpeaking: Boolean = false,
    val flashcardCurrentPlayId: String = "",
    val previousDefinitionVoiceCode: String = "",
    val previousTermVoiceCode: String = "",
)