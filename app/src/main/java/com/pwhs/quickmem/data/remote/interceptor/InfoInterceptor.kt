package com.pwhs.quickmem.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class InfoInterceptor(
    private val versionName: String,
    private val versionCode: Int
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
            .newBuilder()
            .header("accept", "application/json")
            .header("versionName", versionName)
            .header("versionCode", versionCode.toString())
            .header("timestamp", System.currentTimeMillis().toString())
            .header("platform", "android")
            .build()
        return chain.proceed(request)
    }
}