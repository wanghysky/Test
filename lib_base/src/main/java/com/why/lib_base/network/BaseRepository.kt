package com.why.lib_base.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *
 * @author why
 * @date 2022/11/14 09:57
 */
open class BaseRepository {

    suspend fun <T> apiCall(api: suspend () -> ApiResponse<T>): ApiResponse<T> {
        return withContext(Dispatchers.IO) { api.invoke() }
    }

    suspend fun <T>apiCallNormal(api: suspend () -> T): T {
        return withContext(Dispatchers.IO) { api.invoke() }
    }

    suspend fun apiCallString(api: suspend () -> String): String {
        return withContext(Dispatchers.IO) { api.invoke() }
    }
}