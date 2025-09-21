package com.pwhs.quickmem.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.data.mapper.auth.toDto
import com.pwhs.quickmem.data.mapper.auth.toModel
import com.pwhs.quickmem.data.mapper.user.toDto
import com.pwhs.quickmem.data.mapper.user.toModel
import com.pwhs.quickmem.data.paging.UserPagingSource
import com.pwhs.quickmem.data.remote.ApiService
import com.pwhs.quickmem.domain.datasource.UserRemoteDataResource
import com.pwhs.quickmem.domain.model.auth.AuthResponseModel
import com.pwhs.quickmem.domain.model.auth.AuthSocialGoogleRequestModel
import com.pwhs.quickmem.domain.model.auth.ChangePasswordRequestModel
import com.pwhs.quickmem.domain.model.auth.ChangePasswordResponseModel
import com.pwhs.quickmem.domain.model.auth.GetUserProfileResponseModel
import com.pwhs.quickmem.domain.model.auth.LoginRequestModel
import com.pwhs.quickmem.domain.model.auth.OtpResponseModel
import com.pwhs.quickmem.domain.model.auth.ResendEmailRequestModel
import com.pwhs.quickmem.domain.model.auth.ResetPasswordRequestModel
import com.pwhs.quickmem.domain.model.auth.ResetPasswordResponseModel
import com.pwhs.quickmem.domain.model.auth.SendResetPasswordRequestModel
import com.pwhs.quickmem.domain.model.auth.SendResetPasswordResponseModel
import com.pwhs.quickmem.domain.model.auth.SignupRequestModel
import com.pwhs.quickmem.domain.model.auth.SignupResponseModel
import com.pwhs.quickmem.domain.model.auth.SignupSocialCredentialRequestModel
import com.pwhs.quickmem.domain.model.auth.UpdateAvatarRequestModel
import com.pwhs.quickmem.domain.model.auth.UpdateAvatarResponseModel
import com.pwhs.quickmem.domain.model.auth.UpdateEmailRequestModel
import com.pwhs.quickmem.domain.model.auth.UpdateEmailResponseModel
import com.pwhs.quickmem.domain.model.auth.UpdateFullNameRequestModel
import com.pwhs.quickmem.domain.model.auth.UpdateFullNameResponseModel
import com.pwhs.quickmem.domain.model.auth.UpdateUsernameRequestModel
import com.pwhs.quickmem.domain.model.auth.UpdateUsernameResponseModel
import com.pwhs.quickmem.domain.model.auth.VerifyEmailResponseModel
import com.pwhs.quickmem.domain.model.auth.VerifyPasswordRequestModel
import com.pwhs.quickmem.domain.model.auth.VerifyPasswordResponseModel
import com.pwhs.quickmem.domain.model.users.AvatarResponseModel
import com.pwhs.quickmem.domain.model.users.SearchUserResponseModel
import com.pwhs.quickmem.domain.model.users.UpdateCoinRequestModel
import com.pwhs.quickmem.domain.model.users.UpdateCoinResponseModel
import com.pwhs.quickmem.domain.model.users.UserDetailResponseModel
import com.pwhs.quickmem.domain.repository.AuthRepository
import com.pwhs.quickmem.utils.parseApiError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userRemoteDataResource: UserRemoteDataResource,
) : AuthRepository {

    override suspend fun login(loginRequestModel: LoginRequestModel): Flow<Resources<AuthResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.login(loginRequestModel.toDto())
                emit(Resources.Success(response.toModel()))
            } catch (e: HttpException) {
                val apiError = e.parseApiError()
                if (apiError != null) {
                    emit(Resources.Error(message = apiError.message, status = apiError.statusCode))
                } else {
                    emit(Resources.Error(e.toString()))
                }
            } catch (e: Exception) {
                Timber.e(e.toString())
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun signup(signUpRequestModel: SignupRequestModel): Flow<Resources<SignupResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.signUp(signUpRequestModel.toDto())
                emit(Resources.Success(response.toModel()))
            } catch (e: HttpException) {
                val apiError = e.parseApiError()
                if (apiError != null) {
                    emit(Resources.Error(message = apiError.message, status = apiError.statusCode))
                } else {
                    emit(Resources.Error(e.toString()))
                }
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }

        }
    }

    override suspend fun verifyEmail(
        verifyEmailResponseModel: VerifyEmailResponseModel,
    ): Flow<Resources<AuthResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.verifyEmail(verifyEmailResponseModel.toDto())
                emit(Resources.Success(response.toModel()))
            } catch (e: HttpException) {
                val apiError = e.parseApiError()
                if (apiError != null) {
                    emit(Resources.Error(message = apiError.message, status = apiError.statusCode))
                } else {
                    emit(Resources.Error(e.toString()))
                }
            } catch (e: Exception) {
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun resendOtp(
        resendEmailRequestModel: ResendEmailRequestModel,
    ): Flow<Resources<OtpResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.resendVerificationEmail(resendEmailRequestModel.toDto())
                emit(Resources.Success(response.toModel()))
            } catch (e: HttpException) {
                val apiError = e.parseApiError()
                if (apiError != null) {
                    emit(Resources.Error(message = apiError.message, status = apiError.statusCode))
                } else {
                    emit(Resources.Error(e.toString()))
                }
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun updateFullName(
        updateFullNameRequestModel: UpdateFullNameRequestModel,
    ): Flow<Resources<UpdateFullNameResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.updateFullName(
                    updateFullNameRequestModel.toDto()
                )
                emit(Resources.Success(response.toModel()))
            } catch (e: HttpException) {
                val apiError = e.parseApiError()
                if (apiError != null) {
                    emit(Resources.Error(message = apiError.message, status = apiError.statusCode))
                } else {
                    emit(Resources.Error(e.toString()))
                }
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun updateUsername(
        updateUsernameRequestModel: UpdateUsernameRequestModel,
    ): Flow<Resources<UpdateUsernameResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.updateUsername(
                    updateUsernameRequestModel.toDto()
                )
                emit(Resources.Success(response.toModel()))
            } catch (e: HttpException) {
                val apiError = e.parseApiError()
                if (apiError != null) {
                    emit(Resources.Error(message = apiError.message, status = apiError.statusCode))
                } else {
                    emit(Resources.Error(e.toString()))
                }
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }


    override suspend fun updateEmail(
        updateEmailRequestModel: UpdateEmailRequestModel,
    ): Flow<Resources<UpdateEmailResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.updateEmail(
                    updateEmailRequestModel.toDto()
                )
                emit(Resources.Success(response.toModel()))
            } catch (e: HttpException) {
                val apiError = e.parseApiError()
                if (apiError != null) {
                    emit(Resources.Error(message = apiError.message, status = apiError.statusCode))
                } else {
                    emit(Resources.Error(e.toString()))
                }
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun changePassword(
        changePasswordRequestModel: ChangePasswordRequestModel,
    ): Flow<Resources<ChangePasswordResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.changePassword(
                    changePasswordRequestModel.toDto()
                )
                emit(Resources.Success(response.toModel()))
            } catch (e: HttpException) {
                val apiError = e.parseApiError()
                if (apiError != null) {
                    emit(Resources.Error(message = apiError.message, status = apiError.statusCode))
                } else {
                    emit(Resources.Error(e.toString()))
                }
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun sendResetPassword(
        sendResetPasswordRequestModel: SendResetPasswordRequestModel,
    ): Flow<Resources<SendResetPasswordResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.sendResetPassword(sendResetPasswordRequestModel.toDto())
                emit(Resources.Success(response.toModel()))
            } catch (e: HttpException) {
                val apiError = e.parseApiError()
                if (apiError != null) {
                    emit(Resources.Error(message = apiError.message, status = apiError.statusCode))
                } else {
                    emit(Resources.Error(e.toString()))
                }
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun resetPassword(
        resetPasswordRequestModel: ResetPasswordRequestModel,
    ): Flow<Resources<ResetPasswordResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.resetPassword(resetPasswordRequestModel.toDto())
                emit(Resources.Success(response.toModel()))
            } catch (e: HttpException) {
                val apiError = e.parseApiError()
                if (apiError != null) {
                    emit(Resources.Error(message = apiError.message, status = apiError.statusCode))
                } else {
                    emit(Resources.Error(e.toString()))
                }
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun verifyPassword(
        verifyPasswordRequestModel: VerifyPasswordRequestModel,
    ): Flow<Resources<VerifyPasswordResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.verifyPassword(
                    verifyPasswordRequestModel.toDto()
                )
                emit(Resources.Success(response.toModel()))
            } catch (e: HttpException) {
                val apiError = e.parseApiError()
                if (apiError != null) {
                    emit(Resources.Error(message = apiError.message, status = apiError.statusCode))
                } else {
                    emit(Resources.Error(e.toString()))
                }
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun getUserDetail(
        userId: String,
    ): Flow<Resources<UserDetailResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.getUserDetail(
                    userId,
                )
                emit(Resources.Success(response.toModel()))
            } catch (e: HttpException) {
                val apiError = e.parseApiError()
                if (apiError != null) {
                    emit(Resources.Error(message = apiError.message, status = apiError.statusCode))
                } else {
                    emit(Resources.Error(e.toString()))
                }
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun getAvatar(): Flow<Resources<List<AvatarResponseModel>>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.getAvatars()
                emit(Resources.Success(response.map { it.toModel() }))
            } catch (e: HttpException) {
                val apiError = e.parseApiError()
                if (apiError != null) {
                    emit(Resources.Error(message = apiError.message, status = apiError.statusCode))
                } else {
                    emit(Resources.Error(e.toString()))
                }
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun updateAvatar(
        updateAvatarRequestModel: UpdateAvatarRequestModel,
    ): Flow<Resources<UpdateAvatarResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.updateAvatar(
                    updateAvatarRequestDto = updateAvatarRequestModel.toDto()
                )
                emit(Resources.Success(response.toModel()))
            } catch (e: HttpException) {
                val apiError = e.parseApiError()
                if (apiError != null) {
                    emit(Resources.Error(message = apiError.message, status = apiError.statusCode))
                } else {
                    emit(Resources.Error(e.toString()))
                }
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun searchUser(
        username: String,
        page: Int?,
    ): Flow<PagingData<SearchUserResponseModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                UserPagingSource(
                    userRemoteDataResource,
                    username
                )
            }
        ).flow

    }

    override suspend fun getUserProfile(
    ): Flow<Resources<GetUserProfileResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.getUserProfile()
                emit(Resources.Success(response.toModel()))
            } catch (e: HttpException) {
                val apiError = e.parseApiError()
                if (apiError != null) {
                    emit(Resources.Error(message = apiError.message, status = apiError.statusCode))
                } else {
                    emit(Resources.Error(e.toString()))
                }
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun updateCoin(
        updateCoinRequestModel: UpdateCoinRequestModel,
    ): Flow<Resources<UpdateCoinResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.updateCoin(
                    updateCoinRequestModel.toDto()
                )
                emit(Resources.Success(response.toModel()))
            } catch (e: HttpException) {
                val apiError = e.parseApiError()
                if (apiError != null) {
                    emit(Resources.Error(message = apiError.message, status = apiError.statusCode))
                } else {
                    emit(Resources.Error(e.toString()))
                }
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun loginWithGoogle(
        authSocialGoogleRequestModel: AuthSocialGoogleRequestModel,
    ): Flow<Resources<AuthResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response = apiService.loginWithGoogle(authSocialGoogleRequestModel.toDto())
                emit(Resources.Success(response.toModel()))
            } catch (e: HttpException) {
                val apiError = e.parseApiError()
                if (apiError != null) {
                    emit(Resources.Error(message = apiError.message, status = apiError.statusCode))
                } else {
                    emit(Resources.Error(e.toString()))
                }
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }

    override suspend fun signupWithGoogle(signupSocialCredentialRequestModel: SignupSocialCredentialRequestModel): Flow<Resources<AuthResponseModel>> {
        return flow {
            emit(Resources.Loading())
            try {
                val response =
                    apiService.signupWithGoogle(signupSocialCredentialRequestModel.toDto())
                emit(Resources.Success(response.toModel()))
            } catch (e: HttpException) {
                val apiError = e.parseApiError()
                if (apiError != null) {
                    emit(Resources.Error(message = apiError.message, status = apiError.statusCode))
                } else {
                    emit(Resources.Error(e.toString()))
                }
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resources.Error(e.toString()))
            }
        }
    }
}