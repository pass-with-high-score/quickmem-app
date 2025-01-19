package com.pwhs.quickmem.core.di

import android.content.Context
import com.pwhs.quickmem.BuildConfig
import com.pwhs.quickmem.core.utils.AppConstant.BASE_URL
import com.pwhs.quickmem.data.remote.ApiService
import com.pwhs.quickmem.utils.isInternetAvailable
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
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
    fun provideQuickMemApi(@ApplicationContext context: Context): ApiService {

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
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            })
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (isInternetAvailable(context))
                    request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
                else
                    request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
                chain.proceed(request)
            }
            .addNetworkInterceptor { chain ->
                val response = chain.proceed(chain.request())
                val cacheControl = response.header("Cache-Control")
                if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                    cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
                    response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + 5)
                        .build()
                } else {
                    response
                }
            }
            .addInterceptor(
                Interceptor { chain ->
                    val request: Request = chain.request()
                        .newBuilder()
                        .header("accept", "application/json")
                        .header("versionName", versionName)
                        .header("versionCode", versionCode.toString())
                        .header("timestamp", System.currentTimeMillis().toString())
                        .header("platform", "android")
                        .build()
                    chain.proceed(request)
                }
            )
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }
}