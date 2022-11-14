package com.why.test.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.why.lib_base.util.ImageLoader
import com.why.lib_base.util.Utils
import com.why.test.R
import com.why.test.activity.WebActivity
import com.why.test.data.bean.*

/**
 *
 * @author why
 * @date 2022/11/14 18:34
 */
class MainArticleListAdapter(var list: MutableList<ArticleList>) :
    RecyclerView.Adapter<MainArticleListAdapter.MainArticleListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainArticleListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_article_list_layout, parent, false)
        return MainArticleListViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainArticleListViewHolder, position: Int) {
        holder.updateData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MainArticleListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivBanner = itemView.findViewById<SimpleDraweeView>(R.id.iv_image)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
        private val tvDate = itemView.findViewById<TextView>(R.id.tv_date)
        private val tagView = itemView.findViewById<TagContainerView>(R.id.tag_view)

        fun updateData(data: ArticleList) {
            data.apply {
                tvTitle.text = getTitle().trim()
                val date = getDate().trim()
                if (date.isBlank()) {
                    tvDate.visibility = View.GONE
                } else {
                    tvDate.text = date
                }
                tagView.setTags(getTags())
                ImageLoader.loadRoundPic(getSrc(), ivBanner, Utils.dp2px(8f).toFloat(), 0f, Utils.dp2px(8f).toFloat(), 0f, 0f, 0, Utils.dp2px(150f), Utils.dp2px(84f))
            }
            itemView.setOnClickListener {
                WebActivity.launch(itemView.context, data.getPath())
            }
        }
    }
}

