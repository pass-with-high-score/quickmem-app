package com.pwhs.quickmem.core.utils

import kotlinx.serialization.json.Json

object Serde {
    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = false
        encodeDefaults = true
    }
}