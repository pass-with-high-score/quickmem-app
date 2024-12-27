package com.pwhs.quickmem.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.data.mapper.classes.toDto
import com.pwhs.quickmem.data.mapper.classes.toModel
import com.pwhs.quickmem.data.paging.ClassPagingSource
import com.pwhs.quickmem.data.remote.ApiService
import com.pwhs.quickmem.domain.datasource.ClassRemoteDataSource
import com.pwhs.quickmem.domain.model.classes.CreateClassRequestModel
import com.pwhs.quickmem.domain.model.classes.CreateClassResponseModel
import com.pwhs.quickmem.domain.model.classes.DeleteFolderRequestModel
import com.pwhs.quickmem.domain.model.classes.DeleteStudySetsRequestModel
import com.pwhs.quickmem.domain.model.classes.ExitClassRequestModel
import com.pwhs.quickmem.domain.model.classes.GetClassByOwnerResponseModel
import com.pwhs.quickmem.domain.model.classes.GetClassDetailResponseModel
import com.pwhs.quickmem.domain.model.classes.InviteToClassRequestModel
import com.pwhs.quickmem.domain.model.classes.InviteToClassResponseModel
import com.pwhs.quickmem.domain.model.classes.JoinClassRequestModel
import com.pwhs.quickmem.domain.model.classes.RemoveMembersRequestModel
import com.pwhs.quickmem.domain.model.classes.SaveRecentAccessClassRequestModel
import com.pwhs.quickmem.domain.model.classes.UpdateClassRequestModel
import com.pwhs.quickmem.domain.model.classes.UpdateClassResponseModel
import com.pwhs.quickmem.domain.repository.ClassRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class ClassRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val classRemoteDataSource: ClassRemoteDataSource,
) : ClassRepository {
    override suspend fun createClass(
        token: String,
        createClassRequestModel: CreateClassRequestModel,
    ): Flow<Resources<CreateClassResponseModel>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                val response = apiService.createClass(
                    token, createClassRequestModel.toDto()
                )
                Timber.d("createClass: $response")
                emit(Resources.Success(response.toModel()))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun getClassById(
        token: String,
        classId: String,
    ): Flow<Resources<GetClassDetailResponseModel>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                val response = apiService.getClassByID(
                    token,
                    classId
                )
                Timber.d("getClassByIddddd: ${response.studySets?.firstOrNull()?.flashcardCount}")
                emit(Resources.Success(response.toModel()))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }

    }

    override suspend fun getClassByOwnerId(
        token: String,
        userId: String,
        folderId: String?,
        studySetId: String?,
    ): Flow<Resources<List<GetClassByOwnerResponseModel>>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                val response = apiService.getClassByOwnerID(token, userId, folderId, studySetId)
                Timber.d("listClass: $response")
                emit(Resources.Success(response.map { it.toModel() }))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun updateClass(
        token: String,
        classId: String,
        updateClassRequestModel: UpdateClassRequestModel,
    ): Flow<Resources<UpdateClassResponseModel>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                val response = apiService.updateClass(
                    token, classId, updateClassRequestModel.toDto()
                )
                Timber.d("updateClass: $response")
                emit(Resources.Success(response.toModel()))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun deleteClass(token: String, classId: String): Flow<Resources<Unit>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                apiService.deleteClass(
                    token, classId
                )
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun getSearchResultClasses(
        token: String,
        title: String,
        page: Int?,
    ): Flow<PagingData<GetClassByOwnerResponseModel>> {
        if (token.isEmpty()) {
            return emptyFlow()
        }
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ClassPagingSource(
                    classRemoteDataSource,
                    token,
                    title
                )
            }
        ).flow
    }

    override suspend fun getClassByCode(
        token: String,
        userId: String,
        classCode: String,
    ): Flow<Resources<GetClassDetailResponseModel>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                val response = apiService.getClassByJoinToken(
                    token = token,
                    userId = userId,
                    joinToken = classCode
                )
                emit(Resources.Success(response.toModel()))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun joinClass(
        token: String,
        joinClassRequestModel: JoinClassRequestModel,
    ): Flow<Resources<Unit>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                apiService.joinClass(token, joinClassRequestModel.toDto())
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun exitClass(
        token: String,
        exitClassRequestModel: ExitClassRequestModel,
    ): Flow<Resources<Unit>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                apiService.exitClass(token, exitClassRequestModel.toDto())
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun removeMembers(
        token: String,
        removeMembersRequestModel: RemoveMembersRequestModel,
    ): Flow<Resources<Unit>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                apiService.removeMembers(token, removeMembersRequestModel.toDto())
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun deleteStudySetInClass(
        token: String,
        deleteStudySetsRequestModel: DeleteStudySetsRequestModel,
    ): Flow<Resources<Unit>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                apiService.deleteStudySetInClass(token, deleteStudySetsRequestModel.toDto())
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun deleteFolderInClass(
        token: String,
        deleteFolderRequestModel: DeleteFolderRequestModel,
    ): Flow<Resources<Unit>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                apiService.deleteFolderInClass(token, deleteFolderRequestModel.toDto())
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun saveRecentAccessClass(
        token: String,
        saveRecentAccessClassRequestModel: SaveRecentAccessClassRequestModel,
    ): Flow<Resources<Unit>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                apiService.saveRecentClass(token, saveRecentAccessClassRequestModel.toDto())
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun getRecentAccessClass(
        token: String,
        userId: String,
    ): Flow<Resources<List<GetClassByOwnerResponseModel>>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                val response = apiService.getRecentClass(token, userId)
                emit(Resources.Success(response.map { it.toModel() }))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun inviteToClass(
        token: String,
        inviteToClassRequestModel: InviteToClassRequestModel,
    ): Flow<Resources<InviteToClassResponseModel>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                val response = apiService.inviteUserToClass(
                    token, inviteToClassRequestModel.toDto()
                )
                emit(Resources.Success(response.toModel()))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }
}