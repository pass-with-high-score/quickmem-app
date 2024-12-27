package com.pwhs.quickmem.data.remote.repository

import com.pwhs.quickmem.core.data.enums.LearnMode
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.data.dto.flashcard.FlipFlashCardDto
import com.pwhs.quickmem.data.dto.flashcard.QuizStatusFlashCardDto
import com.pwhs.quickmem.data.dto.flashcard.RatingFlashCardDto
import com.pwhs.quickmem.data.dto.flashcard.ToggleStarredFlashCardDto
import com.pwhs.quickmem.data.dto.flashcard.TrueFalseStatusFlashCardDto
import com.pwhs.quickmem.data.dto.flashcard.WriteStatusFlashCardDto
import com.pwhs.quickmem.data.mapper.flashcard.toDto
import com.pwhs.quickmem.data.mapper.flashcard.toModel
import com.pwhs.quickmem.data.remote.ApiService
import com.pwhs.quickmem.domain.model.flashcard.CreateFlashCardModel
import com.pwhs.quickmem.domain.model.flashcard.EditFlashCardModel
import com.pwhs.quickmem.domain.model.flashcard.FlashCardResponseModel
import com.pwhs.quickmem.domain.model.flashcard.UpdateFlashCardResponseModel
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
        token: String,
        createFlashCardModel: CreateFlashCardModel,
    ): Flow<Resources<FlashCardResponseModel>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                val response = apiService.createFlashCard(
                    token = token,
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
        token: String,
        id: String,
    ): Flow<Resources<Unit>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                val response = apiService.deleteFlashCard(token = token, id = id)
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

    override suspend fun toggleStarredFlashCard(
        token: String,
        id: String,
        isStarred: Boolean,
    ): Flow<Resources<UpdateFlashCardResponseModel>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                val response = apiService.toggleStarredFlashCard(
                    token = token,
                    id = id,
                    toggleStarredFlashCardDto = ToggleStarredFlashCardDto(isStarred)
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

    override suspend fun updateFlashCard(
        token: String,
        id: String,
        editFlashCardModel: EditFlashCardModel,
    ): Flow<Resources<FlashCardResponseModel>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                val response = apiService.updateFlashCard(
                    token = token,
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
        token: String,
        id: String,
        flipStatus: String,
    ): Flow<Resources<UpdateFlashCardResponseModel>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            try {
                val response =
                    apiService.updateFlipFlashCard(
                        token = token,
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
        token: String,
        id: String,
        rating: String,
    ): Flow<Resources<UpdateFlashCardResponseModel>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            try {
                val response =
                    apiService.updateRatingFlashCard(
                        token = token,
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
        token: String,
        id: String,
        quizStatus: String,
    ): Flow<Resources<UpdateFlashCardResponseModel>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            try {
                val response =
                    apiService.updateQuizStatus(
                        token = token,
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
        token: String,
        id: String,
        trueFalseStatus: String,
    ): Flow<Resources<UpdateFlashCardResponseModel>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            try {
                val response = apiService.updateTrueFalseStatus(
                    token = token,
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
        token: String,
        id: String,
        writeStatus: String,
    ): Flow<Resources<UpdateFlashCardResponseModel>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            try {
                val response = apiService.updateWriteStatus(
                    token = token,
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
        token: String,
        studySetId: String,
        learnMode: LearnMode,
        isGetAll: Boolean,
        isSwapped: Boolean,
        isRandom: Boolean,
    ): Flow<Resources<List<FlashCardResponseModel>>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                val response = apiService.getFlashCardsByStudySetId(
                    token = token,
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
        token: String,
        folderId: String,
        learnMode: LearnMode,
        isGetAll: Boolean,
        isSwapped: Boolean,
        isRandom: Boolean,
    ): Flow<Resources<List<FlashCardResponseModel>>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                val response = apiService.getFlashCardsByFolderId(
                    token = token,
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
}