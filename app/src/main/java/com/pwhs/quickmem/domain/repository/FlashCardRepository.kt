package com.pwhs.quickmem.domain.repository

import com.pwhs.quickmem.core.data.enums.LearnMode
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.flashcard.BufferResponseModel
import com.pwhs.quickmem.domain.model.flashcard.CreateFlashCardModel
import com.pwhs.quickmem.domain.model.flashcard.EditFlashCardModel
import com.pwhs.quickmem.domain.model.flashcard.FlashCardResponseModel
import com.pwhs.quickmem.domain.model.flashcard.LanguageModel
import com.pwhs.quickmem.domain.model.flashcard.UpdateFlashCardResponseModel
import com.pwhs.quickmem.domain.model.flashcard.VoiceModel
import kotlinx.coroutines.flow.Flow

interface FlashCardRepository {
    suspend fun createFlashCard(
        token: String,
        createFlashCardModel: CreateFlashCardModel,
    ): Flow<Resources<FlashCardResponseModel>>

    suspend fun deleteFlashCard(
        token: String,
        id: String,
    ): Flow<Resources<Unit>>

    suspend fun toggleStarredFlashCard(
        token: String,
        id: String,
        isStarred: Boolean,
    ): Flow<Resources<UpdateFlashCardResponseModel>>

    suspend fun updateFlashCard(
        token: String,
        id: String,
        editFlashCardModel: EditFlashCardModel,
    ): Flow<Resources<FlashCardResponseModel>>

    suspend fun updateFlipFlashCard(
        token: String,
        id: String,
        flipStatus: String,
    ): Flow<Resources<UpdateFlashCardResponseModel>>

    suspend fun updateFlashCardRating(
        token: String,
        id: String,
        rating: String,
    ): Flow<Resources<UpdateFlashCardResponseModel>>

    suspend fun updateQuizStatus(
        token: String,
        id: String,
        quizStatus: String,
    ): Flow<Resources<UpdateFlashCardResponseModel>>

    suspend fun updateTrueFalseStatus(
        token: String,
        id: String,
        trueFalseStatus: String,
    ): Flow<Resources<UpdateFlashCardResponseModel>>

    suspend fun updateWriteStatus(
        token: String,
        id: String,
        writeStatus: String,
    ): Flow<Resources<UpdateFlashCardResponseModel>>

    suspend fun getFlashCardsByStudySetId(
        token: String,
        studySetId: String,
        learnMode: LearnMode,
        isGetAll: Boolean,
        isSwapped: Boolean,
        isRandom: Boolean,
    ): Flow<Resources<List<FlashCardResponseModel>>>

    suspend fun getFlashCardsByFolderId(
        token: String,
        folderId: String,
        learnMode: LearnMode,
        isGetAll: Boolean,
        isSwapped: Boolean,
        isRandom: Boolean,
    ): Flow<Resources<List<FlashCardResponseModel>>>

    suspend fun getLanguages(
        token: String,
    ): Flow<Resources<List<LanguageModel>>>

    suspend fun getVoices(
        token: String,
        languageCode: String,
    ): Flow<Resources<List<VoiceModel>>>

    suspend fun getSpeech(
        token: String,
        input: String,
        voiceCode: String,
    ): Flow<Resources<BufferResponseModel>>
}