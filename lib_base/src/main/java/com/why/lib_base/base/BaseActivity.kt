package com.why.lib_base.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.why.lib_base.R
import com.why.lib_base.ext.hideLoading
import com.why.lib_base.util.LogUtil
import com.why.lib_base.util.StatusBarUtil
import com.why.lib_base.util.ToastUtil
import java.lang.reflect.ParameterizedType
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 *
 * @author why
 * @date 2022/11/14 10:02
 */
abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {

    @Suppress("UNCHECKED_CAST")
    protected val viewModel: VM by lazy {
        val type = javaClass.genericSuperclass
        val modelClass: Class<VM> = (type as ParameterizedType).actualTypeArguments[0] as Class<VM>
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[modelClass]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置沉浸式状态栏，由于启动页SplashActivity需要无状态栏，这里写死不太好
        // 直接在主题里将其他的状态栏颜色写成跟ActionBar相同，而启动页则是无状态栏
        // 或者提供一个修改的api让SplashActivity重写，两者均可（假如需要更换主题用代码设置更灵活）
        if (setFullScreen()) {
            StatusBarUtil.setNoStatus(this)
        } else {
            StatusBarUtil.setImmersionStatus(this)
        }

        createObserve()
        initObserver()
    }

    /** 是否是无状态栏的全屏模式 */
    open fun setFullScreen(): Boolean {
        return false
    }

    /**
     * 注册Observer
     * */
    open fun initObserver() {

    }

    /** 提供编写LiveData监听逻辑的方法 */
    open fun createObserve() {
        // 全局服务器请求错误监听
        viewModel.apply {
            exception.observe(this@BaseActivity) {
                requestError(it.message)
                LogUtil.e("网络请求错误：${it.message}")
                when (it) {
                    is SocketTimeoutException -> ToastUtil.showShort(
                        this@BaseActivity,
                        getString(R.string.request_time_out)
                    )
                    is ConnectException, is UnknownHostException -> ToastUtil.showShort(
                        this@BaseActivity,
                        getString(R.string.network_error)
                    )
                    else -> ToastUtil.showShort(
                        this@BaseActivity, it.message ?: getString(R.string.response_error)
                    )
                }
            }

            // 全局服务器返回的错误信息监听
            errorResponse.observe(this@BaseActivity) {
                requestError(it?.errorMsg)
                it?.errorMsg?.run {
                    ToastUtil.showShort(this@BaseActivity, this)
                }
            }
        }
    }

    /** 提供一个请求错误的方法,用于像关闭加载框,显示错误布局之类的 */
    open fun requestError(msg: String?) {
        hideLoading()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clear()
    }
}