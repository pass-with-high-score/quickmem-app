package com.pwhs.quickmem.domain.datasource

import com.pwhs.quickmem.domain.model.classes.GetClassByOwnerResponseModel

interface ClassRemoteDataSource {
    suspend fun getSearchResultClasses(
        title: String,
        page: Int?,
    ): List<GetClassByOwnerResponseModel>
}