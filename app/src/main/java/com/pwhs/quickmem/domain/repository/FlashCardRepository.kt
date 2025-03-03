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
        createFlashCardModel: CreateFlashCardModel,
    ): Flow<Resources<FlashCardResponseModel>>

    suspend fun deleteFlashCard(
        id: String,
    ): Flow<Resources<Unit>>

    suspend fun updateFlashCard(
        id: String, editFlashCardModel: EditFlashCardModel,
    ): Flow<Resources<FlashCardResponseModel>>

    suspend fun updateFlipFlashCard(
        id: String, flipStatus: String,
    ): Flow<Resources<UpdateFlashCardResponseModel>>

    suspend fun updateFlashCardRating(
        id: String, rating: String,
    ): Flow<Resources<UpdateFlashCardResponseModel>>

    suspend fun updateQuizStatus(
        id: String, quizStatus: String,
    ): Flow<Resources<UpdateFlashCardResponseModel>>

    suspend fun updateTrueFalseStatus(
        id: String, trueFalseStatus: String,
    ): Flow<Resources<UpdateFlashCardResponseModel>>

    suspend fun updateWriteStatus(
        id: String, writeStatus: String,
    ): Flow<Resources<UpdateFlashCardResponseModel>>

    suspend fun getFlashCardsByStudySetId(
        studySetId: String,
        learnMode: LearnMode,
        isGetAll: Boolean,
        isSwapped: Boolean,
        isRandom: Boolean,
    ): Flow<Resources<List<FlashCardResponseModel>>>

    suspend fun getFlashCardsByFolderId(
        folderId: String,
        learnMode: LearnMode,
        isGetAll: Boolean,
        isSwapped: Boolean,
        isRandom: Boolean,
    ): Flow<Resources<List<FlashCardResponseModel>>>

    suspend fun getLanguages(
    ): Flow<Resources<List<LanguageModel>>>

    suspend fun getVoices(
        languageCode: String,
    ): Flow<Resources<List<VoiceModel>>>

    suspend fun getSpeech(
        input: String, voiceCode: String,
    ): Flow<Resources<BufferResponseModel>>
}