package com.pwhs.quickmem.domain.repository

import android.net.Uri
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.upload.UploadImageResponseModel
import kotlinx.coroutines.flow.Flow

interface UploadImageRepository {
    suspend fun uploadImage(
        token: String,
        imageUri: Uri,
    ): Flow<Resources<UploadImageResponseModel>>

    suspend fun removeImage(
        token: String,
        imageURL: String,
    ): Flow<Resources<Unit>>

    suspend fun uploadUserAvatar(
        token: String,
        imageUri: Uri,
        userId: String,
    ): Flow<Resources<UploadImageResponseModel>>
}