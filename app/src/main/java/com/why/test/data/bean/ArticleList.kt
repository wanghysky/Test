package com.why.test.data.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *
 * @author why
 * @date 2022/11/14 11:01
 */
@Parcelize
data class ArticleList(var frontmatter: FrontMatter?) : Parcelable

@Parcelize
data class FrontMatter(val title: String?,
                       val banner: Banner?,
                       val date: String?,
                       val path: String?,
                       val categories: MutableList<String>?,
                       val tags: MutableList<String>?,
                       val language: String?) : Parcelable

@Parcelize
data class Banner(val childImageSharp: ImageSharp?) : Parcelable

@Parcelize
data class ImageSharp(val fixed: Fixed?) : Parcelable

@Parcelize
data class Fixed(val src: String?) : Parcelable

fun ArticleList.getSrc() : String {
    return frontmatter?.banner?.childImageSharp?.fixed?.src ?: ""
}

fun ArticleList.getTitle() : String {
    return frontmatter?.title ?: ""
}

fun ArticleList.getDate() : String {
    return frontmatter?.date ?: ""
}

fun ArticleList.getPath() : String {
    return frontmatter?.path ?: ""
}


fun ArticleList.getTags() : List<String> {
    return frontmatter?.tags ?: arrayListOf<String>()
}