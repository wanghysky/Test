package com.why.test.data.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *
 * @author why
 * @date 2022/11/14 11:01
 */
@Parcelize
data class ArticleList(var frontmatter: FrontMatter) : Parcelable

@Parcelize
data class FrontMatter(val title:String,val date: String,val path: String) : Parcelable