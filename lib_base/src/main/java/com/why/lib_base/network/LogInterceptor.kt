package com.why.lib_base.network

import android.util.Log.d
import com.why.lib_base.BuildConfig
import com.why.lib_base.util.LogUtil
import okhttp3.logging.HttpLoggingInterceptor

/**
 *
 * @author why
 * @date 2022/11/14 09:57
 */
val logInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        // 使用自己的日志工具接管
        LogUtil.d(message)
    }
}).setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.BASIC)