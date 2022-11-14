package com.why.lib_base.ext

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.why.lib_base.base.BaseViewModel
import com.why.lib_base.network.ApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 *
 * @author why
 * @date 2022/11/14 10:44
 */
fun BaseViewModel.launch(
    tryBlock: suspend CoroutineScope.() -> Unit,
    catchBlock: suspend CoroutineScope.() -> Unit = {},
    finallyBlock: suspend CoroutineScope.() -> Unit = {}
) {
    // 默认是执行在主线程，相当于launch(Dispatchers.Main)
    viewModelScope.launch {
        try {
            tryBlock()
        } catch (e: Exception) {
            exception.value = e
            catchBlock()
        } finally {
            finallyBlock()
        }
    }
}

/**
 * 请求结果处理
 *
 * @param response ApiResponse
 * @param successBlock 服务器请求成功返回成功码的执行回调，默认空实现
 * @param errorBlock 服务器请求成功返回错误码的执行回调，默认返回false的空实现，函数返回值true:拦截统一错误处理，false:不拦截
 */
suspend fun <T> BaseViewModel.handleRequest(
    response: ApiResponse<T>,
    successBlock: suspend CoroutineScope.(response: ApiResponse<T>) -> Unit = {},
    errorBlock: suspend CoroutineScope.(response: ApiResponse<T>) -> Boolean = { false }
) {
    coroutineScope {
        when (response.errorCode) {
            0 -> successBlock(response) // 服务器返回请求成功码
            else -> { // 服务器返回的其他错误码
                if (!errorBlock(response)) {
                    // 只有errorBlock返回false不拦截处理时，才去统一提醒错误提示
                    errorResponse.value = response
                }
            }
        }
    }
}

suspend fun BaseViewModel.handleRequest(
    response: String,
    successBlock: suspend CoroutineScope.(response: String) -> Unit = {},
) {
    coroutineScope {
        successBlock(response)
    }
}

suspend fun <T>BaseViewModel.handleRequest(
    response: T,
    successBlock: suspend CoroutineScope.(response: T) -> Unit = {},
) {
    coroutineScope {
        successBlock(response)
    }
}