package com.pwhs.quickmem.domain.model.flashcard

import com.pwhs.quickmem.core.data.enums.FlipCardStatus
import com.pwhs.quickmem.core.data.enums.QuizStatus
import com.pwhs.quickmem.core.data.enums.Rating
import com.pwhs.quickmem.core.data.enums.TrueFalseStatus
import com.pwhs.quickmem.core.data.enums.WriteStatus

data class StudySetFlashCardResponseModel(
    val id: String,
    val term: String,
    val termImageURL: String? = null,
    val definition: String,
    val definitionImageURL: String? = null,
    val hint: String? = null,
    val explanation: String? = null,
    val isAIGenerated: Boolean = false,
    val rating: String = Rating.NOT_STUDIED.name,
    val flipStatus: String = FlipCardStatus.NONE.name,
    val quizStatus: String = QuizStatus.NONE.status,
    val trueFalseStatus: String = TrueFalseStatus.NONE.status,
    val writeStatus: String = WriteStatus.NONE.status,
    val isStarred: Boolean = false,
    val createdAt: String = "",
    val updatedAt: String = "",
    val termVoiceCode: String?,
    val definitionVoiceCode: String?,
)
