package com.why.test.data.http

import com.why.lib_base.network.ApiResponse
import com.why.test.data.bean.ArticleList
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 *
 * @author why
 * @date 2022/11/14 10:55
 */
interface Api {

    /**
     * 获取文章列表
     * */
    @GET("blog/posts.json")
    suspend fun getArticlePageList(): List<ArticleList>

}