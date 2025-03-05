package com.pwhs.quickmem.domain.repository

import androidx.paging.PagingData
import com.pwhs.quickmem.core.utils.Resources
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
import com.pwhs.quickmem.presentation.app.search_result.study_set.enums.SearchResultCreatorEnum
import com.pwhs.quickmem.presentation.app.search_result.study_set.enums.SearchResultSizeEnum
import kotlinx.coroutines.flow.Flow

interface StudySetRepository {
    suspend fun createStudySet(
        createStudySetRequestModel: CreateStudySetRequestModel,
    ): Flow<Resources<CreateStudySetResponseModel>>

    suspend fun getStudySetById(
        studySetId: String,
    ): Flow<Resources<GetStudySetResponseModel>>

    suspend fun getStudySetsByOwnerId(
        classId: String?,
        folderId: String?,
    ): Flow<Resources<List<GetStudySetResponseModel>>>

    suspend fun updateStudySet(
        studySetId: String,
        updateStudySetRequestModel: UpdateStudySetRequestModel,
    ): Flow<Resources<UpdateStudySetResponseModel>>

    suspend fun deleteStudySet(
        studySetId: String,
    ): Flow<Resources<Unit>>

    suspend fun resetProgress(
        studySetId: String,
        resetType: String,
    ): Flow<Resources<Unit>>

    suspend fun addStudySetToFolder(
        addStudySetToFolderRequestModel: AddStudySetToFolderRequestModel,
    ): Flow<Resources<Unit>>

    suspend fun addStudySetToFolders(
        addStudySetToFoldersRequestModel: AddStudySetToFoldersRequestModel,
    ): Flow<Resources<Unit>>

    suspend fun getSearchResultStudySets(
        title: String,
        size: SearchResultSizeEnum,
        creatorType: SearchResultCreatorEnum?,
        page: Int,
        colorId: Int?,
        subjectId: Int?,
        isAIGenerated: Boolean?,
    ): Flow<PagingData<GetStudySetResponseModel>>

    suspend fun getStudySetByCode(
        code: String,
    ): Flow<Resources<GetStudySetResponseModel>>

    suspend fun makeCopyStudySet(
        studySetId: String,
    ): Flow<Resources<CreateStudySetResponseModel>>

    suspend fun getTop5Subject(
    ): Flow<Resources<List<GetTop5SubjectResponseModel>>>

    suspend fun getStudySetBySubjectId(
        subjectId: Int,
        page: Int,
    ): Flow<PagingData<GetStudySetResponseModel>>

    suspend fun saveRecentAccessStudySet(
        saveRecentAccessStudySetRequestModel: SaveRecentAccessStudySetRequestModel,
    ): Flow<Resources<Unit>>

    suspend fun getRecentAccessStudySet(): Flow<Resources<List<GetStudySetResponseModel>>>

    suspend fun createStudySetByAI(
        createStudySetByAIRequestModel: CreateStudySetByAIRequestModel,
    ): Flow<Resources<CreateStudySetResponseModel>>

    suspend fun createWriteHintAI(
        createWriteHintAIRequestModel: CreateWriteHintAIRequestModel,
    ): Flow<Resources<CreateWriteHintAIResponseModel>>
}