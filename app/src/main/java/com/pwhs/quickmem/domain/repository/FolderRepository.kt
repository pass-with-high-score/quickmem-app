package com.pwhs.quickmem.domain.repository

import androidx.paging.PagingData
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.folder.AddFolderToClassRequestModel
import com.pwhs.quickmem.domain.model.folder.CreateFolderRequestModel
import com.pwhs.quickmem.domain.model.folder.CreateFolderResponseModel
import com.pwhs.quickmem.domain.model.folder.GetFolderResponseModel
import com.pwhs.quickmem.domain.model.folder.SaveRecentAccessFolderRequestModel
import com.pwhs.quickmem.domain.model.folder.UpdateFolderRequestModel
import com.pwhs.quickmem.domain.model.folder.UpdateFolderResponseModel
import kotlinx.coroutines.flow.Flow

interface FolderRepository {
    suspend fun createFolder(
        token: String,
        createFolderRequestModel: CreateFolderRequestModel,
    ): Flow<Resources<CreateFolderResponseModel>>

    suspend fun getFolderById(
        token: String,
        folderId: String,
    ): Flow<Resources<GetFolderResponseModel>>

    suspend fun updateFolder(
        token: String,
        folderId: String,
        updateFolderRequestModel: UpdateFolderRequestModel,
    ): Flow<Resources<UpdateFolderResponseModel>>

    suspend fun getFoldersByUserId(
        token: String,
        userId: String,
        classId: String?,
        studySetId: String?,
    ): Flow<Resources<List<GetFolderResponseModel>>>

    suspend fun deleteFolder(
        token: String,
        folderId: String,
    ): Flow<Resources<Unit>>

    suspend fun addFolderToClass(
        token: String,
        addFolderToClassRequestModel: AddFolderToClassRequestModel,
    ): Flow<Resources<Unit>>

    suspend fun getSearchResultFolders(
        token: String,
        title: String,
        page: Int?,
    ): Flow<PagingData<GetFolderResponseModel>>

    suspend fun getFolderByLinkCode(
        token: String,
        code: String,
    ): Flow<Resources<GetFolderResponseModel>>

    suspend fun saveRecentAccessFolder(
        token: String,
        saveRecentAccessFolderRequestModel: SaveRecentAccessFolderRequestModel,
    ): Flow<Resources<Unit>>

    suspend fun getRecentAccessFolders(
        token: String,
        userId: String,
    ): Flow<Resources<List<GetFolderResponseModel>>>

    suspend fun resetProgress(
        token: String,
        folderId: String,
        resetType: String,
    ): Flow<Resources<Unit>>
}