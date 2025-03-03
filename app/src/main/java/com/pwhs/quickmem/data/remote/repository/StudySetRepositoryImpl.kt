package com.pwhs.quickmem.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.data.dto.study_set.MakeACopyStudySetRequestDto
import com.pwhs.quickmem.data.mapper.classes.toDto
import com.pwhs.quickmem.data.mapper.study_set.toDto
import com.pwhs.quickmem.data.mapper.study_set.toModel
import com.pwhs.quickmem.data.mapper.subject.toModel
import com.pwhs.quickmem.data.paging.StudySetBySubjectPagingSource
import com.pwhs.quickmem.data.paging.StudySetPagingSource
import com.pwhs.quickmem.data.remote.ApiService
import com.pwhs.quickmem.domain.datasource.SearchStudySetBySubjectRemoteDataSource
import com.pwhs.quickmem.domain.datasource.StudySetRemoteDataSource
import com.pwhs.quickmem.domain.model.classes.AddStudySetToClassesRequestModel
import com.pwhs.quickmem.domain.model.study_set.AddStudySetToClassRequestModel
import com.pwhs.quickmem.domain.model.study_set.AddStudySetToFolderRequestModel
import com.pwhs.quickmem.domain.model.study_set.AddStudySetToFoldersRequestModel
import com.pwhs.quickmem.domain.model.study_set.CreateStudySetByAIRequestModel
import com.pwhs.quickmem.domain.model.study_set.CreateStudySetRequestModel
import com.pwhs.quickmem.domain.model.study_set.CreateStudySetResponseModel
import com.pwhs.quickmem.domain.model.study_set.CreateWriteHintAIRequestModel
import com.pwhs.quickmem.domain.model.study_set.CreateWriteHintAIResponseModel
import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel
import com.pwhs.quickmem.domain.model.study_set.SaveRecentAccessStudySetRequestModel
import com.pwhs.quickmem.domain.model.study_set.UpdateStudySetRequestModel
import com.pwhs.quickmem.domain.model.study_set.UpdateStudySetResponseModel
import com.pwhs.quickmem.domain.model.subject.GetTop5SubjectResponseModel
import com.pwhs.quickmem.domain.repository.StudySetRepository
import com.pwhs.quickmem.presentation.app.search_result.study_set.enums.SearchResultCreatorEnum
import com.pwhs.quickmem.presentation.app.search_result.study_set.enums.SearchResultSizeEnum
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class StudySetRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val studySetRemoteDataSource: StudySetRemoteDataSource,
    private val searchStudySetBySubjectRemoteDataSource: SearchStudySetBySubjectRemoteDataSource,
) : StudySetRepository {
    override suspend fun createStudySet(
        createStudySetRequestModel: CreateStudySetRequestModel,
    ): Flow<Resources<CreateStudySetResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.createStudySet(
                    createStudySetRequestDto = createStudySetRequestModel.toDto()
                )
                emit(Resources.Success(response.toModel()))
            } catch (e: Exception) {
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun getStudySetById(
        studySetId: String,
    ): Flow<Resources<GetStudySetResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.getStudySetById(id = studySetId)
                emit(Resources.Success(response.toModel()))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun getStudySetsByOwnerId(
        classId: String?,
        folderId: String?,
    ): Flow<Resources<List<GetStudySetResponseModel>>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.getStudySetsByOwnerId(
                    classId = classId, folderId = folderId
                )
                emit(Resources.Success(response.map { it.toModel() }))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun updateStudySet(
        studySetId: String,
        updateStudySetRequestModel: UpdateStudySetRequestModel,
    ): Flow<Resources<UpdateStudySetResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.updateStudySet(
                    id = studySetId,
                    updateStudySetRequestDto = updateStudySetRequestModel.toDto()
                )
                emit(Resources.Success(response.toModel()))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun deleteStudySet(
        studySetId: String,
    ): Flow<Resources<Unit>> {
        return flow {
            emit(Resources.Loading())
            try {
                apiService.deleteStudySet(id = studySetId)
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun resetProgress(
        studySetId: String,
        resetType: String,
    ): Flow<Resources<Unit>> {
        return flow {
            emit(Resources.Loading())
            try {
                apiService.resetStudySetProgress(
                    id = studySetId, resetType = resetType
                )
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun addStudySetToFolder(
        addStudySetToFolderRequestModel: AddStudySetToFolderRequestModel,
    ): Flow<Resources<Unit>> {
        return flow {
            emit(Resources.Loading())
            try {
                apiService.addStudySetToFolder(
                    addStudySetToFolderRequestDto = addStudySetToFolderRequestModel.toDto()
                )
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun addStudySetToClass(
        addStudySetToClassRequestModel: AddStudySetToClassRequestModel,
    ): Flow<Resources<Unit>> {
        return flow {
            emit(Resources.Loading())
            try {
                apiService.addStudySetToClass(
                    addStudySetToClassRequestDto = addStudySetToClassRequestModel.toDto()
                )
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun addStudySetToFolders(
        addStudySetToFoldersRequestModel: AddStudySetToFoldersRequestModel,
    ): Flow<Resources<Unit>> {
        return flow {
            emit(Resources.Loading())
            try {
                apiService.addStudySetToFolders(
                    addStudySetToFoldersRequestDto = addStudySetToFoldersRequestModel.toDto()
                )
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun addStudySetToClasses(
        addStudySetToClassesRequestModel: AddStudySetToClassesRequestModel,
    ): Flow<Resources<Unit>> {
        return flow {
            emit(Resources.Loading())
            try {
                apiService.addStudySetToClasses(
                    addStudySetToClassesRequestDto = addStudySetToClassesRequestModel.toDto()
                )
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun getSearchResultStudySets(
        title: String,
        size: SearchResultSizeEnum,
        creatorType: SearchResultCreatorEnum?,
        page: Int,
        colorId: Int?,
        subjectId: Int?,
        isAIGenerated: Boolean?,
    ): Flow<PagingData<GetStudySetResponseModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                StudySetPagingSource(
                    studySetRemoteDataSource = studySetRemoteDataSource,
                    title = title,
                    size = size,
                    creatorType = creatorType,
                    colorId = colorId,
                    subjectId = subjectId,
                    isAIGenerated = isAIGenerated
                )
            }
        ).flow

    }

    override suspend fun getStudySetByCode(
        code: String,
    ): Flow<Resources<GetStudySetResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.getStudySetByLinkCode(code = code)
                emit(Resources.Success(response.toModel()))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun makeCopyStudySet(
        studySetId: String,
        newOwnerId: String,
    ): Flow<Resources<CreateStudySetResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val request =
                    MakeACopyStudySetRequestDto(studySetId = studySetId, newOwnerId = newOwnerId)
                val response = apiService.duplicateStudySet(request = request)
                emit(Resources.Success(response.toModel()))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun getTop5Subject(
    ): Flow<Resources<List<GetTop5SubjectResponseModel>>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.getTop5Subject()
                emit(Resources.Success(response.map { it.toModel() }))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun getStudySetBySubjectId(
        subjectId: Int,
        page: Int,
    ): Flow<PagingData<GetStudySetResponseModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                StudySetBySubjectPagingSource(
                    searchStudySetBySubjectRemoteDataSource = searchStudySetBySubjectRemoteDataSource,
                    subjectId = subjectId,
                )
            }
        ).flow

    }

    override suspend fun saveRecentAccessStudySet(
        saveRecentAccessStudySetRequestModel: SaveRecentAccessStudySetRequestModel,
    ): Flow<Resources<Unit>> {
        return flow {
            emit(Resources.Loading())
            try {
                apiService.saveRecentStudySet(
                    saveRecentAccessStudySetRequestDto = saveRecentAccessStudySetRequestModel.toDto()
                )
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun getRecentAccessStudySet(
        userId: String,
    ): Flow<Resources<List<GetStudySetResponseModel>>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.getRecentStudySet(userId = userId)
                emit(Resources.Success(response.map { it.toModel() }))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun createStudySetByAI(
        createStudySetByAIRequestModel: CreateStudySetByAIRequestModel,
    ): Flow<Resources<CreateStudySetResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.createStudySetByAI(
                    createStudySetRequestDto = createStudySetByAIRequestModel.toDto()
                )
                emit(Resources.Success(response.toModel()))
            } catch (e: IOException) {
                Timber.e("Error: ${e.message}")
                emit(Resources.Error(e.message ?: "Unknown error"))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun createWriteHintAI(
        createWriteHintAIRequestModel: CreateWriteHintAIRequestModel,
    ): Flow<Resources<CreateWriteHintAIResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response =
                    apiService.createWriteHintAI(createWriteHintAIModel = createWriteHintAIRequestModel.toDto())
                emit(Resources.Success(response.toModel()))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }
}