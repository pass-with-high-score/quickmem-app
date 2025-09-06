package com.pwhs.quickmem.data.remote.interceptor

import com.pwhs.quickmem.core.datastore.TokenManager
import com.pwhs.quickmem.core.utils.AppConstant.BASE_URL
import com.pwhs.quickmem.core.utils.Serde
import com.pwhs.quickmem.data.dto.auth.RefreshTokenRequestDto
import com.pwhs.quickmem.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    private val refreshTokenService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(Serde.json.asConverterFactory("application/json".toMediaType()))
            .client(OkHttpClient.Builder().build())
            .build()
            .create(ApiService::class.java)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val token = runBlocking { tokenManager.accessToken.first() }
        val newRequest = if (token != null) {
            request.newBuilder()
                .addHeader("Authorization", token)
                .build()
        } else {
            request
        }

        val response = chain.proceed(newRequest)
        if (response.code == 401) {
            val refreshToken = runBlocking { tokenManager.refreshToken.first() }
            if (refreshToken != null) {
                return runBlocking {
                    val tokenResponse =
                        refreshTokenService.refreshToken(RefreshTokenRequestDto(refreshToken))
                    if (tokenResponse.isSuccessful) {
                        val newAccessToken = tokenResponse.body()?.accessToken
                        val newRefreshToken = tokenResponse.body()?.refreshToken

                        if (newAccessToken != null && newRefreshToken != null) {
                            withContext(Dispatchers.IO) {
                                tokenManager.saveAccessToken(newAccessToken)
                                tokenManager.saveRefreshToken(newRefreshToken)
                            }
                        }

                        response.close()

                        val retriedRequest = request.newBuilder()
                            .addHeader("Authorization", "Bearer $newAccessToken")
                            .build()

                        chain.proceed(retriedRequest)
                    } else {
                        tokenManager.clearTokens()
                        response
                    }
                }
            }
        }

        return response
    }
}

