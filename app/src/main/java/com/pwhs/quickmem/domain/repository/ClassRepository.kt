package com.pwhs.quickmem.domain.repository

import androidx.paging.PagingData
import com.pwhs.quickmem.core.utils.Resources
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
import kotlinx.coroutines.flow.Flow

interface ClassRepository {
    suspend fun createClass(
        token: String,
        createClassRequestModel: CreateClassRequestModel,
    ): Flow<Resources<CreateClassResponseModel>>

    suspend fun getClassById(
        token: String,
        classId: String,
    ): Flow<Resources<GetClassDetailResponseModel>>

    suspend fun getClassByOwnerId(
        token: String,
        folderId: String?,
        studySetId: String?,
    ): Flow<Resources<List<GetClassByOwnerResponseModel>>>

    suspend fun updateClass(
        token: String,
        classId: String,
        updateClassRequestModel: UpdateClassRequestModel,
    ): Flow<Resources<UpdateClassResponseModel>>

    suspend fun deleteClass(
        token: String,
        classId: String,
    ): Flow<Resources<Unit>>

    suspend fun getSearchResultClasses(
        token: String,
        title: String,
        page: Int?,
    ): Flow<PagingData<GetClassByOwnerResponseModel>>

    suspend fun getClassByCode(
        token: String,
        classCode: String,
    ): Flow<Resources<GetClassDetailResponseModel>>

    suspend fun joinClass(
        token: String,
        joinClassRequestModel: JoinClassRequestModel,
    ): Flow<Resources<Unit>>

    suspend fun exitClass(
        token: String,
        exitClassRequestModel: ExitClassRequestModel,
    ): Flow<Resources<Unit>>

    suspend fun removeMembers(
        token: String,
        removeMembersRequestModel: RemoveMembersRequestModel,
    ): Flow<Resources<Unit>>

    suspend fun deleteStudySetInClass(
        token: String,
        deleteStudySetsRequestModel: DeleteStudySetsRequestModel,
    ): Flow<Resources<Unit>>

    suspend fun deleteFolderInClass(
        token: String,
        deleteFolderRequestModel: DeleteFolderRequestModel,
    ): Flow<Resources<Unit>>

    suspend fun saveRecentAccessClass(
        token: String,
        id: String
    ): Flow<Resources<Unit>>

    suspend fun getRecentAccessClass(
        token: String,
    ): Flow<Resources<List<GetClassByOwnerResponseModel>>>

    suspend fun inviteToClass(
        token: String,
        inviteToClassRequestModel: InviteToClassRequestModel,
    ): Flow<Resources<InviteToClassResponseModel>>
}