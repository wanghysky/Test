package com.why.lib_base.base

import android.app.Application
import android.content.Context
import android.os.Environment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.tencent.mmkv.MMKV
import kotlin.properties.Delegates

/**
 *
 * @author why
 * @date 2022/11/14 10:09
 */
open class BaseApp : Application(), ViewModelStoreOwner {

    private val mAppViewModelStore: ViewModelStore by lazy {
        ViewModelStore()
    }
    private var mFactory: ViewModelProvider.Factory? = null

    companion object {
        var appContext: Context by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        // MMKV初始化
        MMKV.initialize(this)

        //Fresco缓存
        Fresco.initialize(
            this, ImagePipelineConfig.newBuilder(appContext)
                .setMainDiskCacheConfig(
                    DiskCacheConfig.newBuilder(appContext)
                        .build()
                )
                .build()
        )
    }

    /** 获取一个全局的ViewModel */
    fun getAppViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(this, getAppFactory())
    }

    private fun getAppFactory(): ViewModelProvider.Factory {
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        }
        return mFactory as ViewModelProvider.Factory
    }

    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore
    }
}