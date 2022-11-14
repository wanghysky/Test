package com.why.test.vm

import com.why.lib_base.base.BaseViewModel
import com.why.lib_base.ext.handleRequest
import com.why.lib_base.ext.launch
import com.why.lib_base.livedata.SafeMutableLiveData
import com.why.test.data.DataRepository
import com.why.test.data.bean.ArticleList
import com.why.test.data.bean.Tabs

/**
 *
 * @author why
 * @date 2022/11/14 16:51
 */
class MainArticleListViewModel: BaseViewModel() {
    var currentTabSelectId = 0
    var tabList: MutableList<Tabs> = arrayListOf()
    var articleList = mutableMapOf <String, MutableList<ArticleList>>()

    val bannerData = SafeMutableLiveData<MutableList<ArticleList>>("bannerData")
    val tabsData = SafeMutableLiveData<MutableList<Tabs>>("tabsData")
    val tabScrollTo = SafeMutableLiveData<Int>("tabScrollTo")

    fun getArticlePageList() {
        launch({
            handleRequest(
                DataRepository.getArticlePageList()
            ) {
                val list = it.toMutableList()
                bannerData.value  = list
                val tags = mutableMapOf <String, MutableList<ArticleList>>()
                val tabsList = arrayListOf<Tabs>()
                var position = 1
                setFirstTag(tags, tabsList, list)
                list.map { article ->
                    article.frontmatter?.apply {
                        if((categories?.size ?: 0) > 0){
                            val tag = categories!![0]
                            if(tag.isNotBlank() && tags.containsKey(tag)){
                                val tagList = tags[tag]!!
                                tagList.add(article)
                            } else {
                                tags[tag] = arrayListOf(article)
                                tabsList.add(Tabs(tag, position))
                                position++
                            }
                        }
                    }
                }
                tabList.clear()
                tabList.addAll(tabsList)
                tabsData.value = tabsList
                articleList = tags
            }
        })
    }

    private fun setFirstTag(
        tags: MutableMap<String, MutableList<ArticleList>>,
        tabsList: ArrayList<Tabs>,
        articles: MutableList<ArticleList>
    ){
        val allTag = "ALL"
        tags[allTag] = articles
        tabsList.add(Tabs(allTag, 0))
    }

    fun getArticleList(type: String): MutableList<ArticleList>{
        return if(articleList.containsKey(type)){
            articleList[type]!!
        } else {
            arrayListOf()
        }

    }

    override fun clear() {

    }
}