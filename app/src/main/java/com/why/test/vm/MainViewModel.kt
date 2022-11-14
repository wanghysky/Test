package com.why.test.vm

import com.why.lib_base.base.BaseViewModel
import com.why.lib_base.ext.handleRequest
import com.why.lib_base.ext.launch
import com.why.lib_base.util.LogUtil
import com.why.test.data.DataRepository

/**
 *
 * @author why
 * @date 2022/11/14 10:41
 */
class MainViewModel: BaseViewModel() {

    fun getArticlePageList() {
        launch({
            handleRequest(
                DataRepository.getArticlePageList()
            ) { LogUtil.e("TEST : isLifeCirce  $it") }
        })
    }

    override fun clear() {

    }

}