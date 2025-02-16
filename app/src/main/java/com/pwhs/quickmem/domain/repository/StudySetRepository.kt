package com.pwhs.quickmem.domain.repository

import androidx.paging.PagingData
import com.pwhs.quickmem.core.utils.Resources
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
import com.pwhs.quickmem.presentation.app.search_result.study_set.enums.SearchResultCreatorEnum
import com.pwhs.quickmem.presentation.app.search_result.study_set.enums.SearchResultSizeEnum
import kotlinx.coroutines.flow.Flow

interface StudySetRepository {
    suspend fun createStudySet(
        token: String,
        createStudySetRequestModel: CreateStudySetRequestModel,
    ): Flow<Resources<CreateStudySetResponseModel>>

    suspend fun getStudySetById(
        token: String,
        studySetId: String,
    ): Flow<Resources<GetStudySetResponseModel>>

    suspend fun getStudySetsByOwnerId(
        token: String,
        classId: String?,
        folderId: String?,
    ): Flow<Resources<List<GetStudySetResponseModel>>>

    suspend fun updateStudySet(
        token: String,
        studySetId: String,
        updateStudySetRequestModel: UpdateStudySetRequestModel,
    ): Flow<Resources<UpdateStudySetResponseModel>>

    suspend fun deleteStudySet(
        token: String,
        studySetId: String,
    ): Flow<Resources<Unit>>

    suspend fun resetProgress(
        token: String,
        studySetId: String,
        resetType: String,
    ): Flow<Resources<Unit>>

    suspend fun addStudySetToFolder(
        token: String,
        addStudySetToFolderRequestModel: AddStudySetToFolderRequestModel,
    ): Flow<Resources<Unit>>

    suspend fun addStudySetToClass(
        token: String,
        addStudySetToClassRequestModel: AddStudySetToClassRequestModel,
    ): Flow<Resources<Unit>>

    suspend fun addStudySetToFolders(
        token: String,
        addStudySetToFoldersRequestModel: AddStudySetToFoldersRequestModel,
    ): Flow<Resources<Unit>>

    suspend fun addStudySetToClasses(
        token: String,
        addStudySetToClassesRequestModel: AddStudySetToClassesRequestModel,
    ): Flow<Resources<Unit>>

    suspend fun getSearchResultStudySets(
        token: String,
        title: String,
        size: SearchResultSizeEnum,
        creatorType: SearchResultCreatorEnum?,
        page: Int,
        colorId: Int?,
        subjectId: Int?,
        isAIGenerated: Boolean?,
    ): Flow<PagingData<GetStudySetResponseModel>>

    suspend fun getStudySetByCode(
        token: String,
        code: String,
    ): Flow<Resources<GetStudySetResponseModel>>

    suspend fun makeCopyStudySet(
        token: String,
        studySetId: String,
        newOwnerId: String,
    ): Flow<Resources<CreateStudySetResponseModel>>

    suspend fun getTop5Subject(
        token: String,
    ): Flow<Resources<List<GetTop5SubjectResponseModel>>>

    suspend fun getStudySetBySubjectId(
        token: String,
        subjectId: Int,
        page: Int,
    ): Flow<PagingData<GetStudySetResponseModel>>

    suspend fun saveRecentAccessStudySet(
        token: String,
        saveRecentAccessStudySetRequestModel: SaveRecentAccessStudySetRequestModel,
    ): Flow<Resources<Unit>>

    suspend fun getRecentAccessStudySet(
        token: String,
        userId: String,
    ): Flow<Resources<List<GetStudySetResponseModel>>>

    suspend fun createStudySetByAI(
        token: String,
        createStudySetByAIRequestModel: CreateStudySetByAIRequestModel,
    ): Flow<Resources<CreateStudySetResponseModel>>

    suspend fun createWriteHintAI(
        token: String,
        createWriteHintAIRequestModel: CreateWriteHintAIRequestModel,
    ): Flow<Resources<CreateWriteHintAIResponseModel>>
}