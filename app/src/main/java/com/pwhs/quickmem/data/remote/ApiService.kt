package com.pwhs.quickmem.data.remote

import com.pwhs.quickmem.data.dto.auth.AuthResponseDto
import com.pwhs.quickmem.data.dto.auth.AuthSocialGoogleRequestDto
import com.pwhs.quickmem.data.dto.auth.ChangePasswordRequestDto
import com.pwhs.quickmem.data.dto.auth.ChangePasswordResponseDto
import com.pwhs.quickmem.data.dto.auth.GetUserProfileResponseDto
import com.pwhs.quickmem.data.dto.auth.LoginRequestDto
import com.pwhs.quickmem.data.dto.auth.OtpResponseDto
import com.pwhs.quickmem.data.dto.auth.RefreshTokenRequestDto
import com.pwhs.quickmem.data.dto.auth.RefreshTokenResponseDto
import com.pwhs.quickmem.data.dto.auth.ResendEmailRequestDto
import com.pwhs.quickmem.data.dto.auth.ResetPasswordRequestDto
import com.pwhs.quickmem.data.dto.auth.ResetPasswordResponseDto
import com.pwhs.quickmem.data.dto.auth.SendResetPasswordRequestDto
import com.pwhs.quickmem.data.dto.auth.SendResetPasswordResponseDto
import com.pwhs.quickmem.data.dto.auth.SignupRequestDto
import com.pwhs.quickmem.data.dto.auth.SignupResponseDto
import com.pwhs.quickmem.data.dto.auth.SignupSocialCredentialRequestDto
import com.pwhs.quickmem.data.dto.auth.UpdateAvatarRequestDto
import com.pwhs.quickmem.data.dto.auth.UpdateAvatarResponseDto
import com.pwhs.quickmem.data.dto.auth.UpdateEmailRequestDto
import com.pwhs.quickmem.data.dto.auth.UpdateEmailResponseDto
import com.pwhs.quickmem.data.dto.auth.UpdateFullNameRequestDto
import com.pwhs.quickmem.data.dto.auth.UpdateFullNameResponseDto
import com.pwhs.quickmem.data.dto.auth.UpdateUsernameRequestDto
import com.pwhs.quickmem.data.dto.auth.UpdateUsernameResponseDto
import com.pwhs.quickmem.data.dto.auth.VerifyEmailRequestDto
import com.pwhs.quickmem.data.dto.auth.VerifyPasswordRequestDto
import com.pwhs.quickmem.data.dto.auth.VerifyPasswordResponseDto
import com.pwhs.quickmem.data.dto.flashcard.BufferResponseDto
import com.pwhs.quickmem.data.dto.flashcard.CreateFlashCardDto
import com.pwhs.quickmem.data.dto.flashcard.EditFlashCardDto
import com.pwhs.quickmem.data.dto.flashcard.FlashCardResponseDto
import com.pwhs.quickmem.data.dto.flashcard.FlipFlashCardDto
import com.pwhs.quickmem.data.dto.flashcard.LanguageDto
import com.pwhs.quickmem.data.dto.flashcard.QuizStatusFlashCardDto
import com.pwhs.quickmem.data.dto.flashcard.RatingFlashCardDto
import com.pwhs.quickmem.data.dto.flashcard.TrueFalseStatusFlashCardDto
import com.pwhs.quickmem.data.dto.flashcard.UpdateFlashCardResponseDto
import com.pwhs.quickmem.data.dto.flashcard.VoiceDto
import com.pwhs.quickmem.data.dto.folder.CreateFolderRequestDto
import com.pwhs.quickmem.data.dto.folder.CreateFolderResponseDto
import com.pwhs.quickmem.data.dto.folder.GetFolderResponseDto
import com.pwhs.quickmem.data.dto.folder.SaveRecentAccessFolderRequestDto
import com.pwhs.quickmem.data.dto.folder.UpdateFolderRequestDto
import com.pwhs.quickmem.data.dto.folder.UpdateFolderResponseDto
import com.pwhs.quickmem.data.dto.notification.GetNotificationResponseDto
import com.pwhs.quickmem.data.dto.notification.MarkNotificationReadRequestDto
import com.pwhs.quickmem.data.dto.notification.DeviceTokenRequestDto
import com.pwhs.quickmem.data.dto.flashcard.WriteStatusFlashCardDto
import com.pwhs.quickmem.data.dto.pixabay.SearchImageResponseDto
import com.pwhs.quickmem.data.dto.report.CreateReportRequestDto
import com.pwhs.quickmem.data.dto.streak.GetStreakDto
import com.pwhs.quickmem.data.dto.streak.GetTopStreakResponseDto
import com.pwhs.quickmem.data.dto.streak.StreakDto
import com.pwhs.quickmem.data.dto.study_set.AddStudySetToFolderRequestDto
import com.pwhs.quickmem.data.dto.study_set.AddStudySetToFoldersRequestDto
import com.pwhs.quickmem.data.dto.study_set.CreateStudySetByAIRequestDto
import com.pwhs.quickmem.data.dto.study_set.CreateStudySetRequestDto
import com.pwhs.quickmem.data.dto.study_set.CreateStudySetResponseDto
import com.pwhs.quickmem.data.dto.study_set.CreateWriteHintAIRequestDto
import com.pwhs.quickmem.data.dto.study_set.CreateWriteHintAIResponseDto
import com.pwhs.quickmem.data.dto.study_set.GetStudySetResponseDto
import com.pwhs.quickmem.data.dto.study_set.MakeACopyStudySetRequestDto
import com.pwhs.quickmem.data.dto.study_set.SaveRecentAccessStudySetRequestDto
import com.pwhs.quickmem.data.dto.study_set.UpdateStudySetRequestDto
import com.pwhs.quickmem.data.dto.study_set.UpdateStudySetResponseDto
import com.pwhs.quickmem.data.dto.study_time.CreateStudyTimeDto
import com.pwhs.quickmem.data.dto.study_time.GetStudyTimeByStudySetResponseDto
import com.pwhs.quickmem.data.dto.study_time.GetStudyTimeByUserResponseDto
import com.pwhs.quickmem.data.dto.subject.GetTop5SubjectResponseDto
import com.pwhs.quickmem.data.dto.upload.DeleteImageDto
import com.pwhs.quickmem.data.dto.upload.UploadImageResponseDto
import com.pwhs.quickmem.data.dto.user.AvatarResponseDto
import com.pwhs.quickmem.data.dto.user.SearchUserResponseDto
import com.pwhs.quickmem.data.dto.user.UpdateCoinRequestDto
import com.pwhs.quickmem.data.dto.user.UpdateCoinResponseDto
import com.pwhs.quickmem.data.dto.user.UserDetailResponseDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // Auth
    @POST("auth/refresh-token")
    suspend fun refreshToken(@Body refreshTokenRequestDto: RefreshTokenRequestDto): Response<RefreshTokenResponseDto>

    @POST("auth/signup")
    suspend fun signUp(@Body signupRequestDto: SignupRequestDto): SignupResponseDto

    @POST("auth/login")
    suspend fun login(@Body loginRequestDto: LoginRequestDto): AuthResponseDto

    @POST("auth/verify-otp")
    suspend fun verifyEmail(@Body verifyEmailRequestDto: VerifyEmailRequestDto): AuthResponseDto

    @POST("auth/resend-verification-email")
    suspend fun resendVerificationEmail(@Body resendEmailRequestDto: ResendEmailRequestDto): OtpResponseDto

    @PATCH("auth/user/fullname")
    suspend fun updateFullName(
        @Body updateFullNameRequestDto: UpdateFullNameRequestDto,
    ): UpdateFullNameResponseDto

    @PATCH("auth/user/username")
    suspend fun updateUsername(
        @Body updateUsernameRequestDto: UpdateUsernameRequestDto,
    ): UpdateUsernameResponseDto

    @PATCH("auth/user/email")
    suspend fun updateEmail(
        @Body updateEmailRequestDto: UpdateEmailRequestDto,
    ): UpdateEmailResponseDto

    @PATCH("/auth/user/password")
    suspend fun changePassword(
        @Body changePasswordRequestDto: ChangePasswordRequestDto,
    ): ChangePasswordResponseDto

    @POST("auth/send-reset-password")
    suspend fun sendResetPassword(
        @Body sendResetPasswordRequestDto: SendResetPasswordRequestDto,
    ): SendResetPasswordResponseDto

    @POST("auth/reset-password")
    suspend fun resetPassword(
        @Body resetPasswordRequestDto: ResetPasswordRequestDto,
    ): ResetPasswordResponseDto

    @POST("auth/verify-password")
    suspend fun verifyPassword(
        @Body verifyPasswordRequestDto: VerifyPasswordRequestDto,
    ): VerifyPasswordResponseDto

    @GET("auth/me/{id}")
    suspend fun getUserDetail(
        @Path("id") userId: String,
    ): UserDetailResponseDto

    @GET("auth/profile")
    suspend fun getUserProfile(): GetUserProfileResponseDto

    @PATCH("auth/user/avatar")
    suspend fun updateAvatar(
        @Body updateAvatarRequestDto: UpdateAvatarRequestDto,
    ): UpdateAvatarResponseDto

    @GET("auth/user/search")
    suspend fun searchUser(
        @Query("username") username: String,
        @Query("page") page: Int?,
    ): List<SearchUserResponseDto>

    @POST("auth/coin")
    suspend fun updateCoin(
        @Body request: UpdateCoinRequestDto,
    ): UpdateCoinResponseDto

    @POST("auth/signup/google")
    suspend fun signupWithGoogle(
        @Body socialCredentialRequestDto: SignupSocialCredentialRequestDto
    ): AuthResponseDto

    @POST("auth/login/google")
    suspend fun loginWithGoogle(
        @Body authSocialGoogleRequestDto: AuthSocialGoogleRequestDto
    ): AuthResponseDto

    // Upload
    @Multipart
    @POST("upload")
    suspend fun uploadImage(
        @Part flashcard: MultipartBody.Part,
    ): UploadImageResponseDto

    @POST("upload/delete")
    suspend fun deleteImage(
        @Body deleteImageDto: DeleteImageDto,
    )

    @GET("auth/avatars")
    suspend fun getAvatars(): List<AvatarResponseDto>

    @Multipart
    @POST("upload/avatar")
    suspend fun uploadUserAvatar(@Part avatar: MultipartBody.Part): UploadImageResponseDto

    // Study Set
    @POST("study-set")
    suspend fun createStudySet(
        @Body createStudySetRequestDto: CreateStudySetRequestDto,
    ): CreateStudySetResponseDto

    @GET("study-set/{id}")
    suspend fun getStudySetById(@Path("id") id: String): GetStudySetResponseDto

    @GET("study-set/owner")
    suspend fun getStudySetsByOwnerId(
        @Query("folderId") folderId: String? = null,
    ): List<GetStudySetResponseDto>

    @PATCH("study-set/{id}")
    suspend fun updateStudySet(
        @Path("id") id: String,
        @Body updateStudySetRequestDto: UpdateStudySetRequestDto,
    ): UpdateStudySetResponseDto

    @PATCH("study-set/{id}/reset-progress")
    suspend fun resetStudySetProgress(
        @Path("id") id: String,
        @Query("resetType") resetType: String,
    )

    @DELETE("study-set/{id}")
    suspend fun deleteStudySet(@Path("id") id: String)

    @POST("study-set/folders")
    suspend fun addStudySetToFolders(
        @Body addStudySetToFoldersRequestDto: AddStudySetToFoldersRequestDto,
    )

    @GET("study-set/search")
    suspend fun searchStudySet(
        @Query("title") title: String,
        @Query("size") size: String,
        @Query("page") page: Int,
        @Query("colorId") colorId: Int?,
        @Query("subjectId") subjectId: Int?,
        @Query("isAIGenerated") isAIGenerated: Boolean?,
    ): List<GetStudySetResponseDto>

    @GET("study-set/link/{code}")
    suspend fun getStudySetByLinkCode(
        @Path("code") code: String,
    ): GetStudySetResponseDto

    @POST("study-set/recent")
    suspend fun saveRecentStudySet(
        @Body saveRecentAccessStudySetRequestDto: SaveRecentAccessStudySetRequestDto,
    )

    @GET("study-set/recent")
    suspend fun getRecentStudySet(): List<GetStudySetResponseDto>

    @POST("study-set/ai")
    suspend fun createStudySetByAI(
        @Body createStudySetRequestDto: CreateStudySetByAIRequestDto,
    ): CreateStudySetResponseDto

    @POST("study-set/ai/write-hint")
    suspend fun createWriteHintAI(
        @Body createWriteHintAIModel: CreateWriteHintAIRequestDto,
    ): CreateWriteHintAIResponseDto

    // subject
    @GET("study-set/top-subject")
    suspend fun getTop5Subject(): List<GetTop5SubjectResponseDto>

    @GET("study-set/subject/{subjectId}")
    suspend fun getStudySetBySubjectId(
        @Path("subjectId") subjectId: Int,
        @Query("page") page: Int,
    ): List<GetStudySetResponseDto>

    // Flashcard
    @POST("study-set/duplicate")
    suspend fun duplicateStudySet(@Body request: MakeACopyStudySetRequestDto): CreateStudySetResponseDto

    @GET("/flashcard/study-set/{id}")
    suspend fun getFlashCardsByStudySetId(
        @Path("id") id: String,
        @Query("learnMode") learnMode: String,
        @Query("isGetAll") isGetAll: Boolean,
        @Query("isSwapped") isSwapped: Boolean? = null,
        @Query("isRandom") isRandom: Boolean? = null,
    ): List<FlashCardResponseDto>

    @POST("flashcard")
    suspend fun createFlashCard(@Body createFlashCardDto: CreateFlashCardDto): FlashCardResponseDto

    @PUT("flashcard/{id}")
    suspend fun updateFlashCard(
        @Path("id") id: String,
        @Body editFlashCardDto: EditFlashCardDto,
    ): FlashCardResponseDto

    @DELETE("flashcard/{id}")
    suspend fun deleteFlashCard(@Path("id") id: String)

    @PATCH("flashcard/{id}/flip-status")
    suspend fun updateFlipFlashCard(
        @Path("id") id: String,
        @Body flipFlashCardDto: FlipFlashCardDto,
    ): UpdateFlashCardResponseDto

    @PATCH("flashcard/{id}/rating")
    suspend fun updateRatingFlashCard(
        @Path("id") id: String,
        @Body ratingFlashCardDto: RatingFlashCardDto,
    ): UpdateFlashCardResponseDto

    @PATCH("flashcard/{id}/quiz-status")
    suspend fun updateQuizStatus(
        @Path("id") id: String,
        @Body quizStatusDto: QuizStatusFlashCardDto,
    ): UpdateFlashCardResponseDto

    @PATCH("flashcard/{id}/true-false-status")
    suspend fun updateTrueFalseStatus(
        @Path("id") id: String,
        @Body trueFalseStatusDto: TrueFalseStatusFlashCardDto,
    ): UpdateFlashCardResponseDto

    @PATCH("flashcard/{id}/write-status")
    suspend fun updateWriteStatus(
        @Path("id") id: String,
        @Body writeStatusDto: WriteStatusFlashCardDto,
    ): UpdateFlashCardResponseDto

    @GET("flashcard/folder/{id}")
    suspend fun getFlashCardsByFolderId(
        @Path("id") id: String,
        @Query("learnMode") learnMode: String,
        @Query("isGetAll") isGetAll: Boolean,
        @Query("isSwapped") isSwapped: Boolean? = null,
        @Query("isRandom") isRandom: Boolean? = null,
    ): List<FlashCardResponseDto>

    @GET("flashcard/languages")
    suspend fun getLanguages(): List<LanguageDto>

    @GET("flashcard/voices/{languageCode}")
    suspend fun getVoices(@Path("languageCode") languageCode: String): List<VoiceDto>

    @GET("flashcard/speech")
    suspend fun getSpeech(
        @Query("input") input: String,
        @Query("voiceCode") voiceCode: String,
    ): BufferResponseDto

    // Folder
    @POST("folder")
    suspend fun createFolder(
        @Body createFolderRequestDto: CreateFolderRequestDto,
    ): CreateFolderResponseDto

    @GET("folder/{id}")
    suspend fun getFolderById(@Path("id") id: String): GetFolderResponseDto

    @PUT("folder/{id}")
    suspend fun updateFolder(
        @Path("id") id: String,
        @Body updateFolderRequestDto: UpdateFolderRequestDto,
    ): UpdateFolderResponseDto

    @GET("folder/owner")
    suspend fun getFoldersByOwnerId(
        @Query("studySetId") studySetId: String? = null,
    ): List<GetFolderResponseDto>

    @DELETE("folder/{id}")
    suspend fun deleteFolder(@Path("id") id: String)

    @POST("folder/study-sets")
    suspend fun addStudySetToFolder(@Body addStudySetToFolderRequestDto: AddStudySetToFolderRequestDto)

    @GET("folder/search")
    suspend fun searchFolder(
        @Query("title") title: String,
        @Query("page") page: Int?,
    ): List<GetFolderResponseDto>

    @GET("folder/link/{code}")
    suspend fun getFolderByLinkCode(@Path("code") code: String): GetFolderResponseDto

    @POST("folder/recent")
    suspend fun saveRecentFolder(@Body saveRecentAccessFolderRequestDto: SaveRecentAccessFolderRequestDto)

    @GET("folder/recent")
    suspend fun getRecentFolder(): List<GetFolderResponseDto>

    @PATCH("folder/{id}/reset-progress")
    suspend fun resetProgressFolder(
        @Path("id") id: String,
        @Query("resetType") resetType: String,
    )

    // Streak
    @GET("streak")
    suspend fun getStreaksByUserId(): GetStreakDto

    @POST("streak")
    suspend fun updateStreak(): StreakDto

    @GET("streak/top")
    suspend fun getTopStreaks(@Query("limit") limit: Int?): List<GetTopStreakResponseDto>

    // Notification
    @POST("notifications/register")
    suspend fun sendDeviceToken(@Body tokenRequest: DeviceTokenRequestDto): Response<Unit>

    @GET("notifications/user")
    suspend fun getNotificationsByUserId(): List<GetNotificationResponseDto>

    @PATCH("notifications/{id}/read")
    suspend fun markNotificationAsRead(
        @Path("id") notificationId: String,
        @Body requestDto: MarkNotificationReadRequestDto,
    )

    @DELETE("notifications/{id}")
    suspend fun deleteNotification(@Path("id") notificationId: String)

    @POST("notifications/clear")
    suspend fun clearAllNotifications()

    // Study Time
    @GET("study-time/study-set/{studySetId}")
    suspend fun getStudyTimeByStudySet(@Path("studySetId") studySetId: String): GetStudyTimeByStudySetResponseDto

    @GET("study-time/user/total")
    suspend fun getStudyTimeByUser(): GetStudyTimeByUserResponseDto

    @POST("study-time")
    suspend fun createStudyTime(@Body createStudyTimeDto: CreateStudyTimeDto)

    // Report
    @POST("report")
    suspend fun createReport(@Body createReportRequestDto: CreateReportRequestDto)

    // PixaBay
    @GET("pixabay/search")
    suspend fun searchImage(@Query("query") query: String): SearchImageResponseDto

}