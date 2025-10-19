package com.pwhs.quickmem.core.datastore

import android.content.Context
import android.util.Patterns
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.pwhs.quickmem.utils.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

class AppManager @Inject constructor(private val context: Context) {
    companion object {
        val IS_FIRST_RUN = booleanPreferencesKey("IS_FIRST_RUN")
        val IS_LOGGED_IN = booleanPreferencesKey("IS_LOGGED_IN")
        val POST_LOGIN_OPEN_COUNT = intPreferencesKey("POST_LOGIN_OPEN_COUNT")
        val REVIEW_PROMPT_SHOWN = booleanPreferencesKey("REVIEW_PROMPT_SHOWN")
        val USER_ID = stringPreferencesKey("USER_ID")
        val USER_FULL_NAME = stringPreferencesKey("USER_FULL_NAME")
        val USER_AVATAR = stringPreferencesKey("USER_AVATAR")
        val USER_NAME = stringPreferencesKey("USER_NAME")
        val USER_EMAIL = stringPreferencesKey("USER_EMAIL")
        val USER_BIRTHDAY = stringPreferencesKey("USER_BIRTHDAY")
        val USER_CREATED_AT = stringPreferencesKey("USER_CREATED_AT")
        val USER_LOGIN_PROVIDER = stringPreferencesKey("USER_LOGIN_PROVIDER")
        val PUSH_NOTIFICATIONS = booleanPreferencesKey("PUSH_NOTIFICATIONS")
        val APP_PUSH_NOTIFICATIONS = booleanPreferencesKey("APP_PUSH_NOTIFICATIONS")
        val USER_COINS = intPreferencesKey("USER_COINS")
        val ENABLED_STUDY_SCHEDULE = booleanPreferencesKey("ENABLED_STUDY_SCHEDULE")
        val TIME_STUDY_SCHEDULE = stringPreferencesKey("TIME_STUDY_SCHEDULE")
        val IS_PLAY_SOUND = booleanPreferencesKey("IS_PLAY_SOUND")
    }

