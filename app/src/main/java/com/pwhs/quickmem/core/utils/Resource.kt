package com.pwhs.quickmem.core.utils

sealed class Resources<T>(
    val data: T? = null,
    val message: String? = null,
    val status: Int? = null,
) {
    class Success<T>(data: T?) : Resources<T>(data)
    class Error<T>(message: String, status: Int? = null) : Resources<T>(null, message, status)

    class Loading<T>(val isLoading: Boolean = true) : Resources<T>(null)
}