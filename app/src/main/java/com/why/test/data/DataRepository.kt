package com.why.test.data

import com.why.lib_base.network.ApiResponse
import com.why.lib_base.network.BaseRepository
import com.why.lib_base.network.RetrofitManager
import com.why.test.data.bean.ArticleList
import com.why.test.data.http.Api

/**
 *
 * @author why
 * @date 2022/11/14 10:55
 */
object DataRepository  : BaseRepository(), Api {
    private val service by lazy { RetrofitManager.getService(Api::class.java) }

    /**
     * 获取文章列表
     * */
    override suspend fun getArticlePageList(): List<ArticleList> {
        return apiCallNormal { service.getArticlePageList() }
    }
}