    val isFirstRun: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_FIRST_RUN] != false
        }
    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_LOGGED_IN] == true
        }

    val postLoginOpenCount: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[POST_LOGIN_OPEN_COUNT] ?: 0
        }

    val reviewPromptShown: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[REVIEW_PROMPT_SHOWN] == true
        }
    val userId: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USER_ID] ?: ""
        }
    val userFullName: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USER_FULL_NAME] ?: ""
        }
    val username: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USER_NAME] ?: ""
        }
    val userAvatarUrl: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USER_AVATAR] ?: ""
        }
    val userEmail: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USER_EMAIL] ?: ""
        }
    val pushNotifications: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PUSH_NOTIFICATIONS] != false
        }
    val appPushNotifications: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[APP_PUSH_NOTIFICATIONS] == true
        }

    val userBirthday: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USER_BIRTHDAY] ?: ""
        }

    val userCoins: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[USER_COINS] ?: 0
        }

    val enabledStudySchedule: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[ENABLED_STUDY_SCHEDULE] == true
        }

    val timeStudySchedule: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[TIME_STUDY_SCHEDULE] ?: ""
        }

    val isPlaySound: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_PLAY_SOUND] != false
        }

    val userLoginProviders: Flow<List<String>> = context.dataStore.data
        .map { preferences ->
            val json = preferences[USER_LOGIN_PROVIDER] ?: "[]"
            Json.decodeFromString(ListSerializer(String.serializer()), json)
        }

    val userCreatedAt: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USER_CREATED_AT] ?: ""
        }

    suspend fun saveIsFirstRun(isFirstRun: Boolean) {
        Timber.d("Saving is first run: $isFirstRun")
        context.dataStore.edit { preferences ->
            preferences[IS_FIRST_RUN] = isFirstRun
        }
    }

    suspend fun saveIsLoggedIn(isLoggedIn: Boolean) {
        Timber.d("Saving is logged in: $isLoggedIn")
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = isLoggedIn
        }
    }

    suspend fun resetReviewCountersOnLoginSuccess() {
        Timber.d("Resetting review counters on login success")
        context.dataStore.edit { preferences ->
            preferences[POST_LOGIN_OPEN_COUNT] = 0
            preferences[REVIEW_PROMPT_SHOWN] = false
        }
    }

    suspend fun incrementPostLoginOpenCount() {
        context.dataStore.edit { preferences ->
            val current = preferences[POST_LOGIN_OPEN_COUNT] ?: 0
            preferences[POST_LOGIN_OPEN_COUNT] = current + 1
        }
    }

    suspend fun setReviewPromptShown(shown: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[REVIEW_PROMPT_SHOWN] = shown
        }
    }

    suspend fun saveUserId(userId: String) {
        Timber.d("Saving user id: $userId")
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }

    suspend fun saveUserFullName(userFullName: String) {
        Timber.d("Saving user full name: $userFullName")
        context.dataStore.edit { preferences ->
            preferences[USER_FULL_NAME] = userFullName
        }
    }

    suspend fun saveUserAvatar(userAvatar: String) {
        Timber.d("Saving user avatar: $userAvatar")
        context.dataStore.edit { preferences ->
            preferences[USER_AVATAR] = userAvatar
        }
    }

    suspend fun saveUserName(userName: String) {
        Timber.d("Saving user name: $userName")
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = userName
        }
    }

    suspend fun saveUserEmail(email: String) {
        require(email.isNotEmpty()) { "Email cannot be empty" }
        require(Patterns.EMAIL_ADDRESS.matcher(email).matches()) { "Invalid email address" }
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL] = email
        }
    }

    suspend fun savePushNotifications(pushNotifications: Boolean) {
        Timber.d("Saving push notifications: $pushNotifications")
        context.dataStore.edit { preferences ->
            preferences[PUSH_NOTIFICATIONS] = pushNotifications
        }
    }

    suspend fun saveAppPushNotifications(appPushNotifications: Boolean) {
        Timber.d("Saving app push notifications: $appPushNotifications")
        context.dataStore.edit { preferences ->
            preferences[APP_PUSH_NOTIFICATIONS] = appPushNotifications
        }
    }

    suspend fun saveUserBirthday(birthday: String) {
        Timber.d("Saving user birthday: $birthday")
        context.dataStore.edit { preferences ->
            preferences[USER_BIRTHDAY] = birthday
        }
    }

    suspend fun saveUserCoins(coins: Int) {
        Timber.d("Saving user coins: $coins")
        context.dataStore.edit { preferences ->
            preferences[USER_COINS] = coins
        }
    }

    suspend fun saveEnabledStudySchedule(enabled: Boolean) {
        Timber.d("Saving enabled study schedule: $enabled")
        context.dataStore.edit { preferences ->
            preferences[ENABLED_STUDY_SCHEDULE] = enabled
        }
    }

    suspend fun saveTimeStudySchedule(time: String) {
        Timber.d("Saving time study schedule: $time")
        context.dataStore.edit { preferences ->
            preferences[TIME_STUDY_SCHEDULE] = time
        }
    }

    suspend fun saveIsPlaySound(isPlaySound: Boolean) {
        Timber.d("Saving is play sound: $isPlaySound")
        context.dataStore.edit { preferences ->
            preferences[IS_PLAY_SOUND] = isPlaySound
        }
    }

    suspend fun saveUserLoginProviders(loginProviders: List<String>) {
        Timber.d("Saving user login providers: $loginProviders")
        val json = Json.encodeToString(ListSerializer(String.serializer()), loginProviders)
        context.dataStore.edit { preferences ->
            preferences[USER_LOGIN_PROVIDER] = json
        }
    }
    suspend fun saveUserCreatedAt(createdAt: String) {
        Timber.d("Saving user created at: $createdAt")
        context.dataStore.edit { preferences ->
            preferences[USER_CREATED_AT] = createdAt
        }
    }

    suspend fun clearAllData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
            preferences[IS_FIRST_RUN] = false
        }
    }
}
