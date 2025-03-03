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
import com.pwhs.quickmem.domain.model.classes.UpdateClassRequestModel
import com.pwhs.quickmem.domain.model.classes.UpdateClassResponseModel
import com.pwhs.quickmem.domain.repository.ClassRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class ClassRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val classRemoteDataSource: ClassRemoteDataSource,
) : ClassRepository {
    override suspend fun createClass(
        createClassRequestModel: CreateClassRequestModel,
    ): Flow<Resources<CreateClassResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.createClass(
                    createClassRequestModel.toDto()
                )
                emit(Resources.Success(response.toModel()))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun getClassById(
        classId: String,
    ): Flow<Resources<GetClassDetailResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.getClassByID(classId)
                emit(Resources.Success(response.toModel()))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }

    }

    override suspend fun getClassByOwnerId(
        folderId: String?,
        studySetId: String?,
    ): Flow<Resources<List<GetClassByOwnerResponseModel>>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.getClassByOwnerID(
                    folderId = folderId,
                    studySetId = studySetId
                )
                emit(Resources.Success(response.map { it.toModel() }))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun updateClass(
        classId: String,
        updateClassRequestModel: UpdateClassRequestModel,
    ): Flow<Resources<UpdateClassResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.updateClass(
                    classId, updateClassRequestModel.toDto()
                )
                Timber.d("updateClass: $response")
                emit(Resources.Success(response.toModel()))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun deleteClass(classId: String): Flow<Resources<Unit>> {
        return flow {
            emit(Resources.Loading())
            try {
                apiService.deleteClass(classId)
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun getSearchResultClasses(
        title: String,
        page: Int?,
    ): Flow<PagingData<GetClassByOwnerResponseModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ClassPagingSource(
                    classRemoteDataSource,
                    title
                )
            }
        ).flow
    }

    override suspend fun getClassByCode(
        classCode: String,
    ): Flow<Resources<GetClassDetailResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.getClassByJoinToken(joinToken = classCode)
                emit(Resources.Success(response.toModel()))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun joinClass(
        joinClassRequestModel: JoinClassRequestModel,
    ): Flow<Resources<Unit>> {
        return flow {
            emit(Resources.Loading())
            try {
                apiService.joinClass(joinClassRequestModel.toDto())
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun exitClass(
        exitClassRequestModel: ExitClassRequestModel,
    ): Flow<Resources<Unit>> {
        return flow {
            emit(Resources.Loading())
            try {
                apiService.exitClass(exitClassRequestModel.toDto())
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun removeMembers(
        removeMembersRequestModel: RemoveMembersRequestModel,
    ): Flow<Resources<Unit>> {
        return flow {
            emit(Resources.Loading())
            try {
                apiService.removeMembers(removeMembersRequestModel.toDto())
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun deleteStudySetInClass(
        deleteStudySetsRequestModel: DeleteStudySetsRequestModel,
    ): Flow<Resources<Unit>> {
        return flow {
            emit(Resources.Loading())
            try {
                apiService.deleteStudySetInClass(deleteStudySetsRequestModel.toDto())
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun deleteFolderInClass(
        deleteFolderRequestModel: DeleteFolderRequestModel,
    ): Flow<Resources<Unit>> {
        return flow {
            emit(Resources.Loading())
            try {
                apiService.deleteFolderInClass(deleteFolderRequestModel.toDto())
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun saveRecentAccessClass(
        id: String
    ): Flow<Resources<Unit>> {
        return flow {
            emit(Resources.Loading())
            try {
                apiService.saveRecentClass(id = id)
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun getRecentAccessClass(
    ): Flow<Resources<List<GetClassByOwnerResponseModel>>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.getRecentClass()
                emit(Resources.Success(response.map { it.toModel() }))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun inviteToClass(
        inviteToClassRequestModel: InviteToClassRequestModel,
    ): Flow<Resources<InviteToClassResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.inviteUserToClass(
                    inviteToClassRequestModel.toDto()
                )
                emit(Resources.Success(response.toModel()))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }
}