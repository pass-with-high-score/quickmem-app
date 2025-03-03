package com.pwhs.quickmem.data.remote.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import com.pwhs.quickmem.utils.isInternetAvailable

class CacheInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = if (isInternetAvailable(context))
            request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
        else
            request.newBuilder().header(
                "Cache-Control",
                "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
            ).build()
        val response = chain.proceed(request)
        val cacheControl = response.header("Cache-Control")
        return if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains(
                "no-cache"
            ) ||
            cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")
        ) {
            response.newBuilder()
                .header("Cache-Control", "public, max-age=" + 5)
                .build()
        } else {
            response
        }
    }
}