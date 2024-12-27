package com.pwhs.quickmem.data.remote.repository

import android.content.Context
import android.net.Uri
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.data.dto.upload.DeleteImageDto
import com.pwhs.quickmem.data.mapper.upload.toUploadImageResponseModel
import com.pwhs.quickmem.data.remote.ApiService
import com.pwhs.quickmem.domain.model.upload.UploadImageResponseModel
import com.pwhs.quickmem.domain.repository.UploadImageRepository
import com.pwhs.quickmem.util.RealPathUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class UploadImageRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val context: Context,
) : UploadImageRepository {
    override suspend fun uploadImage(
        token: String,
        imageUri: Uri,
    ): Flow<Resources<UploadImageResponseModel>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                val realPath = RealPathUtil.getRealPath(context = context, imageUri)
                val imageFile = realPath?.let { File(it) }

                if (imageFile != null) {
                    val requestUploadImageFile = MultipartBody.Part.createFormData(
                        name = "flashcard",
                        filename = imageFile.name,
                        body = imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    )
                    val response = apiService.uploadImage(token, requestUploadImageFile)
                    emit(Resources.Success(response.toUploadImageResponseModel()))
                }

            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.message ?: "An error occurred"))
            }
        }
    }

    override suspend fun removeImage(
        token: String,
        imageURL: String,
    ): Flow<Resources<Unit>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                apiService.deleteImage(token, DeleteImageDto(imageURL))
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.message ?: "An error occurred"))
            }
        }
    }

    override suspend fun uploadUserAvatar(
        token: String,
        imageUri: Uri,
        userId: String,
    ): Flow<Resources<UploadImageResponseModel>> {
        return flow {
            if (token.isEmpty()) {
                return@flow
            }
            emit(Resources.Loading())
            try {
                val realPath = RealPathUtil.getRealPath(context = context, imageUri)
                val imageFile = realPath?.let { File(it) }

                if (imageFile != null) {
                    val requestUploadImageFile = MultipartBody.Part.createFormData(
                        name = "avatar",
                        filename = imageFile.name,
                        body = imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    )
                    val response = apiService.uploadUserAvatar(
                        token = token,
                        avatar = requestUploadImageFile,
                        userId = userId
                    )
                    emit(Resources.Success(response.toUploadImageResponseModel()))
                }

            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.message ?: "An error occurred"))
            }
        }
    }
}