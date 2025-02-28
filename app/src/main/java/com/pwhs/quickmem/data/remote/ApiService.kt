package com.pwhs.quickmem.data.remote

import com.pwhs.quickmem.data.dto.auth.AuthResponseDto
import com.pwhs.quickmem.data.dto.auth.AuthSocialGoogleRequestDto
import com.pwhs.quickmem.data.dto.auth.ChangePasswordRequestDto
import com.pwhs.quickmem.data.dto.auth.ChangePasswordResponseDto
import com.pwhs.quickmem.data.dto.auth.ChangeRoleRequestDto
import com.pwhs.quickmem.data.dto.auth.ChangeRoleResponseDto
import com.pwhs.quickmem.data.dto.auth.GetUserProfileResponseDto
import com.pwhs.quickmem.data.dto.auth.LoginRequestDto
import com.pwhs.quickmem.data.dto.auth.OtpResponseDto
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
import com.pwhs.quickmem.data.dto.classes.AddStudySetToClassesRequestDto
import com.pwhs.quickmem.data.dto.classes.CreateClassRequestDto
import com.pwhs.quickmem.data.dto.classes.CreateClassResponseDto
import com.pwhs.quickmem.data.dto.classes.DeleteFolderRequestDto
import com.pwhs.quickmem.data.dto.classes.DeleteStudySetsRequestDto
import com.pwhs.quickmem.data.dto.classes.ExitClassRequestDto
import com.pwhs.quickmem.data.dto.classes.GetClassByOwnerResponseDto
import com.pwhs.quickmem.data.dto.classes.GetClassDetailResponseDto
import com.pwhs.quickmem.data.dto.classes.InviteToClassRequestDto
import com.pwhs.quickmem.data.dto.classes.InviteToClassResponseDto
import com.pwhs.quickmem.data.dto.classes.JoinClassRequestDto
import com.pwhs.quickmem.data.dto.classes.RemoveMembersRequestDto
import com.pwhs.quickmem.data.dto.classes.SaveRecentAccessClassRequestDto
import com.pwhs.quickmem.data.dto.classes.UpdateClassRequestDto
import com.pwhs.quickmem.data.dto.classes.UpdateClassResponseDto
import com.pwhs.quickmem.data.dto.flashcard.BufferResponseDto
import com.pwhs.quickmem.data.dto.flashcard.CreateFlashCardDto
import com.pwhs.quickmem.data.dto.flashcard.EditFlashCardDto
import com.pwhs.quickmem.data.dto.flashcard.FlashCardResponseDto
import com.pwhs.quickmem.data.dto.flashcard.FlipFlashCardDto
import com.pwhs.quickmem.data.dto.flashcard.LanguageDto
import com.pwhs.quickmem.data.dto.flashcard.QuizStatusFlashCardDto
import com.pwhs.quickmem.data.dto.flashcard.RatingFlashCardDto
import com.pwhs.quickmem.data.dto.flashcard.ToggleStarredFlashCardDto
import com.pwhs.quickmem.data.dto.flashcard.TrueFalseStatusFlashCardDto
import com.pwhs.quickmem.data.dto.flashcard.UpdateFlashCardResponseDto
import com.pwhs.quickmem.data.dto.flashcard.VoiceDto
import com.pwhs.quickmem.data.dto.folder.AddFolderToClassRequestDto
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
import com.pwhs.quickmem.data.dto.streak.IncreaseStreakDto
import com.pwhs.quickmem.data.dto.streak.StreakDto
import com.pwhs.quickmem.data.dto.study_set.AddStudySetToClassRequestDto
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
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // Aut
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
        @Header("Authorization") token: String,
        @Body updateFullNameRequestDto: UpdateFullNameRequestDto,
    ): UpdateFullNameResponseDto

    @PATCH("auth/user/username")
    suspend fun updateUsername(
        @Header("Authorization") token: String,
        @Body updateUsernameRequestDto: UpdateUsernameRequestDto,
    ): UpdateUsernameResponseDto

    @PATCH("auth/user/email")
    suspend fun updateEmail(
        @Header("Authorization") token: String,
        @Body updateEmailRequestDto: UpdateEmailRequestDto,
    ): UpdateEmailResponseDto

    @PATCH("/auth/user/password")
    suspend fun changePassword(
        @Header("Authorization") token: String,
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
        @Header("Authorization") token: String,
        @Body verifyPasswordRequestDto: VerifyPasswordRequestDto,
    ): VerifyPasswordResponseDto

    @GET("auth/me/{id}")
    suspend fun getUserDetail(
        @Header("Authorization") token: String,
        @Path("id") userId: String,
        @Query("isOwner") isOwner: Boolean,
    ): UserDetailResponseDto

    @GET("auth/profile/{id}")
    suspend fun getUserProfile(
        @Header("Authorization") token: String,
        @Path("id") userId: String,
    ): GetUserProfileResponseDto

    @PATCH("auth/user/avatar/{id}")
    suspend fun updateAvatar(
        @Header("Authorization") authorization: String,
        @Path("id") userId: String,
        @Body updateAvatarRequestDto: UpdateAvatarRequestDto,
    ): UpdateAvatarResponseDto

    @PATCH("auth/user/role")
    suspend fun changeRole(
        @Header("Authorization") token: String,
        @Body request: ChangeRoleRequestDto,
    ): ChangeRoleResponseDto

    @GET("auth/user/search")
    suspend fun searchUser(
        @Header("Authorization") token: String,
        @Query("username") username: String,
        @Query("page") page: Int?,
    ): List<SearchUserResponseDto>

    @POST("auth/coin")
    suspend fun updateCoin(
        @Header("Authorization") token: String,
        @Body request: UpdateCoinRequestDto,
    ): UpdateCoinResponseDto

    @POST("auth/signup/google")
    suspend fun signupWithGoogle(@Body socialCredentialRequestDto: SignupSocialCredentialRequestDto): AuthResponseDto

    @POST("auth/login/google")
    suspend fun loginWithGoogle(@Body authSocialGoogleRequestDto: AuthSocialGoogleRequestDto): AuthResponseDto

    // Upload
    @Multipart
    @POST("upload")
    suspend fun uploadImage(
        @Header("Authorization") authToken: String,
        @Part flashcard: MultipartBody.Part,
    ): UploadImageResponseDto

    @POST("upload/delete")
    suspend fun deleteImage(
        @Header("Authorization") authToken: String,
        @Body deleteImageDto: DeleteImageDto,
    )

    @GET("auth/avatars")
    suspend fun getAvatars(
        @Header("Authorization") token: String,
    ): List<AvatarResponseDto>

    @Multipart
    @POST("upload/avatar/{userId}")
    suspend fun uploadUserAvatar(
        @Header("Authorization") token: String,
        @Part avatar: MultipartBody.Part,
        @Path("userId") userId: String,
    ): UploadImageResponseDto

    // Study Set
    @POST("study-set")
    suspend fun createStudySet(
        @Header("Authorization") token: String,
        @Body createStudySetRequestDto: CreateStudySetRequestDto,
    ): CreateStudySetResponseDto

    @GET("study-set/{id}")
    suspend fun getStudySetById(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): GetStudySetResponseDto

    @GET("study-set/owner/")
    suspend fun getStudySetsByOwnerId(
        @Header("Authorization") token: String,
        @Query("classId") classId: String? = null,
        @Query("folderId") folderId: String? = null,
    ): List<GetStudySetResponseDto>

    @PATCH("study-set/{id}")
    suspend fun updateStudySet(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body updateStudySetRequestDto: UpdateStudySetRequestDto,
    ): UpdateStudySetResponseDto

    @PATCH("study-set/{id}/reset-progress")
    suspend fun resetStudySetProgress(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Query("resetType") resetType: String,
    )

    @DELETE("study-set/{id}")
    suspend fun deleteStudySet(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    )

    @POST("study-set/folders")
    suspend fun addStudySetToFolders(
        @Header("Authorization") token: String,
        @Body addStudySetToFoldersRequestDto: AddStudySetToFoldersRequestDto,
    )

    @POST("study-set/classes")
    suspend fun addStudySetToClasses(
        @Header("Authorization") token: String,
        @Body addStudySetToClassesRequestDto: AddStudySetToClassesRequestDto,
    )

    @GET("study-set/search")
    suspend fun searchStudySet(
        @Header("Authorization") token: String,
        @Query("title") title: String,
        @Query("size") size: String,
        @Query("creatorType") creatorType: String?,
        @Query("page") page: Int,
        @Query("colorId") colorId: Int?,
        @Query("subjectId") subjectId: Int?,
        @Query("isAIGenerated") isAIGenerated: Boolean?,
    ): List<GetStudySetResponseDto>

    @GET("study-set/link/{code}")
    suspend fun getStudySetByLinkCode(
        @Header("Authorization") token: String,
        @Path("code") code: String,
    ): GetStudySetResponseDto

    @POST("study-set/recent")
    suspend fun saveRecentStudySet(
        @Header("Authorization") token: String,
        @Body saveRecentAccessStudySetRequestDto: SaveRecentAccessStudySetRequestDto,
    )

    @GET("study-set/recent/{userId}")
    suspend fun getRecentStudySet(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
    ): List<GetStudySetResponseDto>

    @POST("study-set/ai")
    suspend fun createStudySetByAI(
        @Header("Authorization") token: String,
        @Body createStudySetRequestDto: CreateStudySetByAIRequestDto,
    ): CreateStudySetResponseDto

    @POST("study-set/ai/write-hint")
    suspend fun createWriteHintAI(
        @Header("Authorization") token: String,
        @Body createWriteHintAIModel: CreateWriteHintAIRequestDto,
    ): CreateWriteHintAIResponseDto

    // subject
    @GET("study-set/top-subject")
    suspend fun getTop5Subject(
        @Header("Authorization") token: String,
    ): List<GetTop5SubjectResponseDto>

    @GET("study-set/subject/{subjectId}")
    suspend fun getStudySetBySubjectId(
        @Header("Authorization") token: String,
        @Path("subjectId") subjectId: Int,
        @Query("page") page: Int,
    ): List<GetStudySetResponseDto>

    // Flashcard
    @POST("study-set/duplicate")
    suspend fun duplicateStudySet(
        @Header("Authorization") token: String,
        @Body request: MakeACopyStudySetRequestDto,
    ): CreateStudySetResponseDto

    @GET("/flashcard/study-set/{id}")
    suspend fun getFlashCardsByStudySetId(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Query("learnMode") learnMode: String,
        @Query("isGetAll") isGetAll: Boolean,
        @Query("isSwapped") isSwapped: Boolean? = null,
        @Query("isRandom") isRandom: Boolean? = null,
    ): List<FlashCardResponseDto>

    @POST("flashcard")
    suspend fun createFlashCard(
        @Header("Authorization") token: String,
        @Body createFlashCardDto: CreateFlashCardDto,
    ): FlashCardResponseDto

    @PUT("flashcard/{id}")
    suspend fun updateFlashCard(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body editFlashCardDto: EditFlashCardDto,
    ): FlashCardResponseDto

    @DELETE("flashcard/{id}")
    suspend fun deleteFlashCard(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    )

    @PATCH("flashcard/{id}/starred")
    suspend fun toggleStarredFlashCard(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body toggleStarredFlashCardDto: ToggleStarredFlashCardDto,
    ): UpdateFlashCardResponseDto

    @PATCH("flashcard/{id}/flip-status")
    suspend fun updateFlipFlashCard(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body flipFlashCardDto: FlipFlashCardDto,
    ): UpdateFlashCardResponseDto

    @PATCH("flashcard/{id}/rating")
    suspend fun updateRatingFlashCard(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body ratingFlashCardDto: RatingFlashCardDto,
    ): UpdateFlashCardResponseDto

    @PATCH("flashcard/{id}/quiz-status")
    suspend fun updateQuizStatus(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body quizStatusDto: QuizStatusFlashCardDto,
    ): UpdateFlashCardResponseDto

    @PATCH("flashcard/{id}/true-false-status")
    suspend fun updateTrueFalseStatus(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body trueFalseStatusDto: TrueFalseStatusFlashCardDto,
    ): UpdateFlashCardResponseDto

    @PATCH("flashcard/{id}/write-status")
    suspend fun updateWriteStatus(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body writeStatusDto: WriteStatusFlashCardDto,
    ): UpdateFlashCardResponseDto

    @GET("flashcard/folder/{id}")
    suspend fun getFlashCardsByFolderId(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Query("learnMode") learnMode: String,
        @Query("isGetAll") isGetAll: Boolean,
        @Query("isSwapped") isSwapped: Boolean? = null,
        @Query("isRandom") isRandom: Boolean? = null,
    ): List<FlashCardResponseDto>

    @GET("flashcard/languages")
    suspend fun getLanguages(
        @Header("Authorization") token: String,
    ): List<LanguageDto>

    @GET("flashcard/voices/{languageCode}")
    suspend fun getVoices(
        @Header("Authorization") token: String,
        @Path("languageCode") languageCode: String,
    ): List<VoiceDto>

    @GET("flashcard/speech")
    suspend fun getSpeech(
        @Header("Authorization") token: String,
        @Query("input") input: String,
        @Query("voiceCode") voiceCode: String,
    ): BufferResponseDto

    // Folder
    @POST("folder")
    suspend fun createFolder(
        @Header("Authorization") token: String,
        @Body createFolderRequestDto: CreateFolderRequestDto,
    ): CreateFolderResponseDto

    @GET("folder/{id}")
    suspend fun getFolderById(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): GetFolderResponseDto

    @PUT("folder/{id}")
    suspend fun updateFolder(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body updateFolderRequestDto: UpdateFolderRequestDto,
    ): UpdateFolderResponseDto

    @GET("folder/owner/{ownerId}")
    suspend fun getFoldersByOwnerId(
        @Header("Authorization") token: String,
        @Path("ownerId") ownerId: String,
        @Query("classId") classId: String? = null,
        @Query("studySetId") studySetId: String? = null,
    ): List<GetFolderResponseDto>

    @DELETE("folder/{id}")
    suspend fun deleteFolder(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    )

    @POST("folder/study-sets")
    suspend fun addStudySetToFolder(
        @Header("Authorization") token: String,
        @Body addStudySetToFolderRequestDto: AddStudySetToFolderRequestDto,
    )

    @GET("folder/search")
    suspend fun searchFolder(
        @Header("Authorization") token: String,
        @Query("title") title: String,
        @Query("page") page: Int?,
    ): List<GetFolderResponseDto>

    @GET("folder/link/{code}")
    suspend fun getFolderByLinkCode(
        @Header("Authorization") token: String,
        @Path("code") code: String,
    ): GetFolderResponseDto

    @POST("folder/recent")
    suspend fun saveRecentFolder(
        @Header("Authorization") token: String,
        @Body saveRecentAccessFolderRequestDto: SaveRecentAccessFolderRequestDto,
    )

    @GET("folder/recent/{userId}")
    suspend fun getRecentFolder(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
    ): List<GetFolderResponseDto>

    @PATCH("folder/{id}/reset-progress")
    suspend fun resetProgressFolder(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Query("resetType") resetType: String,
    )

    // Class
    @POST("class")
    suspend fun createClass(
        @Header("Authorization") token: String,
        @Body createClassRequestDto: CreateClassRequestDto,
    ): CreateClassResponseDto

    @GET("class/{id}")
    suspend fun getClassByID(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): GetClassDetailResponseDto

    @GET("class/user/{userId}")
    suspend fun getClassByOwnerID(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Query("folderId") folderId: String?,
        @Query("studySetId") studySetId: String?,
    ): List<GetClassByOwnerResponseDto>

    @DELETE("class/{id}")
    suspend fun deleteClass(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    )

    @PUT("class/{id}")
    suspend fun updateClass(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body updateClassRequestDto: UpdateClassRequestDto,
    ): UpdateClassResponseDto

    @POST("class/study-sets")
    suspend fun addStudySetToClass(
        @Header("Authorization") token: String,
        @Body addStudySetToClassRequestDto: AddStudySetToClassRequestDto,
    )

    @POST("class/folders")
    suspend fun addFolderToClass(
        @Header("Authorization") token: String,
        @Body addFolderToClassRequestDto: AddFolderToClassRequestDto,
    )

    @GET("class/search")
    suspend fun searchClass(
        @Header("Authorization") token: String,
        @Query("title") title: String,
        @Query("page") page: Int?,
    ): List<GetClassByOwnerResponseDto>

    @GET("class/token/{joinToken}")
    suspend fun getClassByJoinToken(
        @Header("Authorization") token: String,
        @Path("joinToken") joinToken: String,
        @Query("userId") userId: String,
    ): GetClassDetailResponseDto

    @POST("class/join")
    suspend fun joinClass(
        @Header("Authorization") token: String,
        @Body joinClassRequestDto: JoinClassRequestDto,
    )

    @POST("class/exit")
    suspend fun exitClass(
        @Header("Authorization") token: String,
        @Body exitClassRequestDto: ExitClassRequestDto,
    )

    @POST("class/members")
    suspend fun removeMembers(
        @Header("Authorization") token: String,
        @Body removeMembersRequestDto: RemoveMembersRequestDto,
    )

    @POST("class/remove-study-set")
    suspend fun deleteStudySetInClass(
        @Header("Authorization") token: String,
        @Body deleteStudySetsRequestDto: DeleteStudySetsRequestDto,
    )

    @POST("class/remove-folder")
    suspend fun deleteFolderInClass(
        @Header("Authorization") token: String,
        @Body deleteFolderRequestDto: DeleteFolderRequestDto,
    )

    @POST("class/recent")
    suspend fun saveRecentClass(
        @Header("Authorization") token: String,
        @Body saveRecentAccessClassRequestDto: SaveRecentAccessClassRequestDto,
    )

    @GET("class/recent/{userId}")
    suspend fun getRecentClass(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
    ): List<GetClassByOwnerResponseDto>

    @POST("class/invite")
    suspend fun inviteUserToClass(
        @Header("Authorization") token: String,
        @Body inviteToClassRequestDto: InviteToClassRequestDto,
    ): InviteToClassResponseDto

    // Streak
    @GET("streak/{userId}")
    suspend fun getStreaksByUserId(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
    ): GetStreakDto

    @POST("streak")
    suspend fun updateStreak(
        @Header("Authorization") token: String,
        @Body increaseStreakDto: IncreaseStreakDto,
    ): StreakDto

    @GET("streak/top")
    suspend fun getTopStreaks(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int?,
    ): List<GetTopStreakResponseDto>

    // Notification
    @POST("notifications/register")
    suspend fun sendDeviceToken(
        @Header("Authorization") authorization: String,
        @Body tokenRequest: DeviceTokenRequestDto,
    ): Response<Unit>

    @GET("notifications/user/{id}")
    suspend fun getNotificationsByUserId(
        @Header("Authorization") token: String,
        @Path("id") userId: String,
    ): List<GetNotificationResponseDto>

    @PATCH("notifications/{id}/read")
    suspend fun markNotificationAsRead(
        @Header("Authorization") token: String,
        @Path("id") notificationId: String,
        @Body requestDto: MarkNotificationReadRequestDto,
    )

    @DELETE("notifications/{id}")
    suspend fun deleteNotification(
        @Header("Authorization") token: String,
        @Path("id") notificationId: String,
    )

    @POST("notifications/clear")
    suspend fun clearAllNotifications(
        @Header("Authorization") token: String,
    )

    // Study Time
    @GET("study-time/study-set/{studySetId}")
    suspend fun getStudyTimeByStudySet(
        @Header("Authorization") token: String,
        @Path("studySetId") studySetId: String,
    ): GetStudyTimeByStudySetResponseDto

    @GET("study-time/user/{userId}")
    suspend fun getStudyTimeByUser(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
    ): GetStudyTimeByUserResponseDto

    @POST("study-time")
    suspend fun createStudyTime(
        @Header("Authorization") token: String,
        @Body createStudyTimeDto: CreateStudyTimeDto,
    )

    // Report
    @POST("report")
    suspend fun createReport(
        @Header("Authorization") token: String,
        @Body createReportRequestDto: CreateReportRequestDto,
    )

    // PixaBay
    @GET("pixabay/search")
    suspend fun searchImage(
        @Header("Authorization") token: String,
        @Query("query") query: String,
    ): SearchImageResponseDto

}