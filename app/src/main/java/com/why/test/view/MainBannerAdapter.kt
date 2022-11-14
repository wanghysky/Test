package com.why.test.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.why.lib_base.util.ImageLoader
import com.why.lib_base.util.Utils
import com.why.test.R
import com.why.test.activity.WebActivity
import com.why.test.data.bean.*
import java.util.HashMap

/**
 *
 * @author why
 * @date 2022/11/14 17:55
 */
class MainBannerAdapter (var list: MutableList<ArticleList>) :
    RecyclerView.Adapter<MainBannerAdapter.MainBannerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainBannerViewHolder {
        val view =LayoutInflater.from(parent.context)
            .inflate(R.layout.main_article_banner, parent, false)
        return MainBannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainBannerViewHolder, position: Int) {
        holder.updateData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun update(data: MutableList<ArticleList>){
        list = data
        notifyDataSetChanged()
    }


    inner class MainBannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivBanner = itemView.findViewById<SimpleDraweeView>(R.id.iv_image)

        fun updateData(data: ArticleList) {
            data.apply {
                ImageLoader.loadRoundPic(getSrc(), ivBanner, Utils.dp2px(8f).toFloat(), Utils.dp2px(8f).toFloat(), Utils.dp2px(8f).toFloat(), Utils.dp2px(8f).toFloat(), 0f, 0, Utils.dp2px(180f), Utils.dp2px(102f))
            }
            itemView.setOnClickListener {
                WebActivity.launch(itemView.context, data.getPath())
            }
        }
    }
}

