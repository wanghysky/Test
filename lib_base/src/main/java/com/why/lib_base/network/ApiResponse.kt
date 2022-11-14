package com.why.lib_base.network

/**
 *
 * @author why
 * @date 2022/11/14 10:05
 */
data class ApiResponse<T>(
    val data: T,
    val errorCode: Int,
    val errorMsg: String
)