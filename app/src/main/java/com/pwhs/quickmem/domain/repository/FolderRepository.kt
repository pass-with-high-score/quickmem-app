package com.pwhs.quickmem.domain.repository

import androidx.paging.PagingData
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.folder.CreateFolderRequestModel
import com.pwhs.quickmem.domain.model.folder.CreateFolderResponseModel
import com.pwhs.quickmem.domain.model.folder.GetFolderResponseModel
import com.pwhs.quickmem.domain.model.folder.SaveRecentAccessFolderRequestModel
import com.pwhs.quickmem.domain.model.folder.UpdateFolderRequestModel
import com.pwhs.quickmem.domain.model.folder.UpdateFolderResponseModel
import kotlinx.coroutines.flow.Flow

interface FolderRepository {
    suspend fun createFolder(
        createFolderRequestModel: CreateFolderRequestModel,
    ): Flow<Resources<CreateFolderResponseModel>>

    suspend fun getFolderById(
        folderId: String,
    ): Flow<Resources<GetFolderResponseModel>>

    suspend fun updateFolder(
        folderId: String, updateFolderRequestModel: UpdateFolderRequestModel,
    ): Flow<Resources<UpdateFolderResponseModel>>

    suspend fun getFoldersByUserId(
        classId: String?, studySetId: String?,
    ): Flow<Resources<List<GetFolderResponseModel>>>

    suspend fun deleteFolder(
        folderId: String,
    ): Flow<Resources<Unit>>

    suspend fun getSearchResultFolders(
        title: String, page: Int?,
    ): Flow<PagingData<GetFolderResponseModel>>

    suspend fun getFolderByLinkCode(
        code: String,
    ): Flow<Resources<GetFolderResponseModel>>

    suspend fun saveRecentAccessFolder(
        saveRecentAccessFolderRequestModel: SaveRecentAccessFolderRequestModel,
    ): Flow<Resources<Unit>>

    suspend fun getRecentAccessFolders(): Flow<Resources<List<GetFolderResponseModel>>>

    suspend fun resetProgress(
        folderId: String, resetType: String,
    ): Flow<Resources<Unit>>
}