package com.pwhs.quickmem.presentation.auth.utils

import android.content.Context
import android.content.Intent
import android.util.Base64
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialOption
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.pwhs.quickmem.BuildConfig
import com.pwhs.quickmem.core.data.enums.AuthProvider
import com.pwhs.quickmem.domain.model.auth.AuthSocialGoogleRequestModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONObject
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
            scope.launch {
                try {
                    val credentialManager = CredentialManager.create(context)
                    val request: GetCredentialRequest = GetCredentialRequest.Builder()
                        .addCredentialOption(getCredentialOptions())
                        .build()

                    val result = credentialManager.getCredential(
                        request = request,
                        context = context,
                    )

                    val googleIdTokenCredential = GoogleIdTokenCredential
                        .createFrom(result.credential.data)


                    val authSocialGoogleRequestModel = AuthSocialGoogleRequestModel(
                        id = googleIdTokenCredential.id,
                        email = extractEmailFromIdToken(googleIdTokenCredential.idToken).toString(),
                        provider = AuthProvider.GOOGLE.name,
                        displayName = googleIdTokenCredential.displayName ?: "",
                        photoUrl = googleIdTokenCredential.profilePictureUri.toString(),
                        idToken = googleIdTokenCredential.idToken
                    )

                    login(authSocialGoogleRequestModel)

                } catch (e: GetCredentialException) {
                    Timber.e(e, "GetCredentialException")
                    val intent = Intent(android.provider.Settings.ACTION_ADD_ACCOUNT)
                    intent.putExtra(android.provider.Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
                    launcher?.launch(intent)
                } catch (e: GoogleIdTokenParsingException) {
                    Timber.e(e, "GoogleIdTokenParsingException")
                } catch (e: Exception) {
                    Timber.e(e, "Exception")
                }
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

        private fun extractEmailFromIdToken(idToken: String): String? {
            return try {
                val parts = idToken.split(".")
                if (parts.size < 2) return null

                val payloadJson = String(
                    Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
                )
                val payload = JSONObject(payloadJson)
                val email = payload.optString("email", "")
                email.ifBlank { null }
            } catch (e: Exception) {
                Timber.e(e)
                null
            }
        }

    }
}