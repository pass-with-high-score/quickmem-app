package com.pwhs.quickmem.data.remote.repository

import android.content.Context
import android.net.Uri
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.data.dto.upload.DeleteImageDto
import com.pwhs.quickmem.data.mapper.upload.toUploadImageResponseModel
import com.pwhs.quickmem.data.remote.ApiService
import com.pwhs.quickmem.domain.model.upload.UploadImageResponseModel
import com.pwhs.quickmem.domain.repository.UploadImageRepository
import com.pwhs.quickmem.utils.RealPathUtil
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
    companion object {
        const val UPLOAD_FILE_NAME: String = "flashcard"
        const val UPLOAD_AVATAR_NAME: String = "avatar"
    }

    override suspend fun uploadImage(
        imageUri: Uri,
    ): Flow<Resources<UploadImageResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val realPath = RealPathUtil.getRealPath(context = context, imageUri)
                val imageFile = realPath?.let { File(it) }

                if (imageFile != null) {
                    val requestUploadImageFile = MultipartBody.Part.createFormData(
                        name = UPLOAD_FILE_NAME,
                        filename = imageFile.name,
                        body = imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    )
                    val response = apiService.uploadImage(requestUploadImageFile)
                    emit(Resources.Success(response.toUploadImageResponseModel()))
                }

            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.message ?: "An error occurred"))
            }
        }
    }

    override suspend fun removeImage(
        imageURL: String,
    ): Flow<Resources<Unit>> {
        return flow {
            emit(Resources.Loading())
            try {
                apiService.deleteImage(DeleteImageDto(imageURL))
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.message ?: "An error occurred"))
            }
        }
    }

    override suspend fun uploadUserAvatar(
        imageUri: Uri,
    ): Flow<Resources<UploadImageResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val realPath = RealPathUtil.getRealPath(context = context, imageUri)
                val imageFile = realPath?.let { File(it) }

                if (imageFile != null) {
                    val requestUploadImageFile = MultipartBody.Part.createFormData(
                        name = UPLOAD_AVATAR_NAME,
                        filename = imageFile.name,
                        body = imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    )
                    val response = apiService.uploadUserAvatar(
                        avatar = requestUploadImageFile,
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