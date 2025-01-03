package com.pwhs.quickmem.presentation.auth.utils

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialOption
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pwhs.quickmem.BuildConfig
import com.pwhs.quickmem.core.data.enums.AuthProvider
import com.pwhs.quickmem.domain.model.auth.AuthSocialGoogleRequestModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.security.MessageDigest
import java.util.UUID

class GoogleSignInUtils {

    companion object {
        fun doGoogleSignIn(
            context: Context,
            scope: CoroutineScope,
            launcher: ManagedActivityResultLauncher<Intent, ActivityResult>?,
            login: (AuthSocialGoogleRequestModel) -> Unit = {},
        ) {
            val credentialManager = CredentialManager.create(context)

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(getCredentialOptions())
                .build()
            scope.launch {
                try {
                    val result = credentialManager.getCredential(context, request)
                    when (result.credential) {
                        is CustomCredential -> {
                            if (result.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                                val googleIdTokenCredential =
                                    GoogleIdTokenCredential.createFrom(result.credential.data)
                                val googleTokenId = googleIdTokenCredential.idToken
                                val avatar = googleIdTokenCredential.profilePictureUri
                                val id = googleIdTokenCredential.id
                                val authCredential =
                                    GoogleAuthProvider.getCredential(googleTokenId, null)
                                val user =
                                    Firebase.auth.signInWithCredential(authCredential).await().user

                                val authSocialGoogleRequestModel = AuthSocialGoogleRequestModel(
                                    id = id,
                                    email = user?.email ?: "",
                                    provider = AuthProvider.GOOGLE.name,
                                    displayName = user?.displayName ?: "",
                                    photoUrl = avatar.toString(),
                                    idToken = googleTokenId
                                )

                                login(authSocialGoogleRequestModel)
                            }
                        }

                        else -> {
                            Timber.e("No credential found")
                            launcher?.launch(getIntent())
                        }
                    }
                } catch (e: NoCredentialException) {
                    Timber.e("No credential found%s", e.message)
                    launcher?.launch(getIntent())
                } catch (e: GetCredentialException) {
                    Timber.e("Error getting credential%s", e.message)
                } catch (e: Exception) {
                    Timber.e("Error%s", e.message)
                }
            }
        }

        private fun getIntent(): Intent {
            return Intent(Settings.ACTION_ADD_ACCOUNT).apply {
                putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
            }
        }

        private fun getCredentialOptions(): CredentialOption {
            val rawNonce = UUID.randomUUID().toString()
            val bytes = rawNonce.toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            val hashedNonce = digest.fold("") { str, it ->
                str + "%02x".format(it)
            }
            return GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(true)
                .setAutoSelectEnabled(true)
                .setServerClientId(BuildConfig.GOOGLE_OAUTH_CLIENT_ID)
                .setNonce(hashedNonce)
                .build()
        }
    }
}