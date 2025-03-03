package com.pwhs.quickmem.domain.repository

import android.net.Uri
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.upload.UploadImageResponseModel
import kotlinx.coroutines.flow.Flow

interface UploadImageRepository {
    suspend fun uploadImage(
        imageUri: Uri,
    ): Flow<Resources<UploadImageResponseModel>>

    suspend fun removeImage(
        imageURL: String,
    ): Flow<Resources<Unit>>

    suspend fun uploadUserAvatar(
        imageUri: Uri,
    ): Flow<Resources<UploadImageResponseModel>>
}