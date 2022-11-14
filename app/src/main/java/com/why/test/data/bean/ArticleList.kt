package com.why.test.data.bean

/**
 *
 * @author why
 * @date 2022/11/14 11:01
 */
data class ArticleList(var frontmatter: FrontMatter)

data class FrontMatter(val title:String,val date: String,val path: String)