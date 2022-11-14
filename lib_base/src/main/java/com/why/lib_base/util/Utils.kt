package com.why.lib_base.util

import android.content.Context
import androidx.annotation.NonNull
import com.why.lib_base.base.BaseApp

/**
 *
 * @author why
 * @date 2022/11/14 21:55
 */
object Utils {
    fun dp2px(dp: Float): Int {
        return (dp * BaseApp.appContext.resources.displayMetrics.density + 0.5f).toInt()
    }

    /**
     * dp转为px
     *
     * @param context 上下文
     * @param dp dp
     */
    fun dp2px( context: Context?, dp: Int): Int {
        val density = context!!.resources.displayMetrics.density
        return (density * dp).toInt()
    }

    /**
     * px转dp
     *
     * @param context 上下文
     * @param px px
     */
    fun px2dp( context: Context?, px: Int): Int {
        val density = context!!.resources.displayMetrics.density
        return (px / density).toInt()
    }

    /**
     * sp转px
     *
     * @param context 上下文
     * @param sp sp
     */
    fun sp2px(context: Context?, sp: Int): Int {
        val scaledDensity = context!!.resources.displayMetrics.scaledDensity
        return (sp * scaledDensity).toInt()
    }

    /**
     * sp转px
     *
     * @param context 上下文
     * @param px px
     */
    fun px2sp(context: Context?, px: Int): Int {
        val scaledDensity = context!!.resources.displayMetrics.scaledDensity
        return (px / scaledDensity).toInt()
    }
}