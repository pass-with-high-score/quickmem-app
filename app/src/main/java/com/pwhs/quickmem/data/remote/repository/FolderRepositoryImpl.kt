package com.pwhs.quickmem.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.data.mapper.folder.toDto
import com.pwhs.quickmem.data.mapper.folder.toModel
import com.pwhs.quickmem.data.paging.FolderPagingSource
import com.pwhs.quickmem.data.remote.ApiService
import com.pwhs.quickmem.domain.datasource.FolderRemoteDataSource
import com.pwhs.quickmem.domain.model.folder.AddFolderToClassRequestModel
import com.pwhs.quickmem.domain.model.folder.CreateFolderRequestModel
import com.pwhs.quickmem.domain.model.folder.CreateFolderResponseModel
import com.pwhs.quickmem.domain.model.folder.GetFolderResponseModel
import com.pwhs.quickmem.domain.model.folder.SaveRecentAccessFolderRequestModel
import com.pwhs.quickmem.domain.model.folder.UpdateFolderRequestModel
import com.pwhs.quickmem.domain.model.folder.UpdateFolderResponseModel
import com.pwhs.quickmem.domain.repository.FolderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class FolderRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val folderRemoteDataSource: FolderRemoteDataSource,
) : FolderRepository {
    override suspend fun createFolder(
        token: String,
        createFolderRequestModel: CreateFolderRequestModel,
    ): Flow<Resources<CreateFolderResponseModel>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                val response = apiService.createFolder(
                    token = token,
                    createFolderRequestDto = createFolderRequestModel.toDto()
                )
                emit(Resources.Success(response.toModel()))
            } catch (e: Exception) {
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun getFolderById(
        token: String,
        folderId: String,
    ): Flow<Resources<GetFolderResponseModel>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                val response = apiService.getFolderById(token = token, id = folderId)
                emit(Resources.Success(response.toModel()))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun updateFolder(
        token: String,
        folderId: String,
        updateFolderRequestModel: UpdateFolderRequestModel,
    ): Flow<Resources<UpdateFolderResponseModel>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                val response =
                    apiService.updateFolder(
                        token = token,
                        id = folderId,
                        updateFolderRequestDto = updateFolderRequestModel.toDto()
                    )
                emit(Resources.Success(response.toModel()))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun getFoldersByUserId(
        token: String,
        userId: String,
        classId: String?,
        studySetId: String?,
    ): Flow<Resources<List<GetFolderResponseModel>>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                val response = apiService.getFoldersByOwnerId(
                    token = token,
                    ownerId = userId,
                    classId = classId,
                    studySetId = studySetId
                )
                emit(Resources.Success(response.map { it.toModel() }))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun deleteFolder(token: String, folderId: String): Flow<Resources<Unit>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                apiService.deleteFolder(token = token, id = folderId)
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun addFolderToClass(
        token: String,
        addFolderToClassRequestModel: AddFolderToClassRequestModel,
    ): Flow<Resources<Unit>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                apiService.addFolderToClass(
                    token = token,
                    addFolderToClassRequestDto = addFolderToClassRequestModel.toDto()
                )
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun getSearchResultFolders(
        token: String,
        title: String,
        page: Int?,
    ): Flow<PagingData<GetFolderResponseModel>> {
        if (token.isEmpty()) {
            return emptyFlow()
        }
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                FolderPagingSource(
                    folderRemoteDataSource = folderRemoteDataSource,
                    token = token,
                    title = title
                )
            }
        ).flow
    }

    override suspend fun getFolderByLinkCode(
        token: String,
        code: String,
    ): Flow<Resources<GetFolderResponseModel>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                val response = apiService.getFolderByLinkCode(token = token, code = code)
                emit(Resources.Success(response.toModel()))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun saveRecentAccessFolder(
        token: String,
        saveRecentAccessFolderRequestModel: SaveRecentAccessFolderRequestModel,
    ): Flow<Resources<Unit>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                apiService.saveRecentFolder(
                    token = token,
                    saveRecentAccessFolderRequestDto = saveRecentAccessFolderRequestModel.toDto()
                )
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun getRecentAccessFolders(
        token: String,
        userId: String,
    ): Flow<Resources<List<GetFolderResponseModel>>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                val response = apiService.getRecentFolder(token = token, userId = userId)
                emit(Resources.Success(response.map { it.toModel() }))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun resetProgress(
        token: String,
        folderId: String,
        resetType: String,
    ): Flow<Resources<Unit>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                apiService.resetProgressFolder(token = token, id = folderId, resetType = resetType)
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }
}