package com.pwhs.quickmem.data.dto.flashcard

import kotlinx.serialization.SerialName
import com.pwhs.quickmem.core.data.enums.FlipCardStatus
import com.pwhs.quickmem.core.data.enums.QuizStatus
import com.pwhs.quickmem.core.data.enums.Rating
import com.pwhs.quickmem.core.data.enums.TrueFalseStatus
import com.pwhs.quickmem.core.data.enums.WriteStatus
import kotlinx.serialization.Serializable

@Serializable
data class UpdateFlashCardResponseDto(
    @SerialName("id")
    val id: String,
    @SerialName("message")
    val message: String,
    @SerialName("isStarred")
    val isStarred: Boolean? = false,
    @SerialName("rating")
    val rating: String? = Rating.NOT_STUDIED.name,
    @SerialName("flipStatus")
    val flipStatus: String? = FlipCardStatus.NONE.name,
    @SerialName("quizStatus")
    val quizStatus: String? = QuizStatus.NONE.name,
    @SerialName("trueFalseStatus")
    val trueFalseStatus: TrueFalseStatus? = TrueFalseStatus.NONE,
    @SerialName("writeStatus")
    val writeStatus: WriteStatus? = WriteStatus.NONE,
)
