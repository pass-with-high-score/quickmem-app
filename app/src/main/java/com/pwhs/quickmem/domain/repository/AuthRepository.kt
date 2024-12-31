package com.pwhs.quickmem.domain.repository

import androidx.paging.PagingData
import com.pwhs.quickmem.core.data.enums.AuthProvider
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.auth.AuthResponseModel
import com.pwhs.quickmem.domain.model.auth.AuthSocialGoogleRequestModel
import com.pwhs.quickmem.domain.model.auth.ChangePasswordRequestModel
import com.pwhs.quickmem.domain.model.auth.ChangePasswordResponseModel
import com.pwhs.quickmem.domain.model.auth.ChangeRoleRequestModel
import com.pwhs.quickmem.domain.model.auth.ChangeRoleResponseModel
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
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun checkEmailValidity(email: String): Flow<Resources<Boolean>>
    suspend fun login(loginRequestModel: LoginRequestModel): Flow<Resources<AuthResponseModel>>
    suspend fun signup(
        signUpRequestModel: SignupRequestModel,
    ): Flow<Resources<SignupResponseModel>>

    suspend fun verifyEmail(
        verifyEmailResponseModel: VerifyEmailResponseModel,
    ): Flow<Resources<AuthResponseModel>>

    suspend fun resendOtp(
        resendEmailRequestModel: ResendEmailRequestModel,
    ): Flow<Resources<OtpResponseModel>>

    suspend fun updateFullName(
        token: String,
        updateFullNameRequestModel: UpdateFullNameRequestModel,
    ): Flow<Resources<UpdateFullNameResponseModel>>

    suspend fun updateUsername(
        token: String,
        updateUsernameRequestModel: UpdateUsernameRequestModel,
    ): Flow<Resources<UpdateUsernameResponseModel>>

    suspend fun updateEmail(
        token: String,
        updateEmailRequestModel: UpdateEmailRequestModel,
    ): Flow<Resources<UpdateEmailResponseModel>>

    suspend fun changePassword(
        token: String,
        changePasswordRequestModel: ChangePasswordRequestModel,
    ): Flow<Resources<ChangePasswordResponseModel>>

    suspend fun sendResetPassword(
        sendResetPasswordRequestModel: SendResetPasswordRequestModel,
    ): Flow<Resources<SendResetPasswordResponseModel>>

    suspend fun resetPassword(
        resetPasswordRequestModel: ResetPasswordRequestModel,
    ): Flow<Resources<ResetPasswordResponseModel>>

    suspend fun verifyPassword(
        token: String,
        verifyPasswordRequestModel: VerifyPasswordRequestModel,
    ): Flow<Resources<VerifyPasswordResponseModel>>

    suspend fun getUserDetail(
        userId: String,
        token: String,
        isOwner: Boolean,
    ): Flow<Resources<UserDetailResponseModel>>

    suspend fun getAvatar(token: String): Flow<Resources<List<AvatarResponseModel>>>

    suspend fun updateAvatar(
        token: String,
        avatarId: String,
        updateAvatarRequestModel: UpdateAvatarRequestModel,
    ): Flow<Resources<UpdateAvatarResponseModel>>

    suspend fun searchUser(
        token: String,
        username: String,
        page: Int?,
    ): Flow<PagingData<SearchUserResponseModel>>

    suspend fun getUserProfile(
        token: String,
        userId: String,
    ): Flow<Resources<GetUserProfileResponseModel>>

    suspend fun changeRole(
        token: String,
        changeRoleRequestModel: ChangeRoleRequestModel,
    ): Flow<Resources<ChangeRoleResponseModel>>

    suspend fun updateCoin(
        token: String,
        updateCoinRequestModel: UpdateCoinRequestModel,
    ): Flow<Resources<UpdateCoinResponseModel>>

    suspend fun loginWithGoogle(
        authSocialGoogleRequestModel: AuthSocialGoogleRequestModel,
    ): Flow<Resources<AuthResponseModel>>

    suspend fun signupWithGoogle(
        signupSocialCredentialRequestModel: SignupSocialCredentialRequestModel,
    ): Flow<Resources<AuthResponseModel>>

    suspend fun loginWithFacebook(
        provider: AuthProvider,
        accessToken: String,
    ): Flow<Resources<AuthResponseModel>>

    suspend fun signupWithFacebook(
        provider: AuthProvider,
        accessToken: String,
    ): Flow<Resources<AuthResponseModel>>
}