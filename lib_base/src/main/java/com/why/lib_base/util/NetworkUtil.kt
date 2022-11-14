package com.why.lib_base.util

import android.content.Context
import android.net.ConnectivityManager

/**
 *
 * @author why
 * @date 2022/11/14 09:58
 */
object NetworkUtil {
    /**
     * 网络是否可用
     *
     * @param context Context
     * @return  网络是否可用
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val manager =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return manager.activeNetworkInfo?.isAvailable == true
    }


    /**
     * 是否连接Wifi
     *
     * @param context
     * @return boolean
     */
    fun isWifi(context: Context): Boolean {
        val connectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetInfo = connectivityManager.activeNetworkInfo
        return activeNetInfo?.type == ConnectivityManager.TYPE_WIFI
    }
}