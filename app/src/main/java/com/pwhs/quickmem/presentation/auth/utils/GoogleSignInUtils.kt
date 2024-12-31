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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

class GoogleSignInUtils {

    companion object {
        fun doGoogleSignIn(
            context: Context,
            scope: CoroutineScope,
            launcher: ManagedActivityResultLauncher<Intent, ActivityResult>?,
            login: () -> Unit,
        ) {
            val credentialManager = CredentialManager.create(context)

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(getCredentialOptions())
                .build()
            scope.launch {
                try {
                    val result = credentialManager.getCredential(context, request)
                    Timber.d("Credential result: $result")
                    when (result.credential) {
                        is CustomCredential -> {
                            if (result.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                                val googleIdTokenCredential =
                                    GoogleIdTokenCredential.createFrom(result.credential.data)
                                val googleTokenId = googleIdTokenCredential.idToken
                                val avatar = googleIdTokenCredential.profilePictureUri
                                val firstName = googleIdTokenCredential.givenName
                                val lastName = googleIdTokenCredential.familyName
                                val id = googleIdTokenCredential.id
                                Timber.d("GoogleIdToken: $googleTokenId")
                                Timber.d("Avatar: $avatar")
                                Timber.d("First Name: $firstName")
                                Timber.d("Last Name: $lastName")
                                Timber.d("Id: $id")
                            }
                        }

                        else -> {

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
            return GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setAutoSelectEnabled(false)
                .setServerClientId("743857474439-b8umbci861okgpukka3l8lr1tfrvjso8.apps.googleusercontent.com")
                .build()
        }
    }
}