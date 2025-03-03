package com.pwhs.quickmem.data.remote.repository

import com.pwhs.quickmem.core.data.enums.LearnMode
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.data.dto.flashcard.FlipFlashCardDto
import com.pwhs.quickmem.data.dto.flashcard.QuizStatusFlashCardDto
import com.pwhs.quickmem.data.dto.flashcard.RatingFlashCardDto
import com.pwhs.quickmem.data.dto.flashcard.TrueFalseStatusFlashCardDto
import com.pwhs.quickmem.data.dto.flashcard.WriteStatusFlashCardDto
import com.pwhs.quickmem.data.mapper.flashcard.toDto
import com.pwhs.quickmem.data.mapper.flashcard.toModel
import com.pwhs.quickmem.data.remote.ApiService
import com.pwhs.quickmem.domain.model.flashcard.BufferResponseModel
import com.pwhs.quickmem.domain.model.flashcard.CreateFlashCardModel
import com.pwhs.quickmem.domain.model.flashcard.EditFlashCardModel
import com.pwhs.quickmem.domain.model.flashcard.FlashCardResponseModel
import com.pwhs.quickmem.domain.model.flashcard.LanguageModel
import com.pwhs.quickmem.domain.model.flashcard.UpdateFlashCardResponseModel
import com.pwhs.quickmem.domain.model.flashcard.VoiceModel
import com.pwhs.quickmem.domain.repository.FlashCardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class FlashCardRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : FlashCardRepository {
    override suspend fun createFlashCard(
        createFlashCardModel: CreateFlashCardModel,
    ): Flow<Resources<FlashCardResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.createFlashCard(
                    createFlashCardDto = createFlashCardModel.toDto()
                )
                emit(Resources.Success(response.toModel()))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun deleteFlashCard(
        id: String,
    ): Flow<Resources<Unit>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.deleteFlashCard(id = id)
                emit(Resources.Success(response))
            } catch (e: HttpException) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            } catch (e: IOException) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun updateFlashCard(
        id: String,
        editFlashCardModel: EditFlashCardModel,
    ): Flow<Resources<FlashCardResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.updateFlashCard(
                    id = id,
                    editFlashCardDto = editFlashCardModel.toDto()
                )
                emit(Resources.Success(response.toModel()))
            } catch (e: HttpException) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            } catch (e: IOException) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun updateFlipFlashCard(
        id: String,
        flipStatus: String,
    ): Flow<Resources<UpdateFlashCardResponseModel>> {
        return flow {
            try {
                val response =
                    apiService.updateFlipFlashCard(
                        id = id,
                        flipFlashCardDto = FlipFlashCardDto(flipStatus)
                    )
                emit(Resources.Success(response.toModel()))
            } catch (e: HttpException) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            } catch (e: IOException) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun updateFlashCardRating(
        id: String,
        rating: String,
    ): Flow<Resources<UpdateFlashCardResponseModel>> {
        return flow {
            try {
                val response =
                    apiService.updateRatingFlashCard(
                        id = id,
                        ratingFlashCardDto = RatingFlashCardDto(rating)
                    )
                emit(Resources.Success(response.toModel()))
            } catch (e: HttpException) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            } catch (e: IOException) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun updateQuizStatus(
        id: String,
        quizStatus: String,
    ): Flow<Resources<UpdateFlashCardResponseModel>> {
        return flow {
            try {
                val response = apiService.updateQuizStatus(
                    id = id,
                    quizStatusDto = QuizStatusFlashCardDto(quizStatus)
                )
                emit(Resources.Success(response.toModel()))
            } catch (e: HttpException) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            } catch (e: IOException) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun updateTrueFalseStatus(
        id: String,
        trueFalseStatus: String,
    ): Flow<Resources<UpdateFlashCardResponseModel>> {
        return flow {
            try {
                val response = apiService.updateTrueFalseStatus(
                    id = id,
                    trueFalseStatusDto = TrueFalseStatusFlashCardDto(trueFalseStatus)
                )
                emit(Resources.Success(response.toModel()))
            } catch (e: HttpException) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            } catch (e: IOException) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun updateWriteStatus(
        id: String,
        writeStatus: String,
    ): Flow<Resources<UpdateFlashCardResponseModel>> {
        return flow {
            try {
                val response = apiService.updateWriteStatus(
                    id = id,
                    writeStatusDto = WriteStatusFlashCardDto(writeStatus)
                )
                emit(Resources.Success(response.toModel()))
            } catch (e: HttpException) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            } catch (e: IOException) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun getFlashCardsByStudySetId(
        studySetId: String,
        learnMode: LearnMode,
        isGetAll: Boolean,
        isSwapped: Boolean,
        isRandom: Boolean,
    ): Flow<Resources<List<FlashCardResponseModel>>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.getFlashCardsByStudySetId(
                    id = studySetId,
                    learnMode = learnMode.mode,
                    isGetAll = isGetAll,
                    isSwapped = isSwapped,
                    isRandom = isRandom
                )
                emit(Resources.Success(response.map { it.toModel() }))
            } catch (e: HttpException) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            } catch (e: IOException) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun getFlashCardsByFolderId(
        folderId: String,
        learnMode: LearnMode,
        isGetAll: Boolean,
        isSwapped: Boolean,
        isRandom: Boolean,
    ): Flow<Resources<List<FlashCardResponseModel>>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.getFlashCardsByFolderId(
                    id = folderId,
                    learnMode = learnMode.mode,
                    isGetAll = isGetAll,
                    isSwapped = isSwapped,
                    isRandom = isRandom
                )
                emit(Resources.Success(response.map { it.toModel() }))
            } catch (e: HttpException) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            } catch (e: IOException) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun getLanguages(): Flow<Resources<List<LanguageModel>>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.getLanguages()
                emit(Resources.Success(response.map { it.toModel() }))
            } catch (e: HttpException) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            } catch (e: IOException) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun getVoices(
        languageCode: String,
    ): Flow<Resources<List<VoiceModel>>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.getVoices(languageCode = languageCode)
                emit(Resources.Success(response.map { it.toModel() }))
            } catch (e: HttpException) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            } catch (e: IOException) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun getSpeech(
        input: String,
        voiceCode: String
    ): Flow<Resources<BufferResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response =
                    apiService.getSpeech(input = input, voiceCode = voiceCode)
                emit(Resources.Success(response.toModel()))
            } catch (e: HttpException) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            } catch (e: IOException) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }
}