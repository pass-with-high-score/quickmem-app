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
        createClassRequestModel: CreateClassRequestModel,
    ): Flow<Resources<CreateClassResponseModel>>

    suspend fun getClassById(
        classId: String,
    ): Flow<Resources<GetClassDetailResponseModel>>

    suspend fun getClassByOwnerId(
        folderId: String?, studySetId: String?,
    ): Flow<Resources<List<GetClassByOwnerResponseModel>>>

    suspend fun updateClass(
        classId: String, updateClassRequestModel: UpdateClassRequestModel,
    ): Flow<Resources<UpdateClassResponseModel>>

    suspend fun deleteClass(
        classId: String,
    ): Flow<Resources<Unit>>

    suspend fun getSearchResultClasses(
        title: String, page: Int?,
    ): Flow<PagingData<GetClassByOwnerResponseModel>>

    suspend fun getClassByCode(
        classCode: String,
    ): Flow<Resources<GetClassDetailResponseModel>>

    suspend fun joinClass(
        joinClassRequestModel: JoinClassRequestModel,
    ): Flow<Resources<Unit>>

    suspend fun exitClass(
        exitClassRequestModel: ExitClassRequestModel,
    ): Flow<Resources<Unit>>

    suspend fun removeMembers(
        removeMembersRequestModel: RemoveMembersRequestModel,
    ): Flow<Resources<Unit>>

    suspend fun deleteStudySetInClass(
        deleteStudySetsRequestModel: DeleteStudySetsRequestModel,
    ): Flow<Resources<Unit>>

    suspend fun deleteFolderInClass(
        deleteFolderRequestModel: DeleteFolderRequestModel,
    ): Flow<Resources<Unit>>

    suspend fun saveRecentAccessClass(
        id: String
    ): Flow<Resources<Unit>>

    suspend fun getRecentAccessClass(

    ): Flow<Resources<List<GetClassByOwnerResponseModel>>>

    suspend fun inviteToClass(
        inviteToClassRequestModel: InviteToClassRequestModel,
    ): Flow<Resources<InviteToClassResponseModel>>
}