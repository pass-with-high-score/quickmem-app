package com.pwhs.quickmem.core.di

import android.content.Context
import com.pwhs.quickmem.BuildConfig
import com.pwhs.quickmem.core.datastore.TokenManager
import com.pwhs.quickmem.core.utils.AppConstant.BASE_URL
import com.pwhs.quickmem.data.remote.ApiService
import com.pwhs.quickmem.data.remote.interceptor.AuthInterceptor
import com.pwhs.quickmem.data.remote.interceptor.CacheInterceptor
import com.pwhs.quickmem.data.remote.interceptor.InfoInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideQuickMemApi(
        @ApplicationContext context: Context,
        authInterceptor: AuthInterceptor
    ): ApiService {

        val cacheSize = (5 * 1024 * 1024).toLong() // 5 MB
        val myCache = Cache(context.cacheDir, cacheSize)

        val versionName = BuildConfig.VERSION_NAME
        val versionCode = BuildConfig.VERSION_CODE

        val okHttpClient = OkHttpClient.Builder()
            .cache(myCache)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level =
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            })
            .addInterceptor(CacheInterceptor(context = context))
            .addInterceptor(
                InfoInterceptor(
                    versionName = versionName,
                    versionCode = versionCode
                )
            )
            .addInterceptor(authInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        tokenManager: TokenManager,
    ): AuthInterceptor {
        return AuthInterceptor(tokenManager)
    }
}